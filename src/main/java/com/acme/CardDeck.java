package com.acme;

import java.util.*;

public class CardDeck
{
    private static final int HAND_SIZE = 5;
    public static String[] getFullDeck(){
        var suits = Arrays.asList( /* spades*/ "s", /* hearts */ "h", /* clubs */ "c", /* diamonds*/ "d" );
        var ranks = Arrays.asList( "a", "2", "3", "4", "5", "6", "7", "8", "9", "10", "j", "q", "k" );

        var cards = new ArrayList<String>(suits.size() * ranks.size());
        for(var suit : suits){
        for(var rank : ranks)
            cards.add(rank + suit);
        }
        return cards.toArray(new String[]{});
    }

    public static String[] generateRandomHandFrom(String[] cards){
        //let's not modify the original list of cards
        var clone = Arrays.asList(cards);
        Collections.shuffle(clone);

        return clone.subList(0, HAND_SIZE).toArray( new String[]{} );
    }

}
