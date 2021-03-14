package com.acme.stocks;

public class Trade {
    public final int priceHistoryIndex;
    public final int day;
    public final int value;

    public Trade(int priceHistoryIndex, int value) {
        this.priceHistoryIndex = priceHistoryIndex;
        this.day = priceHistoryIndex + 1;
        this.value = value;
    }
}
