package tn.esprit.disasteraidbackend.modules.gamification.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "leaderboards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Leaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeaderboardScope scope;

    // e.g. "HOURS", "POINTS", "IMPACT_SCORE", "DISASTER_RESPONSES"
    @Column(nullable = false)
    private String metric;

    // JSON snapshot of ranked entries: [{"rank":1,"volunteerId":42,"value":120}, ...]
    @Column(columnDefinition = "TEXT")
    private String snapshotJson;

    private LocalDateTime lastRefreshedAt;
}
