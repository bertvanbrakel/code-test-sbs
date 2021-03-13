package com.acme.stocks;

import org.junit.Test;

import java.util.Arrays;

import static com.acme.stocks.TransactionAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class StockTraderTest {

    @Test
    public void no_price_history() {
        var trades = new StockTrader().trade(priceHistory());
        assertEquals(0, trades.getNumTransactions());
    }

    @Test
    public void question_1_4_example() {
        var prices = priceHistory(300, 250, 260, 310, 215, 280);
        var trades = new StockTrader().trade(prices);

        System.out.println("for price input " + Arrays.toString(prices));
        System.out.println("trades are \n" + trades.toJSON());

        var tx0 = trades.getTransaction(0);
        assertThat(tx0)
                .hasBoughtDay(2).hasBoughtValue(250)
                .hasSoldDay(4).hasSoldValue(310)
                .hasProfit(60);

        var tx1 = trades.getTransaction(1);
        assertThat(tx1)
                .hasBoughtDay(5).hasBoughtValue(215)
                .hasSoldDay(6).hasSoldValue(280)
                .hasProfit(65);

        assertEquals(2, trades.getNumTransactions());
        assertEquals(125, trades.getTotalProfit());
    }

    @Test
    public void question_1_4_test() {
        var prices = priceHistory(360, 255, 260, 230, 150, 100, 135, 265, 750, 460, 600);
        var trades = new StockTrader().trade(prices);

        System.out.println("for price input " + Arrays.toString(prices));
        System.out.println("trades are \n" + trades.toJSON());

        var tx0 = trades.getTransaction(0);
        assertThat(tx0)
                .hasBoughtDay(2).hasBoughtValue(255)
                .hasSoldDay(3).hasSoldValue(260)
                .hasProfit(5);

        var tx1 = trades.getTransaction(1);
        assertThat(tx1)
                .hasBoughtDay(6).hasBoughtValue(100)
                .hasSoldDay(9).hasSoldValue(750)
                .hasProfit(650);

        var tx2 = trades.getTransaction(2);
        assertThat(tx2)
                .hasBoughtDay(10).hasBoughtValue(460)
                .hasSoldDay(11).hasSoldValue(600)
                .hasProfit(140);

        assertEquals(3, trades.getNumTransactions());
        assertEquals(795, trades.getTotalProfit());
    }

    @Test
    public void question_1_4_json_output_test() {
        //quick and dirty output test. We should probably compare structure to avoid whitespace and ordering issues)
        var trades = new TransactionHistory();
        trades.add(new Trade(0, 123), new Trade(1, 456));
        trades.add(new Trade(3, 567), new Trade(4, 789));

        var expectJson = "{\n" +
                "  \"transactions\" : {\n" +
                "    \"0\" : {\n" +
                "      \"bought\" : {\n" +
                "        \"day\" : \"1\",\n" +
                "        \"value\" : \"123\"\n" +
                "      },\n" +
                "      \"sold\" : {\n" +
                "        \"day\" : \"2\",\n" +
                "        \"value\" : \"456\"\n" +
                "      },\n" +
                "      \"profit\" : \"333\"\n" +
                "    },\n" +
                "    \"1\" : {\n" +
                "      \"bought\" : {\n" +
                "        \"day\" : \"4\",\n" +
                "        \"value\" : \"567\"\n" +
                "      },\n" +
                "      \"sold\" : {\n" +
                "        \"day\" : \"5\",\n" +
                "        \"value\" : \"789\"\n" +
                "      },\n" +
                "      \"profit\" : \"222\"\n" +
                "    }\n" +
                "  },\n" +
                "  \"total_profit\" : \"555\"\n" +
                "}".replaceAll("'", "\"");

        assertEquals("json invalid", expectJson, trades.toJSON());
    }

    private int[] priceHistory(int... prices) {
        return prices;
    }
}
