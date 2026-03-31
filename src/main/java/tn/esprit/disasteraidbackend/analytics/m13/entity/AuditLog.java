package tn.esprit.disasteraidbackend.analytics.m13.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    @Column(name = "actor_id", nullable = false, updatable = false)
    private Integer actorId;

    @Column(nullable = false, updatable = false, length = 100)
    private String action;

    @Column(nullable = false, updatable = false, length = 100)
    private String entityType;

    @Column(updatable = false)
    private Integer entityId;

    @Column(columnDefinition = "TEXT", updatable = false)
    private String previousValue;

    @Column(columnDefinition = "TEXT", updatable = false)
    private String newValue;

    @Column(updatable = false)
    private String ipAddress;

    @Column(updatable = false)
    private String userAgent;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, updatable = false)
    private Boolean isMLGenerated;

    @Column(updatable = false)
    private String mlModelVersion;

    @PrePersist
    void prePersist() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
        if (isMLGenerated == null) {
            isMLGenerated = Boolean.FALSE;
        }
    }
}

