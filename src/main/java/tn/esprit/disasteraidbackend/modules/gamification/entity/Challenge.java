package tn.esprit.disasteraidbackend.modules.gamification.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "challenges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long challengeId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String targetMetric;

    private Float targetValue;

    private Integer rewardPoints;

    private LocalDate startDate;

    private LocalDate endDate;

    // ✅ FIXED: FetchType.EAGER prevents "no Session" lazy loading error
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "challenge_participants",
            joinColumns = @JoinColumn(name = "challenge_id")
    )
    @Column(name = "volunteer_id")
    private List<Long> participantIds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeStatus status = ChallengeStatus.UPCOMING;
}
