package tn.esprit.disasteraidbackend.modules.gamification.dto;

import lombok.Data;
import tn.esprit.disasteraidbackend.modules.gamification.entity.LeaderboardScope;

import java.time.LocalDateTime;

@Data
public class LeaderboardResponse {
    private Long boardId;
    private LeaderboardScope scope;
    private String metric;
    private String snapshotJson;
    private LocalDateTime lastRefreshedAt;
}
