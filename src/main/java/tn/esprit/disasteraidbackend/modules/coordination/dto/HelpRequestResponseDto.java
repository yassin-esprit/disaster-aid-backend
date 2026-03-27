package tn.esprit.disasteraidbackend.modules.coordination.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class HelpRequestResponseDto {

    private Integer id;
    private Integer reporterId;
    private Integer alertId;
    private String type;
    private Integer urgency;
    private String location;
    private String description;
    private String status;
    private Integer assignedResponderId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}