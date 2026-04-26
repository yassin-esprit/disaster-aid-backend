package tn.esprit.disasteraidbackend.modules.hours.dto;

import lombok.Data;
import tn.esprit.disasteraidbackend.modules.hours.entity.TimesheetStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TimesheetResponse {
    private Long timesheetId;
    private Long applicationId;
    private Long volunteerId;
    private Long orgId;
    private Float loggedHours;
    private String taskDescription;
    private LocalDate workDate;
    private TimesheetStatus status;
    private Long supervisorId;
    private LocalDateTime approvedAt;
    private String rejectionReason;
}
