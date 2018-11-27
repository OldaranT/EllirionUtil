package com.ellirion.util;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import com.ellirion.util.async.Promise;
import com.ellirion.util.test.SetBlockCommand;

public final class EllirionUtil extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("setblocktostone").setExecutor(new SetBlockCommand());

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
     * Loads a chunk.
     * @param loc the location of the chunk to load.
     */
    public void loadChunk(Location loc) {
        WorldHelper.getBlock(loc);
    }

    /**
     * Schedule promise for execution.
     * @param promise the promise to schedule
     * @return this promise
     */
    public Promise schedulePromise(Promise promise) {
        return promise.schedule();
    }
}
