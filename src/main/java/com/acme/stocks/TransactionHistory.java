package com.acme.stocks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Note: not threadsafe
 */
public class TransactionHistory {

    private List<Transaction> transactions = new ArrayList<>();
    private int profit = 0;

    public void add(Trade bought, Trade sold) {
        add(new Transaction(transactions.size(), bought, sold));
    }

    private void add(Transaction tx) {
        transactions.add(tx);
        profit += tx.profit;
    }

    public Transaction getTransaction(int id) {
        return transactions.get(id);
    }

    public int getTotalProfit() {
        return profit;
    }

    public int getNumTransactions() {
        return transactions.size();
    }

    @Override
    public String toString() {
        return toJSON();
    }

    public String toJSON() {
        try {
            var mapper = new ObjectMapper();
            var node = node(mapper, this);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing transaction history", e);
        }
    }

    private static ObjectNode node(ObjectMapper mapper, TransactionHistory history) {
        var node = mapper.createObjectNode();
        var txNodes = mapper.createObjectNode();
        for (var tx : history.transactions) {
            txNodes.set(Integer.toString(tx.id), node(mapper, tx));
        }
        node.set("transactions", txNodes);
        node.put("total_profit", Integer.toString(history.profit));

        return node;
    }

    private static ObjectNode node(ObjectMapper mapper, Transaction tx) {
        var node = mapper.createObjectNode();
        node.set("bought", node(mapper, tx.bought));
        node.set("sold", node(mapper, tx.sold));
        node.put("profit", Integer.toString(tx.profit));

        return node;
    }

    private static ObjectNode node(ObjectMapper mapper, Trade trade) {
        var node = mapper.createObjectNode();
        node.put("day", Integer.toString(trade.day));
        node.put("value", Integer.toString(trade.value));
        return node;
    }

}

