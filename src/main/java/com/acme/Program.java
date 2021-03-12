package com.acme;

import java.util.Arrays;

public class Program {
    public static void main(String[] args) {
        var cards = CardDeck.getFullDeck();
        var hand = CardDeck.generateRandomHandFrom(cards);

        System.out.println("Hand: " + Arrays.toString(hand));
    }

}