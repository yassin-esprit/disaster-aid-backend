package tn.esprit.disasteraidbackend.modules.sensor.entities;

import tn.esprit.disasteraidbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "sensors")
@Getter
@Setter
public class Sensor extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SensorType type;

    @Column(nullable = false, columnDefinition = "GEOMETRY(POINT, 4326)")
    private Point location;

    @Column(name = "owner_id")
    private Integer ownerId;

    private String manufacturer;

    @Enumerated(EnumType.STRING)
    private SensorStatus status = SensorStatus.ACTIVE;

    private LocalDateTime lastPingAt;

    private LocalDate calibrationDate;

    public enum SensorType {
        SEISMIC, FLOOD_GAUGE, WEATHER, AIR_QUALITY, FIRE_SMOKE
    }

    public enum SensorStatus {
        ACTIVE, OFFLINE, FAULTY, MAINTENANCE
    }
}
