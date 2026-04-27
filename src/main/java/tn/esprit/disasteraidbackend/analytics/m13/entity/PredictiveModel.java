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

import java.time.LocalDateTime;

@Entity
@Table(name = "predictive_model")
@Getter
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

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setType(ModelType type) {
        this.type = type;
    }

    public void setArtifactUrl(String artifactUrl) {
        this.artifactUrl = artifactUrl;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setTrainedAt(LocalDateTime trainedAt) {
        this.trainedAt = trainedAt;
    }
}
