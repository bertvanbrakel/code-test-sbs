package com.acme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.google.common.primitives.Ints.tryParse;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

public class PokerDeck {
    private static final int HAND_SIZE = 5;

    /**
     * Return a full deck of cards in no particular order. No jokers
     *
     * <p>NOTE:in a real system I'd likely turn the array of strings into a Collection of Card's.</p>
     */
    public static String[] getFullDeck() {
        var suits = Arrays.asList( /* spades*/ "s", /* hearts */ "h", /* clubs */ "c", /* diamonds*/ "d");
        var ranks = Arrays.asList("a", "2", "3", "4", "5", "6", "7", "8", "9", "10", "j", "q", "k");

        var cards = new ArrayList<String>(suits.size() * ranks.size());
        for (var suit : suits) {
            for (var rank : ranks)
                cards.add(rank + suit);
        }
        return cards.toArray(new String[cards.size()]);
    }

    /**
     * Generate a random hand of {@link #HAND_SIZE} cards from the given deck
     *
     * @param deck
     * @return
     */
    public static String[] generateRandomHandFrom(String[] deck) {
        //let's not modify the original list of cards
        var shuffled = Arrays.asList(deck);
        Collections.shuffle(shuffled);

        //pick the hand out of the shuffled deck
        return shuffled.subList(0, HAND_SIZE).toArray(new String[]{});
    }

    /**
     * Return whether a hand is a straight or  straight flush. A straight flush is just a more specialised form
     * of a straight, so just test for a straight
     *
     * @param h - the hand to test
     */
    public static boolean isStraight(String[] h) {
        //cards ordered by rank order
        var o = stream(h).map(c -> r(c)).sorted().collect(toList());
        //is a sequence or a low ace (so last card an ace, first a 2, then in sequence)
        return s(o) || (o.get(o.size() - 1) == 14 && o.get(0) == 2 && s(o.subList(0, o.size() - 2)));
    }

    /**
     * Return if the given hand is a continuous sequence.
     * <p>
     * NOTE: Should be 'isSequence' but reduced to get code char count down
     * </p>
     *
     * @param h - the hand, sorted by rank order (shortened to reduce code count)
     */
    private static boolean s(List<Integer> h) {
        for (var i = 0; i < h.size() - 1; i++) {
            //should be pulled out into expectNext and next. But, code count
            if (h.get(i + 1) != h.get(i) + 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Extract the card rank order.
     * <p>
     * NOTE: Yes, we're using single letter function to reduce the code char  count
     * </p>
     *
     * @param c - the card
     * @return the rank order. A Jack is 11, Queen 12, King 13, Ace 14
     */
    private static Integer r(String c) {
        var r = c.substring(0, c.length() - 1);
        var i = tryParse(r);
        if (i != null) {
            return i;
        }
        switch (r) {
            case "j":
                return 11;
            case "q":
                return 12;
            case "k":
                return 13;
            case "a":
                return 14; // this should be a constant
        }
        throw new RuntimeException("Invalid rank " + r);
    }
}
