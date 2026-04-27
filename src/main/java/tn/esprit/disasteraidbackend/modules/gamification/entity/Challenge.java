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

    // e.g. "HOURS", "DISASTER_RESPONSES", "PEOPLE_HELPED"
    @Column(nullable = false)
    private String targetMetric;

    private Float targetValue;

    private Integer rewardPoints;

    private LocalDate startDate;

    private LocalDate endDate;

    // List of volunteer user IDs who joined the challenge
    @ElementCollection
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
