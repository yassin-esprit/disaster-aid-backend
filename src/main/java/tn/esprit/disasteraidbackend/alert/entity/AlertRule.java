package tn.esprit.disasteraidbackend.alert.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alert_rule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    /** Field to evaluate: TOTAL_REPORTS, HELP_REQUESTS, ACTIVE_RESPONDERS, AVG_RESPONSE_TIME */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleField field;

    /** Comparison operator */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleOperator operator;

    @Column(nullable = false)
    private Double threshold;

    /** Action to take when rule triggers */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RuleAction action;

    /** Target severity when action is CHANGE_SEVERITY */
    @Enumerated(EnumType.STRING)
    private AlertSeverity targetSeverity;

    @Column(nullable = false)
    private Boolean active;

    private LocalDateTime lastTriggeredAt;

    private Integer triggerCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public enum RuleField {
        TOTAL_REPORTS, HELP_REQUESTS, ACTIVE_RESPONDERS, AVG_RESPONSE_TIME
    }

    public enum RuleOperator {
        GREATER_THAN, LESS_THAN, EQUALS, GREATER_OR_EQUAL, LESS_OR_EQUAL
    }

    public enum RuleAction {
        CHANGE_SEVERITY, NOTIFY, AUTO_RESOLVE
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (active == null) active = true;
        if (triggerCount == null) triggerCount = 0;
    }
}
