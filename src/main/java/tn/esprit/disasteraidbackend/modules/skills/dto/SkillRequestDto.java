package tn.esprit.disasteraidbackend.modules.skills.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillRequestDto {

    @NotBlank
    private String name;

    private String slug;

    @NotBlank
    private String category;

    private Integer parentSkillId;

    private String description;

    private Boolean isVerifiable;

    private Boolean isActive;
}
