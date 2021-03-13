package com.acme.stocks;

public class Trade {
    public final int index;
    public final int day;
    public final int value;

    public Trade(int index, int value) {
        this.index = index;
        this.day = index + 1;
        this.value = value;
    }
}
