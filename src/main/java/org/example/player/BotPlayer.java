package org.example.player;

import org.example.dice.DiceSet;

public class BotPlayer extends Player {
    public BotPlayer(String id, String name) { super(id, name); }

    @Override
    public int takeTurn(DiceSet diceSet) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {}
        int roll = diceSet.roll();
        System.out.println(getName() + " (Bot) rolled: " + roll);
        return roll;
    }
}
