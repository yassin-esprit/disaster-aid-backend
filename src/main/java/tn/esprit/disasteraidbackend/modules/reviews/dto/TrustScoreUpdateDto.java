package tn.esprit.disasteraidbackend.modules.reviews.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import tn.esprit.disasteraidbackend.modules.reviews.entity.EntityType;

public class TrustScoreUpdateDto {

    @NotNull
    private Integer entityId;

    @NotNull
    private EntityType entityType;

    @NotNull
    @DecimalMin("0.0") @DecimalMax("100.0")
    private Double score;

    @NotNull
    @PositiveOrZero
    private Integer reviewCount;

    @NotNull
    @DecimalMin("0.0") @DecimalMax("5.0")
    private Double averageRating;

    @NotNull
    @DecimalMin("0.0") @DecimalMax("1.0")
    private Double completionRate;

    @NotNull
    @DecimalMin("0.0") @DecimalMax("1.0")
    private Double responseRate;

    public Integer getEntityId() { return entityId; }
    public void setEntityId(Integer entityId) { this.entityId = entityId; }

    public EntityType getEntityType() { return entityType; }
    public void setEntityType(EntityType entityType) { this.entityType = entityType; }

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public Integer getReviewCount() { return reviewCount; }
    public void setReviewCount(Integer reviewCount) { this.reviewCount = reviewCount; }

    public Double getAverageRating() { return averageRating; }
    public void setAverageRating(Double averageRating) { this.averageRating = averageRating; }

    public Double getCompletionRate() { return completionRate; }
    public void setCompletionRate(Double completionRate) { this.completionRate = completionRate; }

    public Double getResponseRate() { return responseRate; }
    public void setResponseRate(Double responseRate) { this.responseRate = responseRate; }
}
