package com.acme;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class CardDeckTest
{
    @Test
    public void all_cards_generated()
    {
        var cards = CardDeck.getFullDeck();

        assertEquals( 52, cards.length );
    }

    @Test
    public void card_format_is_rank_then_suit()
    {
        var cards = Arrays.asList(CardDeck.getFullDeck());

        assertTrue(cards.contains("10c"));
        assertTrue(cards.contains("ac"));
    }

    /**
     * Bit hard to test that a returned list is shuffled. Checking it's in a different order to the original
     * is problematic as a 'random' shuffle might result in the same order at times
     */
    @Test
    public void question1_1()
    {
        var cards = CardDeck.getFullDeck();
        var hand = CardDeck.generateRandomHandFrom(cards);

        assertEquals( 5, hand.length );
    }

    @Test
    public void question1_2_is_flush()
    {
        assertTrue( CardDeck.isFlush(new String[] { "5d", "2d", "10d", "ad" }));
        assertTrue( CardDeck.isFlush(new String[] { "5c", "2c", "10c", "ac" }));

        assertFalse( CardDeck.isFlush(new String[] { "5d", "2d", "10c" }));
    }
}
