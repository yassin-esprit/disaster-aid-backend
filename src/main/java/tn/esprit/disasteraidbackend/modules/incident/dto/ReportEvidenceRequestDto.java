package tn.esprit.disasteraidbackend.modules.incident.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReportEvidenceRequestDto {

    private String fileUrl;
    private String fileType;
    private Double clipScore;
    private LocalDateTime capturedAt;
}