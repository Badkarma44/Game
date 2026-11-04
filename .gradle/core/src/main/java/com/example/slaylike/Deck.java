package com.example.slaylike;

import com.badlogic.gdx.utils.Array;

public class Deck {
    private final Array<Card> drawPile = new Array<>();
    private final Array<Card> discardPile = new Array<>();

    public Deck() {
        // starter deck
        for (int i = 0; i < 2; i++) {
            drawPile.add(new Card("Strike", 1, 6, "deal 6 damage"));
        }
        for (int i = 0; i < 2; i++) {
            drawPile.add(new Card("Defend", 1, 0, "gain 5 block"));
        }
        drawPile.add(new Card("Heavy Hit", 2, 12, "deal 12 damage"));
        drawPile.shuffle();
    }

    public Array<Card> drawCards(int amount) {
        Array<Card> result = new Array<>();

        while (result.size < amount) {
            if (drawPile.size == 0) {
                refillDrawPile();
            }

            if (drawPile.size == 0) {
                break;
            }

            result.add(drawPile.pop());
        }

        return result;
    }

    public void addCard(Card card) {
        discardPile.add(card);
    }

    public void discardHand(Array<Card> hand) {
        for (int i = 0; i < hand.size; i++) {
            discardPile.add(hand.get(i));
        }
        hand.clear();
    }

    private void refillDrawPile() {
        if (discardPile.size == 0) return;

        for (int i = 0; i < discardPile.size; i++) {
            drawPile.add(discardPile.get(i));
        }

        discardPile.clear();
        drawPile.shuffle();
    }
}
