package tn.esprit.disasteraidbackend.alert.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alert_snapshot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "alert_id", nullable = false)
    private Integer alertId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertSeverity severity;

    private Double radiusKm;

    private Integer totalReports;
    private Integer totalHelpRequests;
    private Integer activeResponders;

    @Column(nullable = false)
    private LocalDateTime snapshotAt;

    @PrePersist
    void prePersist() {
        if (snapshotAt == null) snapshotAt = LocalDateTime.now();
    }
}
