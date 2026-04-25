package tn.esprit.disasteraidbackend.modules.coordination.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.disasteraidbackend.common.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "volunteer_offers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerOffer extends BaseEntity {

    @Column(name = "volunteer_id")
    private Integer volunteerId;       // temp — will be User FK

    @Column(columnDefinition = "TEXT")
    private String skills;             // comma-separated for now

    @Column
    private String location;

    @Column(name = "available_from")
    private LocalDateTime availableFrom;

    @Column(name = "available_to")
    private LocalDateTime availableTo;

    @Column(name = "max_radius_km")
    private Double maxRadiusKm = 10.0;

    @Column(nullable = false)
    private String status = "ACTIVE";  // ACTIVE, MATCHED, EXPIRED
}