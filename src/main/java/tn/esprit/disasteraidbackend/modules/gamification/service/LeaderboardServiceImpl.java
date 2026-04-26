package tn.esprit.disasteraidbackend.modules.gamification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.gamification.dto.LeaderboardRequest;
import tn.esprit.disasteraidbackend.modules.gamification.dto.LeaderboardResponse;
import tn.esprit.disasteraidbackend.modules.gamification.entity.Leaderboard;
import tn.esprit.disasteraidbackend.modules.gamification.entity.LeaderboardScope;
import tn.esprit.disasteraidbackend.modules.gamification.repository.LeaderboardRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeaderboardServiceImpl implements LeaderboardService {

    private final LeaderboardRepository leaderboardRepository;

    // ── Mapping helpers ──────────────────────────────────────────────────────

    private Leaderboard toEntity(LeaderboardRequest request) {
        return Leaderboard.builder()
                .scope(request.getScope())
                .metric(request.getMetric())
                .snapshotJson(request.getSnapshotJson())
                .lastRefreshedAt(LocalDateTime.now())
                .build();
    }

    private LeaderboardResponse toResponse(Leaderboard l) {
        LeaderboardResponse response = new LeaderboardResponse();
        response.setBoardId(l.getBoardId());
        response.setScope(l.getScope());
        response.setMetric(l.getMetric());
        response.setSnapshotJson(l.getSnapshotJson());
        response.setLastRefreshedAt(l.getLastRefreshedAt());
        return response;
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    public LeaderboardResponse createLeaderboard(LeaderboardRequest request) {
        return toResponse(leaderboardRepository.save(toEntity(request)));
    }

    @Override
    public LeaderboardResponse getLeaderboardById(Long id) {
        Leaderboard l = leaderboardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leaderboard not found with id: " + id));
        return toResponse(l);
    }

    @Override
    public List<LeaderboardResponse> getAllLeaderboards() {
        return leaderboardRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<LeaderboardResponse> getByScope(LeaderboardScope scope) {
        return leaderboardRepository.findByScope(scope)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<LeaderboardResponse> getByMetric(String metric) {
        return leaderboardRepository.findByMetric(metric)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public LeaderboardResponse updateLeaderboard(Long id, LeaderboardRequest request) {
        Leaderboard l = leaderboardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leaderboard not found with id: " + id));
        l.setScope(request.getScope());
        l.setMetric(request.getMetric());
        l.setSnapshotJson(request.getSnapshotJson());
        l.setLastRefreshedAt(LocalDateTime.now());
        return toResponse(leaderboardRepository.save(l));
    }

    @Override
    public LeaderboardResponse refreshSnapshot(Long id, String newSnapshotJson) {
        Leaderboard l = leaderboardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leaderboard not found with id: " + id));
        l.setSnapshotJson(newSnapshotJson);
        l.setLastRefreshedAt(LocalDateTime.now());
        return toResponse(leaderboardRepository.save(l));
    }

    @Override
    public void deleteLeaderboard(Long id) {
        if (!leaderboardRepository.existsById(id)) {
            throw new RuntimeException("Leaderboard not found with id: " + id);
        }
        leaderboardRepository.deleteById(id);
    }
}
