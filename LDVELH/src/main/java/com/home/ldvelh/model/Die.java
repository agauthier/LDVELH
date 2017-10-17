package com.home.ldvelh.model;

import java.util.Random;

public enum Die {

    SIX_FACES {
        @Override public int roll() {
            return random.nextInt(6) + 1;
        }
    };

    private static final Random random = new Random();

    @SuppressWarnings("WeakerAccess") public abstract int roll();

    public int roll(int nbTimes) {
        int result = 0;
        for (int i = 0; i < nbTimes; i++) {
            result += roll();
        }
        return result;
    }
}
