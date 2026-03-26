package tn.esprit.disasteraidbackend.modules.coordination.entity;


import jakarta.persistence.*;
import lombok.*;
import tn.esprit.disasteraidbackend.common.BaseEntity;

import java.util.List;

@Entity
@Table(name = "help_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HelpRequest extends BaseEntity {

    @Column(name = "reporter_id")
    private Integer reporterId;        // temp — will be User FK

    @Column(name = "alert_id")
    private Integer alertId;           // temp — will be Alert FK

    @Column(nullable = false)
    private String type;               // RESCUE, MEDICAL, EVACUATION, SUPPLY, SHELTER

    @Column(nullable = false)
    private Integer urgency = 1;       // 1-5

    @Column(nullable = false)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String status = "OPEN";    // OPEN, ASSIGNED, IN_PROGRESS, RESOLVED, CANCELLED

    @Column(name = "assigned_responder_id")
    private Integer assignedResponderId;   // temp — will be User FK

    @OneToMany(mappedBy = "helpRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskAssignment> assignments = new java.util.ArrayList<>();
}