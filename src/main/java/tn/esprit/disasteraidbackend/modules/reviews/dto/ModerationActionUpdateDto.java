package tn.esprit.disasteraidbackend.modules.reviews.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tn.esprit.disasteraidbackend.modules.reviews.entity.ModerationActionType;

public class ModerationActionUpdateDto {

    @NotNull
    private Integer adminId;

    @NotBlank
    private String targetType;

    @NotNull
    private Integer targetId;

    @NotNull
    private ModerationActionType action;

    @NotBlank
    private String reason;

    public Integer getAdminId() { return adminId; }
    public void setAdminId(Integer adminId) { this.adminId = adminId; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }

    public Integer getTargetId() { return targetId; }
    public void setTargetId(Integer targetId) { this.targetId = targetId; }

    public ModerationActionType getAction() { return action; }
    public void setAction(ModerationActionType action) { this.action = action; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
