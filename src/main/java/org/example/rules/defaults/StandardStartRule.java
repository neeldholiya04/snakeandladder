package org.example.rules.defaults;

import org.example.rules.*;
import org.example.player.Player;

public class StandardStartRule extends BaseRule {
    public StandardStartRule() {
        super("Standard Start", "All players start at position 1", RuleType.START_GAME, 100);
    }

    @Override
    public RuleResult execute(RuleContext context) {
        for (Player player : context.getPlayers()) {
            player.setPosition(1);
        }
        return RuleResult.success("All players positioned at start (position 1)");
    }
}