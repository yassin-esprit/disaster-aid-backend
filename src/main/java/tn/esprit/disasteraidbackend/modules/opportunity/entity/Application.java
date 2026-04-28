package tn.esprit.disasteraidbackend.modules.opportunity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.disasteraidbackend.common.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Application extends BaseEntity {

    @Column(name = "opportunity_id", nullable = false)
    private Integer opportunityId;

    @Column(name = "volunteer_id", nullable = false)
    private Integer volunteerId;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "motivation_letter", columnDefinition = "TEXT")
    private String motivationLetter;

    @Column(name = "ml_match_score")
    private Double mlMatchScore;

    @Column(name = "decided_at")
    private LocalDateTime decidedAt;
}
