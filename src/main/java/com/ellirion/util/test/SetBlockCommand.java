package com.ellirion.util.test;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.util.WorldHelper;

public class SetBlockCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            Location loc = player.getLocation();

            WorldHelper.setBlock(loc, Material.STONE, (byte) 0);
            return true;
        }
        return false;
    }
}
