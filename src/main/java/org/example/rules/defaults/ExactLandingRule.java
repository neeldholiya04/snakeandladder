package org.example.rules.defaults;

import org.example.rules.*;

public class ExactLandingRule extends BaseRule {
    public ExactLandingRule() {
        super("Exact Landing", "Must land exactly on last cell to win", RuleType.MOVEMENT, 50);
    }

    @Override
    public RuleResult execute(RuleContext context) {
        int targetPosition = context.getOldPosition() + context.getDiceRoll();
        if (targetPosition > context.getBoard().getLastCell()) {
            context.setNewPosition(context.getOldPosition());
            return RuleResult.success("Cannot move beyond last cell, staying at position " + context.getOldPosition());
        }
        context.setNewPosition(targetPosition);
        return RuleResult.success("");
    }
}