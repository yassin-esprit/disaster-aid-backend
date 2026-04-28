package tn.esprit.disasteraidbackend.modules.skills.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VolunteerSkillRequestDto {

    @NotNull
    private Integer volunteerId;

    @NotNull
    private Integer skillId;

    @NotBlank
    private String proficiency;

    private String verificationDocUrl;
}
