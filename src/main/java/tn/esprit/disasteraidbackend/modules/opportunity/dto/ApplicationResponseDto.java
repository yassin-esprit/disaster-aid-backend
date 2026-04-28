package tn.esprit.disasteraidbackend.modules.opportunity.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ApplicationResponseDto {
    private Integer id;
    private Integer opportunityId;
    private Integer volunteerId;
    private String status;
    private String motivationLetter;
    private Double mlMatchScore;
    private LocalDateTime decidedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
