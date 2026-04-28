package tn.esprit.disasteraidbackend.modules.skills.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SkillResponseDto {
    private Integer id;
    private String name;
    private String slug;
    private String category;
    private Integer parentSkillId;
    private String description;
    private Boolean isVerifiable;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
