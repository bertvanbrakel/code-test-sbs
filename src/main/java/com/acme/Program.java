package com.acme;

import java.util.Arrays;

/**
 * This program prints out a random poker hand of cards, and then tests whether it is a straight
 */
public class Program {
    public static void main(String[] args) {
        var cards = PokerDeck.getFullDeck();
        var hand = PokerDeck.generateRandomHandFrom(cards);

        System.out.println("Hand: " + Arrays.toString(hand));
        System.out.println("Is straight or straight flush: " + PokerDeck.isStraight(hand));
    }

}