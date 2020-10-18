package com.ellirion.util;

import net.minecraft.server.v1_16_R2.BlockPosition;
import net.minecraft.server.v1_16_R2.MinecraftServer;
import net.minecraft.server.v1_16_R2.NBTTagCompound;
import net.minecraft.server.v1_16_R2.TileEntity;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import com.ellirion.util.async.Promise;
import com.ellirion.util.transact.Transaction;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class WorldHelper {

    private static final Map<Chunk, Long> CHUNK_ACTIVITY = new HashMap<>();
    private static final BlockingQueue<PendingBlockChange> PENDING = new LinkedBlockingQueue<>();

    /**
     * Safely set a block in the world at the given coordinates to the given material and metadata.
     * @param world The world to set the block in
     * @param x The X coordinate of the block
     * @param y The Y coordinate of the block
     * @param z The Z coordinate of the block
     * @param material The Material of the block
     * @param blockData The BlockData of the block
     * @return A {@link BlockChangeTransaction} that has been applied
     */
    public static Transaction setBlock(World world, int x, int y, int z, Material material, BlockData blockData) {
        return setBlock(new Location(world, x, y, z), material, blockData);
    }

    /**
     * Safely set a block in the world at the given location to the given material and metadata.
     * @param location The Location of the block
     * @param material The Material of the block
     * @param blockData The BlockData of the block
     * @return A {@link BlockChangeTransaction} that has been applied
     */
    public static Transaction setBlock(Location location, Material material, BlockData blockData) {
        Transaction t = new BlockChangeTransaction(new BlockChange(location, material, blockData));
        t.apply();
        return t;
    }

    /**
     * Safely set a block in the world at the given location to the given material, metadata and nbtdata.
     * @param location The Location of the block
     * @param material The Material of the block
     * @param blockData The BlockData of the block
     * @param nbt The nbtdata of the block
     * @return A {@link BlockChangeTransaction} that has been applied
     */
    public static Transaction setBlock(Location location, Material material, BlockData blockData, NBTTagCompound nbt) {
        Transaction t = new BlockChangeTransaction(new BlockChange(location, material, blockData, nbt));
        t.apply();
        return t;
    }

    /**
     * Safely set a block in the world at the given coordinates to the given material, metadata and nbtdata.
     * @param world The world to set the block in
     * @param x The X coordinate of the block
     * @param y The Y coordinate of the block
     * @param z The Z coordinate of the block
     * @param material The Material of the block
     * @param blockData The BlockData of the block
     * @param nbtData The nbtdata of the Block
     * @return A {@link BlockChangeTransaction} that has been applied
     */
    public static Transaction setBlock(World world, int x, int y, int z, Material material, BlockData blockData, NBTTagCompound nbtData) {
        return setBlock(new Location(world, x, y, z), material, blockData, nbtData);
    }

    /**
     * Safely get a block from the world at the given coordinates.
     * @param world The World to get the block from
     * @param x The X coordinate of the block
     * @param y The Y coordinate of the block
     * @param z The Z coordinate of the block
     * @return The Block
     */
    public static Block getBlock(World world, int x, int y, int z) {
        // Load chunk if necessary
        int chunkX = Math.floorDiv(x, 16);
        int chunkZ = Math.floorDiv(z, 16);

        if (!world.isChunkLoaded(chunkX, chunkZ) &&
            (MinecraftServer.getServer() == null || Thread.currentThread() !=
                                                    MinecraftServer.getServer().serverThread)) {
            Promise p = new Promise<>(finisher -> {
                world.loadChunk(chunkX, chunkZ);
                markChunkActive(world.getChunkAt(chunkX, chunkZ));
                finisher.resolve(null);
            }, false);

            p.await();
        }

        // Get the block
        return world.getBlockAt(x, y, z);
    }

    /**
     * Marks the given Chunk as active.
     * @param c The Chunk to mark as active
     */
    public static void markChunkActive(Chunk c) {
        CHUNK_ACTIVITY.put(c, System.currentTimeMillis());
    }

    /**
     * Marks the given Chunk as inactive.
     * @param c The Chunk to mark as inactive
     */
    public static void markChunkInactive(Chunk c) {
        CHUNK_ACTIVITY.remove(c);
    }

    /**
     * Check if the Chunk {@code c} is marked 'active' (has been accessed recently).
     * @param c The Chunk to check
     * @return Whether this Chunk is marked active or not
     */
    public static boolean isChunkActive(Chunk c) {
        return System.currentTimeMillis() - CHUNK_ACTIVITY.getOrDefault(c, 0L) < 5000;
    }

    /**
     * Safely get a block from the world at the given location.
     * @param loc The Location of the block
     * @return The Block
     */
    public static Block getBlock(Location loc) {
        return getBlock(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    private static Promise<BlockChange> scheduleSetBlock(BlockChange change) {
        PendingBlockChange pending = new PendingBlockChange(change);
        PENDING.add(pending);
        return pending.promise;
    }

    /**
     * Run scheduled block changes.
     */
    public static void run() {
        // 125 is the "magic value" of block updates that can be applied
        // per tick without having a noticeable performance impact.
        for (int i = 0; i < 125; i++) {
            PendingBlockChange pending = PENDING.poll();
            if (pending == null) {
                return;
            }

            pending.apply();
        }
    }

    /**
     * @param dir in what direction it needs to look.
     * @param block the block from where it needs to look.
     * @param world the world where you are looking in.
     * @return return the found block.
     */
    public static Block getRelativeBlock(BlockFace dir, Block block, World world) {
        int x = block.getX();
        int y = block.getY();
        int z = block.getZ();
        switch (dir) {
            case NORTH:
                // NORTH
                return getBlock(world, x, y, z - 1);
            case EAST:
                // EAST
                return getBlock(world, x + 1, y, z);
            case SOUTH:
                // SOUTH
                return getBlock(world, x, y, z + 1);
            case WEST:
                // WEST
                return getBlock(world, x - 1, y, z);
            case UP:
                // UP
                return getBlock(world, x, y + 1, z);
            case DOWN:
                // DOWN
                return getBlock(world, x, y - 1, z);

            default:
                throw new IndexOutOfBoundsException();
        }
    }

    private static class BlockChange {

        private Location location;
        private Material material;
        private BlockData blockData;
        private NBTTagCompound nbtData;

        BlockChange(final Location location, final Material material, final BlockData blockData) {
            this(location, material, blockData, null);
        }

        BlockChange(final Location location, final Material material, final BlockData blockData, final NBTTagCompound nbtData) {
            this.location = location;
            this.material = material;
            this.blockData = blockData;
            this.nbtData = nbtData;
        }

        /**
         * Apply the BlockChange
         * @return a BlockChange to revert this BlockChange
         */
        BlockChange apply() {
            // Note what the current block state was so we can revert back to it.
            Block block = getBlock(location);
            BlockChange blockChange;

            // Check if the block we're trying to change is a tile entity
            TileEntity tileEntityRevert = ((CraftWorld) location.getWorld()).getHandle()
                    .getTileEntity(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            if (tileEntityRevert != null) {
                NBTTagCompound ntc = tileEntityRevert.save(new NBTTagCompound());
                ntc.setInt("x", location.getBlockX());
                ntc.setInt("y", location.getBlockY());
                ntc.setInt("z", location.getBlockZ());
                blockChange = new BlockChange(location, block.getType(), block.getBlockData(), ntc);

                // Load empty NBT data into the block, prevents items from spilling on to the ground if the result of
                // the BlockChange is not a tile entity
                tileEntityRevert.load(null, new NBTTagCompound());
            } else {
                blockChange = new BlockChange(location, block.getType(), block.getBlockData());
            }

            // Apply the changes we were supposed to make.
            // Set Material and BlockData
            block.setType(material);
            if (blockData != null) {
                block.setBlockData(blockData);
            }

            // Load NBT data
            if (nbtData != null) {
                TileEntity tileEntityApply = ((CraftWorld) location.getWorld()).getHandle()
                        .getTileEntity(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
                if (tileEntityApply != null) {
                    // First argument is unused
                    tileEntityApply.load(null, nbtData);
                }
            }
            // Return the BlockChange to be used for reverting.
            return blockChange;
        }
    }

    private static class PendingBlockChange {

        private BlockChange blockChange;
        private Promise<BlockChange> promise;

        PendingBlockChange(final BlockChange change) {
            this.blockChange = change;
            promise = new Promise<>();
        }

        BlockChange apply() {
            BlockChange previous = blockChange.apply();
            promise.getFinisher().resolve(previous);
            return previous;
        }
    }

    private static class BlockChangeTransaction extends Transaction {

        private BlockChange before;
        private BlockChange after;

        BlockChangeTransaction(final BlockChange blockChange) {
            before = null;
            after = blockChange;
        }

        @Override
        protected Promise<Boolean> applier() {
            return scheduleSetBlock(after).then(blockChange -> {
                before = blockChange;
                return true;
            });
        }

        @Override
        protected Promise<Boolean> reverter() {
            return scheduleSetBlock(before).then(blockChange -> true);
        }
    }
}
