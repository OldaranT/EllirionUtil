package com.ellirion.util.test;

import com.ellirion.util.transact.Transaction;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionManager {

    public static final Map<Player, List<Transaction>> playerTransactions = new HashMap<>();
}
