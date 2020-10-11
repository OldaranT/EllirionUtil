package com.ellirion.util;

import com.ellirion.util.test.RevertCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.ellirion.util.async.Promise;
import com.ellirion.util.test.SetBlockCommand;

public final class EllirionUtil extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("setblocktostone").setExecutor(new SetBlockCommand());
        getCommand("reverttransaction").setExecutor(new RevertCommand());

        Promise.setSyncRunner(r -> Bukkit.getScheduler().runTask(this, r));
        Promise.setAsyncRunner(r -> Bukkit.getScheduler().runTaskAsynchronously(this, r));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, WorldHelper::run, 1L, 1L);

        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin disabled.");
    }

    /**
     * Set a block in the world.
     * @param loc the location of the block to set.
     * @param mat the material to set the block to.
     * @param meta the metadate to set on the block.
     * @param nbt the NBT data to set on the block.
     */
    /*public void setBlock(Location loc, Material mat, byte meta, NBTTagCompound nbt) {
        WorldHelper.setBlock(loc, mat, meta, nbt);
    }

     */

    /**
     * Schedule promise for execution.
     * @param promise the promise to schedule
     * @return this promise
     */
    public Promise schedulePromise(Promise promise) {
        return promise.schedule();
    }
}
