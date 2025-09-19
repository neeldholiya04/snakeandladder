package org.example.dice;

import java.util.ArrayList;
import java.util.List;

public class DiceSet {
    private static DiceSet instance;
    private final List<Dice> dice;

    private DiceSet(int count) {
        dice = new ArrayList<>();
        for (int i = 0; i < count; i++) dice.add(new Dice());
    }

    public static DiceSet getInstance(int count) {
        if (instance == null) {
            synchronized (DiceSet.class) {
                if (instance == null) {
                    instance = new DiceSet(count);
                }
            }
        }
        return instance;
    }

    public int roll() {
        return dice.stream().mapToInt(Dice::roll).sum();
    }
}