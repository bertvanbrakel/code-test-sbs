package com.acme.stocks;


public class StockTrader {

    /**
     * Return the trades which mould maximise our returns
     *
     * @param priceHistory the list of stock prices indexed by day (day is 1 based)
     *
     * @return the transactions that would have produced the max amount of profit
     */
    public TransactionHistory calculateMaxProfitTrades(int[] priceHistory) {
        /*
        The ideas is find all increasing sequences in the price history list, and buy at the lowest value (local min) of
        the sequence, and sell at the highest (local max).
         */
        var trades = new TransactionHistory();
        int localMinPos = 0;
        for (var currentPos = 1; currentPos < priceHistory.length; currentPos++) {
            var currentPrice = priceHistory[currentPos];
            var prevPrice = priceHistory[currentPos - 1];
            //price dropped, so a decreasing sequence
            if (prevPrice > currentPrice) {
                localMinPos = currentPos;
            }
            var nextPos = currentPos + 1;
            var isEnd = nextPos == priceHistory.length;
            //if the current price is the peak, then sell.
            if (prevPrice <= currentPrice && (isEnd || currentPrice > priceHistory[nextPos])) {
                var bought = new Trade(localMinPos, priceHistory[localMinPos]);
                var sold = new Trade(currentPos, currentPrice);
                trades.add(bought, sold);
            }
        }
        return trades;
    }


}
