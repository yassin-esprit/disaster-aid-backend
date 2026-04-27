package tn.esprit.disasteraidbackend.alert.service;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.alert.dto.RuleExecutionResult;
import tn.esprit.disasteraidbackend.alert.entity.Alert;
import tn.esprit.disasteraidbackend.alert.entity.AlertRule;
import tn.esprit.disasteraidbackend.alert.entity.AlertStatus;
import tn.esprit.disasteraidbackend.alert.repository.AlertRepository;
import tn.esprit.disasteraidbackend.alert.repository.AlertRuleRepository;
import tn.esprit.disasteraidbackend.analytics.m13.entity.DisasterStatistics;
import tn.esprit.disasteraidbackend.analytics.m13.repository.DisasterStatisticsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RuleEngineService {

    private final AlertRuleRepository ruleRepo;
    private final AlertRepository alertRepo;
    private final DisasterStatisticsRepository statsRepo;

    public RuleEngineService(AlertRuleRepository ruleRepo, AlertRepository alertRepo,
                              DisasterStatisticsRepository statsRepo) {
        this.ruleRepo = ruleRepo;
        this.alertRepo = alertRepo;
        this.statsRepo = statsRepo;
    }

    public List<AlertRule> getAllRules() {
        return ruleRepo.findAll();
    }

    public AlertRule createRule(AlertRule rule) {
        return ruleRepo.save(rule);
    }

    public AlertRule updateRule(Integer id, AlertRule updated) {
        return ruleRepo.findById(id).map(rule -> {
            rule.setName(updated.getName());
            rule.setDescription(updated.getDescription());
            rule.setField(updated.getField());
            rule.setOperator(updated.getOperator());
            rule.setThreshold(updated.getThreshold());
            rule.setAction(updated.getAction());
            rule.setTargetSeverity(updated.getTargetSeverity());
            rule.setActive(updated.getActive());
            return ruleRepo.save(rule);
        }).orElse(null);
    }

    public boolean deleteRule(Integer id) {
        if (!ruleRepo.existsById(id)) return false;
        ruleRepo.deleteById(id);
        return true;
    }

    /** Evaluate all active rules against all active alerts and return results */
    public List<RuleExecutionResult> evaluateRules() {
        List<AlertRule> rules = ruleRepo.findByActiveTrueOrderByCreatedAtDesc();
        List<Alert> activeAlerts = alertRepo.findByStatusInOrderBySeverityAscReportedAtDesc(
                List.of(AlertStatus.ACTIVE, AlertStatus.MONITORING));

        List<RuleExecutionResult> results = new ArrayList<>();

        for (AlertRule rule : rules) {
            for (Alert alert : activeAlerts) {
                DisasterStatistics stats = statsRepo
                        .findTopByAlertIdOrderByComputedAtDesc(alert.getAlertId())
                        .orElse(null);
                if (stats == null) continue;

                double actual = getFieldValue(rule.getField(), stats);
                boolean triggered = evaluate(actual, rule.getOperator(), rule.getThreshold());

                String actionTaken = "none";
                if (triggered) {
                    actionTaken = executeAction(rule, alert);
                    rule.setLastTriggeredAt(LocalDateTime.now());
                    rule.setTriggerCount(rule.getTriggerCount() + 1);
                    ruleRepo.save(rule);
                }

                results.add(new RuleExecutionResult(
                        rule.getId(), rule.getName(),
                        alert.getAlertId(), alert.getTitle(),
                        triggered, actionTaken, actual, rule.getThreshold()
                ));
            }
        }
        return results;
    }

    private double getFieldValue(AlertRule.RuleField field, DisasterStatistics stats) {
        return switch (field) {
            case TOTAL_REPORTS -> stats.getTotalReports();
            case HELP_REQUESTS -> stats.getTotalHelpRequests();
            case ACTIVE_RESPONDERS -> stats.getActiveResponders();
            case AVG_RESPONSE_TIME -> stats.getAvgResponseTimeMin() != null ? stats.getAvgResponseTimeMin() : 0;
        };
    }

    private boolean evaluate(double actual, AlertRule.RuleOperator op, double threshold) {
        return switch (op) {
            case GREATER_THAN -> actual > threshold;
            case LESS_THAN -> actual < threshold;
            case EQUALS -> actual == threshold;
            case GREATER_OR_EQUAL -> actual >= threshold;
            case LESS_OR_EQUAL -> actual <= threshold;
        };
    }

    private String executeAction(AlertRule rule, Alert alert) {
        return switch (rule.getAction()) {
            case CHANGE_SEVERITY -> {
                if (rule.getTargetSeverity() != null) {
                    alert.setSeverity(rule.getTargetSeverity());
                    alertRepo.save(alert);
                    yield "Severity changed to " + rule.getTargetSeverity();
                }
                yield "No target severity set";
            }
            case NOTIFY -> "Notification sent for " + alert.getTitle();
            case AUTO_RESOLVE -> {
                alert.setStatus(AlertStatus.RESOLVED);
                alert.setResolvedAt(LocalDateTime.now());
                alertRepo.save(alert);
                yield "Alert auto-resolved";
            }
        };
    }
}
