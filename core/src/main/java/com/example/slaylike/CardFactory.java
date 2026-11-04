package com.example.slaylike;

import java.util.Random;

public class CardFactory {
    private static final Random random = new Random();

    public static Card createRandomRewardCard() {
        Card[] rewards = {
            new Card("Strike+", 1, 7, "deal 7 damage"),
            new Card("Piercing Blow", 2, 10, "deal 10 damage"),
            new Card("Guard", 1, 0, "gain 6 block"),
            new Card("Power Slash", 3, 15, "deal 15 damage"),
            new Card("Quick Jab", 1, 5, "deal 5 damage"),
            new Card("Fortify", 1, 0, "gain 8 block")
        };

        return rewards[random.nextInt(rewards.length)];
    }
}
