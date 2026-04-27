package tn.esprit.disasteraidbackend.analytics.m13.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tn.esprit.disasteraidbackend.analytics.m13.entity.ModelType;

import java.time.LocalDateTime;

public class PredictiveModelRequestDto {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "version is required")
    private String version;

    @NotNull(message = "type is required")
    private ModelType type;

    private String artifactUrl;

    private String metrics;

    private Boolean isActive;

    private LocalDateTime trainedAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ModelType getType() {
        return type;
    }

    public void setType(ModelType type) {
        this.type = type;
    }

    public String getArtifactUrl() {
        return artifactUrl;
    }

    public void setArtifactUrl(String artifactUrl) {
        this.artifactUrl = artifactUrl;
    }

    public String getMetrics() {
        return metrics;
    }

    public void setMetrics(String metrics) {
        this.metrics = metrics;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getTrainedAt() {
        return trainedAt;
    }

    public void setTrainedAt(LocalDateTime trainedAt) {
        this.trainedAt = trainedAt;
    }
}

