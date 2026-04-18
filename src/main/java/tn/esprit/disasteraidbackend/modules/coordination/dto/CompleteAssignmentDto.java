package tn.esprit.disasteraidbackend.modules.coordination.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteAssignmentDto {
    private String completionNote;
    private Integer rating;  // 1-5

}