package tn.esprit.disasteraidbackend.modules.resourceShelter.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class ShelterOccupantUpdateDto {

    @NotNull
    private Integer shelterId;

    @NotNull
    private Integer userId;

    private LocalDateTime checkInAt;   // optionnel
    private LocalDateTime checkOutAt;  // optionnel
    private List<String> needsFlag;

    public Integer getShelterId() { return shelterId; }
    public void setShelterId(Integer shelterId) { this.shelterId = shelterId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public LocalDateTime getCheckInAt() { return checkInAt; }
    public void setCheckInAt(LocalDateTime checkInAt) { this.checkInAt = checkInAt; }
    public LocalDateTime getCheckOutAt() { return checkOutAt; }
    public void setCheckOutAt(LocalDateTime checkOutAt) { this.checkOutAt = checkOutAt; }
    public List<String> getNeedsFlag() { return needsFlag; }
    public void setNeedsFlag(List<String> needsFlag) { this.needsFlag = needsFlag; }
}