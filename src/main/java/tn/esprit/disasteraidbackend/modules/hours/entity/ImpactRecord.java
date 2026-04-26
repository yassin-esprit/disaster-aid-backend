package tn.esprit.disasteraidbackend.modules.hours.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "impact_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImpactRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long impactId;

    // FK → User (volunteer)
    private Long volunteerId;

    private Float totalApprovedHours;

    private Float co2OffsetKg;

    private Integer mealsServed;

    private Integer peopleHelped;

    private Float wasteRemovedKg;

    private Float hoursOfTutoring;

    @Enumerated(EnumType.STRING)
    private ConfidenceLevel confidenceLevel;

    private LocalDateTime lastCalculatedAt;
}
