package com.acme.stocks;

public class Transaction {
    public final int id;
    public final Trade bought;
    public final Trade sold;
    public final int profit;

    public Transaction(int id, Trade bought, Trade sold) {
        this.id = id;
        this.bought = bought;
        this.sold = sold;
        this.profit = sold.value - bought.value;
    }
}
