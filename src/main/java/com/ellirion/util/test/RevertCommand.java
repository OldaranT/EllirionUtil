package com.ellirion.util.test;

import com.ellirion.util.transact.Transaction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.ellirion.util.WorldHelper;

import java.util.List;

public class RevertCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (!TransactionManager.playerTransactions.containsKey(player)) {
                player.sendMessage("You have no transactions to revert");
                return true;
            }

            List<Transaction> transactions = TransactionManager.playerTransactions.get(player);
            transactions.get(transactions.size() - 1).revert();
            transactions.remove(transactions.size() - 1);
            player.sendMessage("Transaction reverted");
            return true;
        }
        return false;
    }
}
