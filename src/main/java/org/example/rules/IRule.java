package org.example.rules;

public interface IRule {
    String getName();
    String getDescription();
    RuleType getType();
    int getPriority();
    void setPriority(int priority);
    boolean isEnabled();
    void setEnabled(boolean enabled);

    RuleResult execute(RuleContext context);
    boolean canExecute(RuleContext context);
}