package tn.esprit.disasteraidbackend.modules.coordination.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskAssignmentRequestDto {

    private Integer responderId;
    private Double mlScoreAtAssignment;
    private String notes;
}