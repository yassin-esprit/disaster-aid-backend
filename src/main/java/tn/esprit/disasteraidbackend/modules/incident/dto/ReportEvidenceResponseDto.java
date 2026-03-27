package tn.esprit.disasteraidbackend.modules.incident.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReportEvidenceResponseDto {

    private Integer id;
    private Integer incidentReportId;
    private String fileUrl;
    private String fileType;
    private Double clipScore;
    private LocalDateTime capturedAt;
    private LocalDateTime createdAt;
}



