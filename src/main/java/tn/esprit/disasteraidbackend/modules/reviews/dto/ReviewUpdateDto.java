package tn.esprit.disasteraidbackend.modules.reviews.dto;

import jakarta.validation.constraints.*;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ReviewStatus;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ReviewTarget;

public class ReviewUpdateDto {

    @NotNull
    private Integer authorId;

    @NotNull
    private Integer targetId;

    @NotNull
    private ReviewTarget targetType;

    @NotNull
    @Min(1) @Max(5)
    private Integer rating;

    @NotBlank
    private String body;

    private Integer opportunityId;

    private Boolean isVerifiedParticipant;

    @NotNull
    private ReviewStatus status;

    public Integer getAuthorId() { return authorId; }
    public void setAuthorId(Integer authorId) { this.authorId = authorId; }

    public Integer getTargetId() { return targetId; }
    public void setTargetId(Integer targetId) { this.targetId = targetId; }

    public ReviewTarget getTargetType() { return targetType; }
    public void setTargetType(ReviewTarget targetType) { this.targetType = targetType; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public Integer getOpportunityId() { return opportunityId; }
    public void setOpportunityId(Integer opportunityId) { this.opportunityId = opportunityId; }

    public Boolean getIsVerifiedParticipant() { return isVerifiedParticipant; }
    public void setIsVerifiedParticipant(Boolean isVerifiedParticipant) { this.isVerifiedParticipant = isVerifiedParticipant; }

    public ReviewStatus getStatus() { return status; }
    public void setStatus(ReviewStatus status) { this.status = status; }
}
