package tn.esprit.disasteraidbackend.modules.opportunity.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.disasteraidbackend.common.BaseEntity;

@Entity
@Table(name = "opportunities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Opportunity extends BaseEntity {

    @Column(name = "org_id", nullable = false)
    private Integer orgId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String category;

    private String location;

    @Column(name = "is_remote", nullable = false)
    private boolean isRemote;

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    @Column(name = "duration_hours")
    private Double durationHours;

    @Column(nullable = false)
    private int slots;

    @Column(name = "filled_slots", nullable = false)
    private int filledSlots = 0;

    @Column(nullable = false)
    private String status = "DRAFT";

    @Column(name = "min_age")
    private Integer minAge;

    @Column(name = "requires_background_check", nullable = false)
    private boolean requiresBackgroundCheck = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "opportunity_required_skills", joinColumns = @JoinColumn(name = "opportunity_id"))
    @Column(name = "skill_id")
    private List<Integer> requiredSkillIds = new ArrayList<>();

    @Column(name = "closed_at")
    private LocalDateTime closedAt;
}
