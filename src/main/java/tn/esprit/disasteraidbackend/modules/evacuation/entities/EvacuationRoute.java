package tn.esprit.disasteraidbackend.modules.evacuation.entities;

import tn.esprit.disasteraidbackend.common.BaseEntity;
import tn.esprit.disasteraidbackend.modules.sensor.entities.Alert;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.LineString;

import java.time.LocalDateTime;

@Entity
@Table(name = "evacuation_routes")
@Getter
@Setter
public class EvacuationRoute extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "alert_id", nullable = false)
    private Alert alert;

    @Column(name = "to_shelter_id")
    private Integer toShelterId;

    @Column(columnDefinition = "GEOMETRY(LINESTRING, 4326)", nullable = false)
    private LineString path;

    private Integer estimatedDurationMin;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private RouteStatus status = RouteStatus.PLANNED;

    private LocalDateTime activatedAt;

    private LocalDateTime closedAt;

    public enum RouteStatus {
        PLANNED, ACTIVE, BLOCKED, CLOSED
    }
}