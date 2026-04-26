package tn.esprit.disasteraidbackend.modules.gamification.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "achievements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long achievementId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String iconUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AchievementCategory category;

    // Stored as JSON string (e.g. {"min_hours": 10, "cause": "medical"})
    // For full JSONB support use a custom converter or Hibernate types library
    @Column(columnDefinition = "TEXT")
    private String criteria;

    private Integer points;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rarity rarity;

    @Column(nullable = false)
    private Boolean isActive = true;
}
