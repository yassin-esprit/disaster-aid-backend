package tn.esprit.disasteraidbackend.alert.dto;

public class RuleExecutionResult {
    private Integer ruleId;
    private String ruleName;
    private Integer alertId;
    private String alertTitle;
    private boolean triggered;
    private String actionTaken;
    private double actualValue;
    private double threshold;

    public RuleExecutionResult(Integer ruleId, String ruleName, Integer alertId, String alertTitle,
                                boolean triggered, String actionTaken, double actualValue, double threshold) {
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.alertId = alertId;
        this.alertTitle = alertTitle;
        this.triggered = triggered;
        this.actionTaken = actionTaken;
        this.actualValue = actualValue;
        this.threshold = threshold;
    }

    public Integer getRuleId() { return ruleId; }
    public String getRuleName() { return ruleName; }
    public Integer getAlertId() { return alertId; }
    public String getAlertTitle() { return alertTitle; }
    public boolean isTriggered() { return triggered; }
    public String getActionTaken() { return actionTaken; }
    public double getActualValue() { return actualValue; }
    public double getThreshold() { return threshold; }
}
