package tn.esprit.disasteraidbackend.modules.sensor.entities;

import tn.esprit.disasteraidbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Polygon;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerts")
@Getter
@Setter
public class Alert extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DisasterType disasterType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    private AlertStatus status = AlertStatus.ACTIVE;

    @Column(columnDefinition = "GEOMETRY(POLYGON, 4326)")
    private Polygon affectedArea;

    @Column(columnDefinition = "GEOMETRY(POLYGON, 4326)")
    private Polygon predictedArea2h;

    @Column(columnDefinition = "GEOMETRY(POLYGON, 4326)")
    private Polygon predictedArea6h;

    private Double confidenceScore;

    @ManyToOne
    @JoinColumn(name = "trigger_sensor_id")
    private Sensor triggerSensor;

    @Column(name = "issued_by")
    private Integer issuedBy;

    private LocalDateTime issuedAt;

    private LocalDateTime resolvedAt;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    public enum DisasterType {
        FLOOD, EARTHQUAKE, WILDFIRE, LANDSLIDE, STORM, CHEMICAL
    }

    public enum Severity {
        LOW, MODERATE, HIGH, CRITICAL
    }

    public enum AlertStatus {
        ACTIVE, MONITORING, RESOLVED, FALSE_ALARM
    }
}
