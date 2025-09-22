package org.example.rules.defaults;

import org.example.rules.*;

public class DoubleSixRule extends BaseRule {
    public DoubleSixRule() {
        super("Double Six", "Roll again if you get 6", RuleType.DICE_ROLL, 75);
    }

    @Override
    public RuleResult execute(RuleContext context) {
        if (context.getDiceRoll() == 6) {
            context.putGameState("extraTurn", true);
            return RuleResult.success("Rolled 6! You get another turn!");
        }
        return RuleResult.skip();
    }
}