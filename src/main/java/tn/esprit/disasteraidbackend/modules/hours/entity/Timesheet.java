package tn.esprit.disasteraidbackend.modules.hours.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "timesheets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Timesheet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long timesheetId;

    // FK → Application (Module 07 — managed separately)
    private Long applicationId;

    // FK → User (volunteer)
    private Long volunteerId;

    // FK → User (organisation)
    private Long orgId;

    private Float loggedHours;

    @Column(columnDefinition = "TEXT")
    private String taskDescription;

    private LocalDate workDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimesheetStatus status = TimesheetStatus.PENDING;

    // FK → User (supervisor who approves/rejects)
    private Long supervisorId;

    private LocalDateTime approvedAt;

    private String rejectionReason;
}
