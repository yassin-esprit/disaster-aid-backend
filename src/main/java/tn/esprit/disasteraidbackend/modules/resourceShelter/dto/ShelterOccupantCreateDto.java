package tn.esprit.disasteraidbackend.modules.resourceShelter.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public class ShelterOccupantCreateDto {

    @NotNull
    private Integer shelterId;

    @NotNull
    private Integer userId;

    private List<String> needsFlag;

    public Integer getShelterId() { return shelterId; }
    public void setShelterId(Integer shelterId) { this.shelterId = shelterId; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public List<String> getNeedsFlag() { return needsFlag; }
    public void setNeedsFlag(List<String> needsFlag) { this.needsFlag = needsFlag; }
}