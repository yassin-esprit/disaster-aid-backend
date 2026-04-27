package tn.esprit.disasteraidbackend.modules.gamification.dto;

import lombok.Data;
import tn.esprit.disasteraidbackend.modules.gamification.entity.LeaderboardScope;

@Data
public class LeaderboardRequest {
    private LeaderboardScope scope;
    private String metric;
    private String snapshotJson;
}
