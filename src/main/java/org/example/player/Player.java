package org.example.player;

import org.example.dice.DiceSet;

public abstract class Player {
    private final String id;
    private final String name;
    private int position;

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        this.position = 1;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getPosition() { return position; }
    public void setPosition(int pos) { this.position = pos; }
    public abstract int takeTurn(DiceSet diceSet);
}
