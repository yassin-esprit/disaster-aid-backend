package tn.esprit.disasteraidbackend.modules.coordination.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskAssignmentResponseDto {

    private Integer id;
    private Integer helpRequestId;
    private Integer responderId;
    private String status;
    private Double mlScoreAtAssignment;
    private Integer responseTimeMin;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}