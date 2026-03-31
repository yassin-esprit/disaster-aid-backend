package tn.esprit.disasteraidbackend.analytics.m14.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "churn_prediction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChurnPrediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prediction_id")
    private Integer predictionId;

    @Column(nullable = false)
    private Integer volunteerId;

    @Column(nullable = false)
    private Double predictedChurnProb;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InterventionType interventionType;

    @Column(nullable = false)
    private LocalDateTime predictedAt;

    @Column(nullable = false)
    private Boolean reactivated;

    private LocalDateTime reactivatedAt;

    private String modelVersion;

    @PrePersist
    void prePersist() {
        if (predictedAt == null) {
            predictedAt = LocalDateTime.now();
        }
        if (reactivated == null) {
            reactivated = Boolean.FALSE;
        }
    }
}

