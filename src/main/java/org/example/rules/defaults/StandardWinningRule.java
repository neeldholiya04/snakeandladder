package org.example.rules.defaults;

import org.example.rules.*;

public class StandardWinningRule extends BaseRule {
    public StandardWinningRule() {
        super("Standard Win", "First player to reach last cell wins", RuleType.WINNING, 100);
    }

    @Override
    public RuleResult execute(RuleContext context) {
        if (context.getCurrentPlayer().getPosition() == context.getBoard().getLastCell()) {
            return RuleResult.gameEnd(context.getCurrentPlayer().getName() + " wins the game!");
        }
        return RuleResult.skip();
    }
}