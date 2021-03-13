package com.acme.stocks;

import org.assertj.core.api.AbstractAssert;

public class TransactionAssert extends AbstractAssert<TransactionAssert, Transaction> {

    public static TransactionAssert assertThat(Transaction actual) {
        return new TransactionAssert(actual);
    }

    public TransactionAssert(Transaction actual) {
        super(actual, TransactionAssert.class);
    }

    public TransactionAssert matches(Transaction expect) {
        isNotNull();
        hasBoughtDay(expect.bought.day);
        hasBoughtValue(expect.bought.value);
        hasSoldDay(expect.sold.day);
        hasSoldValue(expect.sold.value);
        hasProfit(expect.profit);
        return this;
    }

    public TransactionAssert hasBoughtDay(int date) {
        isNotNull();
        var actualDay = actual.bought.day;
        if (actualDay != date) {
            failWithMessage("Expected transaction to have bought date '%s' but was '%s'",
                    date, actualDay);
        }
        return this;
    }

    public TransactionAssert hasBoughtValue(int value) {
        isNotNull();
        var actualValue = actual.bought.value;
        if (actualValue != value) {
            failWithMessage("Expected transaction to have bought value '%s' but was '%s'",
                    value, actualValue);
        }
        return this;
    }

    public TransactionAssert hasSoldDay(int date) {
        isNotNull();
        var actualDay = actual.sold.day;
        if (actualDay != date) {
            failWithMessage("Expected transaction to have sold date '%s' but was '%s'",
                    date, actualDay);
        }
        return this;
    }

    public TransactionAssert hasSoldValue(int value) {
        isNotNull();
        var actualValue = actual.sold.value;
        if (actualValue != value) {
            failWithMessage("Expected transaction to have sold value '%s' but was '%s'",
                    value, actualValue);
        }
        return this;
    }

    public TransactionAssert hasProfit(int expectProfit) {
        isNotNull();
        if (actual.profit != expectProfit) {
            failWithMessage("Expected transaction to have profit '%s' but was '%s'",
                    expectProfit, actual.profit);
        }
        return this;
    }

}