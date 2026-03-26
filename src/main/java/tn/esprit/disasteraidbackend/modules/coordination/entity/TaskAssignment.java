package tn.esprit.disasteraidbackend.modules.coordination.entity;


import jakarta.persistence.*;
import lombok.*;
import tn.esprit.disasteraidbackend.common.BaseEntity;

@Entity
@Table(name = "task_assignments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskAssignment extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "help_request_id", nullable = false)
    private HelpRequest helpRequest;

    @Column(name = "responder_id")
    private Integer responderId;       // temp — will be User FK

    @Column(nullable = false)
    private String status = "ASSIGNED";  // ASSIGNED, ACCEPTED, REJECTED, COMPLETED, ABANDONED

    @Column(name = "ml_score_at_assignment")
    private Double mlScoreAtAssignment = 0.0;

    @Column(name = "response_time_min")
    private Integer responseTimeMin;

    @Column(columnDefinition = "TEXT")
    private String notes;
}