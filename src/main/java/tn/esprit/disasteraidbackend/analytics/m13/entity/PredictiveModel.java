package tn.esprit.disasteraidbackend.analytics.m13.entity;

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
@Table(name = "predictive_model")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictiveModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_id")
    private Integer modelId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String version;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModelType type;

    private String artifactUrl;

    @Column(columnDefinition = "TEXT")
    private String metrics;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private LocalDateTime trainedAt;

    @PrePersist
    void prePersist() {
        if (trainedAt == null) {
            trainedAt = LocalDateTime.now();
        }
        if (isActive == null) {
            isActive = Boolean.FALSE;
        }
    }
}

