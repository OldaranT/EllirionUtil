package com.ellirion.util.test;

import com.ellirion.util.transact.Transaction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.util.WorldHelper;

import java.util.LinkedList;
import java.util.List;

public class SetBlockCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            Location loc = player.getLocation();
            loc = loc.add(0, -1, 0);

            Transaction t = WorldHelper.setBlock(loc, Material.STONE, null);

            if (TransactionManager.playerTransactions.containsKey(player)) {
                List<Transaction> transactions = TransactionManager.playerTransactions.get(player);
                transactions.add(t);
            } else {
                List<Transaction> transactions = new LinkedList<>();
                transactions.add(t);
                TransactionManager.playerTransactions.put(player, transactions);
            }
            return true;
        }
        return false;
    }
}
