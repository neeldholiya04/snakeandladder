package org.example.rules.defaults;

import org.example.rules.*;
import org.example.player.Player;

public class KillRule extends BaseRule {
    public KillRule() {
        super("Kill Players", "Send other players back to start if you land on them", RuleType.KILLING, 80);
    }

    @Override
    public RuleResult execute(RuleContext context) {
        int currentPos = context.getCurrentPlayer().getPosition();
        StringBuilder message = new StringBuilder();

        for (Player player : context.getPlayers()) {
            if (player != context.getCurrentPlayer() && player.getPosition() == currentPos) {
                player.setPosition(1);
                message.append(player.getName()).append(" sent back to start! ");
            }
        }

        if (message.length() > 0) {
            return RuleResult.success(message.toString().trim());
        }
        return RuleResult.skip();
    }
}