package tn.esprit.disasteraidbackend.modules.gamification.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "volunteer_achievements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VolunteerAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vaId;

    // FK → User (volunteer)
    @Column(nullable = false)
    private Long volunteerId;

    // FK → Achievement
    @Column(nullable = false)
    private Long achievementId;

    private LocalDateTime earnedAt;

    @Column(nullable = false)
    private Boolean isDisplayed = true;
}
