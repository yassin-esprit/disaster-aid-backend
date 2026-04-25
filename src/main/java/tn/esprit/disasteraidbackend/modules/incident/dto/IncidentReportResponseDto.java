package tn.esprit.disasteraidbackend.modules.incident.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class IncidentReportResponseDto {

    private Integer id;
    private Integer reporterId;
    private Integer alertId;
    private String disasterType;
    private String severity;
    private String location;
    private String description;
    private String status;
    private Double credibilityScore;
    private Integer upvoteCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}