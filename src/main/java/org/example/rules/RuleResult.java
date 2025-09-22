package org.example.rules;

public class RuleResult {
    private final boolean executed;
    private final String message;
    private final Object data;
    private final boolean shouldContinue;
    private final boolean gameEnded;

    public RuleResult(boolean executed, String message, Object data, boolean shouldContinue, boolean gameEnded) {
        this.executed = executed;
        this.message = message;
        this.data = data;
        this.shouldContinue = shouldContinue;
        this.gameEnded = gameEnded;
    }

    public static RuleResult success(String message) {
        return new RuleResult(true, message, null, true, false);
    }

    public static RuleResult success(String message, Object data) {
        return new RuleResult(true, message, data, true, false);
    }

    public static RuleResult gameEnd(String message) {
        return new RuleResult(true, message, null, false, true);
    }

    public static RuleResult skip() {
        return new RuleResult(false, "", null, true, false);
    }

    public static RuleResult halt(String message) {
        return new RuleResult(true, message, null, false, false);
    }

    public boolean isExecuted() { return executed; }
    public String getMessage() { return message; }
    public Object getData() { return data; }
    public boolean shouldContinue() { return shouldContinue; }
    public boolean isGameEnded() { return gameEnded; }
}