package org.example.dice;

import java.util.Random;

class Dice {
    private static final int FACES = 6;
    private final Random rand = new Random();

    public int roll() { return rand.nextInt(FACES) + 1; }
}
