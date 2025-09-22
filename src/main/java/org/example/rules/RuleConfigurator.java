package org.example.rules;

import java.util.Scanner;

public class RuleConfigurator {
    private final RuleManager ruleManager;
    private final Scanner scanner;

    public RuleConfigurator(RuleManager ruleManager, Scanner scanner) {
        this.ruleManager = ruleManager;
        this.scanner = scanner;
    }

    public void configureRules() {
        System.out.println("\n=== Rule Configuration ===");
        System.out.println("1. View current rules");
        System.out.println("2. Enable/Disable rules");
        System.out.println("3. Change rule priorities");
        System.out.println("4. Use default configuration");
        System.out.println("0. Continue with current configuration");

        System.out.print("Choose option: ");
        String choice = scanner.nextLine().trim();

        switch (choice) {
            case "1" -> viewRules();
            case "2" -> toggleRules();
            case "3" -> changePriorities();
            case "4" -> useDefaultConfiguration();
            case "0" -> System.out.println("Using current rule configuration");
            default -> {
                System.out.println("Invalid option. Using current configuration.");
                return;
            }
        }

        if (!"0".equals(choice)) {
            configureRules();
        }
    }

    private void viewRules() {
        System.out.println("\n=== Current Rules ===");
        for (RuleType type : RuleType.values()) {
            System.out.println("\n" + type.getDescription() + ":");
            ruleManager.getRulesByType(type).forEach(rule ->
                    System.out.printf("  [%s] %s (Priority: %d) - %s%n",
                            rule.isEnabled() ? "✓" : "✗",
                            rule.getName(),
                            rule.getPriority(),
                            rule.getDescription()));
        }
    }

    private void toggleRules() {
        System.out.println("\n=== Enable/Disable Rules ===");
        ruleManager.getAllRules().forEach(rule -> {
            System.out.printf("[%s] %s - %s%n",
                    rule.isEnabled() ? "✓" : "✗",
                    rule.getName(),
                    rule.getDescription());
        });

        System.out.print("\nEnter rule name to toggle (or 'done'): ");
        String ruleName = scanner.nextLine().trim();

        if (!"done".equalsIgnoreCase(ruleName)) {
            IRule rule = ruleManager.getRule(ruleName);
            if (rule != null) {
                rule.setEnabled(!rule.isEnabled());
                System.out.println(ruleName + " is now " + (rule.isEnabled() ? "enabled" : "disabled"));
                toggleRules();
            } else {
                System.out.println("Rule not found!");
                toggleRules();
            }
        }
    }

    private void changePriorities() {
        System.out.println("\n=== Change Rule Priorities ===");
        ruleManager.getAllRules().forEach(rule ->
                System.out.printf("%s (Current priority: %d)%n", rule.getName(), rule.getPriority()));

        System.out.print("\nEnter rule name to change priority (or 'done'): ");
        String ruleName = scanner.nextLine().trim();

        if (!"done".equalsIgnoreCase(ruleName)) {
            IRule rule = ruleManager.getRule(ruleName);
            if (rule != null) {
                System.out.print("Enter new priority (higher = executes first): ");
                try {
                    int priority = Integer.parseInt(scanner.nextLine().trim());
                    ruleManager.setRulePriority(ruleName, priority);
                    System.out.println("Priority updated for " + ruleName);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid priority number!");
                }
                changePriorities();
            } else {
                System.out.println("Rule not found!");
                changePriorities();
            }
        }
    }

    private void useDefaultConfiguration() {
        ruleManager.getAllRules().forEach(rule -> rule.setEnabled(true));
        ruleManager.setRulePriority("Standard Start", 100);
        ruleManager.setRulePriority("Standard Win", 100);
        ruleManager.setRulePriority("Kill Players", 80);
        ruleManager.setRulePriority("Double Six", 75);
        ruleManager.setRulePriority("Exact Landing", 50);
        System.out.println("Reset to default rule configuration");
    }
}