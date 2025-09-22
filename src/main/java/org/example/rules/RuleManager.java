package org.example.rules;

import org.example.rules.defaults.*;
import java.util.*;
import java.util.stream.Collectors;

public class RuleManager {
    private final List<IRule> rules;
    private final Map<RuleType, List<IRule>> rulesByType;

    public RuleManager() {
        this.rules = new ArrayList<>();
        this.rulesByType = new HashMap<>();
        initializeDefaultRules();
    }

    private void initializeDefaultRules() {
        addRule(new StandardStartRule());
        addRule(new StandardWinningRule());
        addRule(new ExactLandingRule());
        addRule(new DoubleSixRule());
        addRule(new KillRule());
    }

    public void addRule(IRule rule) {
        rules.add(rule);
        rulesByType.computeIfAbsent(rule.getType(), k -> new ArrayList<>()).add(rule);
        sortRulesByPriority();
    }

    public void removeRule(String ruleName) {
        rules.removeIf(rule -> rule.getName().equals(ruleName));
        rulesByType.values().forEach(list -> list.removeIf(rule -> rule.getName().equals(ruleName)));
    }

    public void enableRule(String ruleName, boolean enabled) {
        rules.stream()
                .filter(rule -> rule.getName().equals(ruleName))
                .forEach(rule -> rule.setEnabled(enabled));
    }

    public void setRulePriority(String ruleName, int priority) {
        rules.stream()
                .filter(rule -> rule.getName().equals(ruleName))
                .forEach(rule -> rule.setPriority(priority));
        sortRulesByPriority();
    }

    private void sortRulesByPriority() {
        rules.sort((r1, r2) -> Integer.compare(r2.getPriority(), r1.getPriority()));
        rulesByType.values().forEach(list ->
                list.sort((r1, r2) -> Integer.compare(r2.getPriority(), r1.getPriority())));
    }

    public List<RuleResult> executeRules(RuleType type, RuleContext context) {
        List<RuleResult> results = new ArrayList<>();
        List<IRule> typeRules = rulesByType.getOrDefault(type, new ArrayList<>());

        for (IRule rule : typeRules) {
            if (rule.canExecute(context)) {
                RuleResult result = rule.execute(context);
                if (result.isExecuted()) {
                    results.add(result);
                    if (result.isGameEnded() || !result.shouldContinue()) {
                        break;
                    }
                }
            }
        }

        return results;
    }

    public List<IRule> getAllRules() {
        return new ArrayList<>(rules);
    }

    public List<IRule> getRulesByType(RuleType type) {
        return new ArrayList<>(rulesByType.getOrDefault(type, new ArrayList<>()));
    }

    public IRule getRule(String name) {
        return rules.stream()
                .filter(rule -> rule.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}