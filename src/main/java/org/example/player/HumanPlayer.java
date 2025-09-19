package org.example.player;

import org.example.dice.DiceSet;

public class HumanPlayer extends Player {
    public HumanPlayer(String id, String name) {
        super(id, name);
    }

    @Override
    public int takeTurn(DiceSet diceSet) {
        int roll = diceSet.roll();
        System.out.println(getName() + " rolled: " + roll);
        return roll;
    }
}