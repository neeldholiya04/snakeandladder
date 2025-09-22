package org.example.rules;

public abstract class BaseRule implements IRule {
    protected String name;
    protected String description;
    protected RuleType type;
    protected int priority;
    protected boolean enabled;

    public BaseRule(String name, String description, RuleType type, int priority) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.priority = priority;
        this.enabled = true;
    }

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    @Override
    public RuleType getType() { return type; }

    @Override
    public int getPriority() { return priority; }

    @Override
    public void setPriority(int priority) { this.priority = priority; }

    @Override
    public boolean isEnabled() { return enabled; }

    @Override
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    @Override
    public boolean canExecute(RuleContext context) {
        return enabled;
    }
}