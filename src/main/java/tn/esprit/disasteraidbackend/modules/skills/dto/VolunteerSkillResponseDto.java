package tn.esprit.disasteraidbackend.modules.skills.dto;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VolunteerSkillResponseDto {
    private Integer id;
    private Integer volunteerId;
    private Integer skillId;
    private String proficiency;
    @JsonProperty("isVerified")
    private Boolean isVerified; 
    private String verificationDocUrl;
    private int endorsementCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
