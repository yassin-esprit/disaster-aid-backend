package tn.esprit.disasteraidbackend.modules.reviews.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
//noinspection JpaDataSourceORMInspection
@Table(name = "moderation_actions")
public class ModerationAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer actionId;


    @Column(name = "admin_id", nullable = false)
    private Integer adminId;


    @Column(name = "target_type", nullable = false, length = 50)
    private String targetType;


    @Column(name = "target_id", nullable = false)
    private Integer targetId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ModerationActionType action;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String reason;


    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
