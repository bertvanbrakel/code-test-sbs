package com.acme;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PokerDeckTest {
    @Test
    public void all_cards_generated() {
        var cards = PokerDeck.getFullDeck();

        assertEquals("deck size correct", 52, cards.length);
        assertEquals("no duplicate cards", 52, Stream.of(cards).distinct().count());
    }

    @Test
    public void card_format_is_rank_then_suit() {
        var cards = Arrays.asList(PokerDeck.getFullDeck());

        assertTrue("invalid format", cards.contains("10c"));
        assertTrue("invalid format", cards.contains("ac"));
    }

    @Test
    public void question1_1_generate_random_hand() {

        //Bit hard to test that a returned list is shuffled. Checking it's in a different order to the original
        //is problematic as a 'random' shuffle might result in the same order at times

        var cards = PokerDeck.getFullDeck();
        var hand = PokerDeck.generateRandomHandFrom(cards);

        assertEquals("correct hand size", 5, hand.length);
        assertEquals("no duplicates", 5, Stream.of(hand).distinct().count());
    }

    @Test
    public void question1_2_is_straight_same_suit() {
        assertTrue("low cards", PokerDeck.isStraight(hand("2d", "3d", "4d", "5d", "6d")));
        assertTrue("mid cards", PokerDeck.isStraight(hand("7d", "8d", "9d", "10d", "jd")));
        assertTrue("high cards", PokerDeck.isStraight(hand("9d", "10d", "jd", "qd", "kd")));
    }

    @Test
    public void question1_2_is_straight_mixed_suit() {
        assertTrue("low cards", PokerDeck.isStraight(hand("2d", "3c", "4h", "5s", "6d")));
        assertTrue("mid cards", PokerDeck.isStraight(hand("7d", "8c", "9h", "10s", "jd")));
        assertTrue("high cards", PokerDeck.isStraight(hand("9d", "10c", "jh", "qs", "kd")));
    }

    @Test
    public void question1_2_is_straight_high_ace() {
        assertTrue("high ace same suit", PokerDeck.isStraight(hand("10d", "jd", "qd", "kd", "ad")));
        assertTrue("high ace mixed suit", PokerDeck.isStraight(hand("10d", "jc", "qh", "ks", "ad")));
    }

    @Test
    public void question1_2_is_straight_low_ace() {
        assertTrue("low ace same suit", PokerDeck.isStraight(hand("2d", "3d", "4d", "5d", "ad")));
        assertTrue("low ace mixed suit", PokerDeck.isStraight(hand("2d", "3c", "4h", "5s", "ad")));
    }

    @Test
    public void question1_2_is_not_straight() {
        assertFalse("out of sequence same suit", PokerDeck.isStraight(hand("2d", "3d", "4d", "6d", "7d")));
        assertFalse("out of sequence mixed suit", PokerDeck.isStraight(hand("2d", "3c", "4h", "6s", "7d")));

        assertFalse("out of sequence low ace same suit", PokerDeck.isStraight(hand("3d", "4d", "5d", "6d", "ad")));
        assertFalse("out of sequence low ace mixed suit", PokerDeck.isStraight(hand("3d", "4c", "5h", "6s", "ad")));

        assertFalse("out of sequence high ace same suit", PokerDeck.isStraight(hand("9d", "10d", "jd", "qd", "ad")));
        assertFalse("out of sequence high ace mixed suit", PokerDeck.isStraight(hand("9d", "10c", "jh", "qs", "ad")));
    }

    /**
     * Return the given hand as shuffled to ensure no CardDeck invocations assumes order
     */
    private static String[] hand(String... cards) {
        var shuffled = Arrays.asList(cards);
        Collections.shuffle(shuffled);

        return shuffled.toArray(new String[cards.length]);
    }

}
