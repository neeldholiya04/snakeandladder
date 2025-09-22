package org.example.rules;

public enum RuleType {
    START_GAME("Start Game Rules"),
    TURN_START("Turn Start Rules"),
    DICE_ROLL("Dice Roll Rules"),
    MOVEMENT("Movement Rules"),
    LANDING("Landing Rules"),
    WINNING("Winning Rules"),
    KILLING("Killing Rules"),
    TURN_END("Turn End Rules");

    private final String description;

    RuleType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}