package tn.esprit.disasteraidbackend.modules.hours.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TimesheetRequest {
    private Long applicationId;
    private Long volunteerId;
    private Long orgId;
    private Float loggedHours;
    private String taskDescription;
    private LocalDate workDate;
    private Long supervisorId;
}
