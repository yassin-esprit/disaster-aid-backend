package tn.esprit.disasteraidbackend.modules.gamification.service;

import tn.esprit.disasteraidbackend.modules.gamification.dto.LeaderboardRequest;
import tn.esprit.disasteraidbackend.modules.gamification.dto.LeaderboardResponse;
import tn.esprit.disasteraidbackend.modules.gamification.entity.LeaderboardScope;

import java.util.List;

public interface LeaderboardService {

    LeaderboardResponse createLeaderboard(LeaderboardRequest request);

    LeaderboardResponse getLeaderboardById(Long id);

    List<LeaderboardResponse> getAllLeaderboards();

    List<LeaderboardResponse> getByScope(LeaderboardScope scope);

    List<LeaderboardResponse> getByMetric(String metric);

    LeaderboardResponse updateLeaderboard(Long id, LeaderboardRequest request);

    LeaderboardResponse refreshSnapshot(Long id, String newSnapshotJson);

    void deleteLeaderboard(Long id);
}
