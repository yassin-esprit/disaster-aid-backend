package tn.esprit.disasteraidbackend.modules.evacuation.entities;

import tn.esprit.disasteraidbackend.common.BaseEntity;
import tn.esprit.disasteraidbackend.modules.sensor.entities.GeofenceZone;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "zone_shelter_assignments")
@Getter
@Setter
public class ZoneShelterAssignment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "geofence_zone_id", nullable = false)
    private GeofenceZone geofenceZone;

    @Column(name = "shelter_id", nullable = false)
    private Integer shelterId;

    private Integer priority;

    private Integer estimatedUsersCount;

    private LocalDateTime createdAt;
}