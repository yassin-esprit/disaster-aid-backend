package tn.esprit.disasteraidbackend.modules.sensor.entities;

import tn.esprit.disasteraidbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Polygon;

import java.time.LocalDateTime;

@Entity
@Table(name = "geofence_zones")
@Getter
@Setter
public class GeofenceZone extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "alert_id", nullable = false)
    private Alert alert;

    @Column(columnDefinition = "GEOMETRY(POLYGON, 4326)", nullable = false)
    private Polygon polygon;

    @Enumerated(EnumType.STRING)
    private ZoneType zoneType;

    private Double radius;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    public enum ZoneType {
        EVACUATION, QUARANTINE, SAFE, RESTRICTED
    }
}
