package tn.esprit.disasteraidbackend.modules.incident.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IncidentReportRequestDto {

    private Integer reporterId;
    private Integer alertId;
    private String disasterType;
    private String severity;
    private String location;
    private String description;
    private String status;

}