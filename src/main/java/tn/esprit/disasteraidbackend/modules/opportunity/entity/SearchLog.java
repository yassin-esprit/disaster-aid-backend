package tn.esprit.disasteraidbackend.modules.opportunity.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import tn.esprit.disasteraidbackend.common.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "search_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchLog extends BaseEntity {

    @Column(name = "volunteer_id", nullable = false)
    private Integer volunteerId;

    @Column(name = "opportunity_id", nullable = false)
    private Integer opportunityId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    private Double score;

    @CreationTimestamp
    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;
}
