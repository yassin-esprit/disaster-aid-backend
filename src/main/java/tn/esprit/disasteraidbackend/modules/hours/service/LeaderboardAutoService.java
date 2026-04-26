package tn.esprit.disasteraidbackend.modules.hours.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.gamification.entity.Leaderboard;
import tn.esprit.disasteraidbackend.modules.gamification.entity.LeaderboardScope;
import tn.esprit.disasteraidbackend.modules.gamification.repository.LeaderboardRepository;
import tn.esprit.disasteraidbackend.modules.hours.entity.ImpactRecord;
import tn.esprit.disasteraidbackend.modules.hours.repository.ImpactRecordRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderboardAutoService {

    private final ImpactRecordRepository impactRecordRepository;
    private final LeaderboardRepository  leaderboardRepository;

    private static final String METRIC = "IMPACT_SCORE";

    public void refreshImpactScoreLeaderboard() {

        List<ImpactRecord> records = impactRecordRepository.findAll();

        if (records.isEmpty()) {
            log.info("LeaderboardAutoService — no impact records found, skipping.");
            return;
        }

        // Sort descending by impact score
        records.sort(Comparator.comparingDouble(
                r -> -(r.getImpactScore() != null ? r.getImpactScore() : 0.0)
        ));

        // Build JSON string manually — no Jackson needed
        // Result: [{"rank":1,"volunteerId":42,"value":127.5},{"rank":2,...}]
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < records.size(); i++) {
            ImpactRecord r     = records.get(i);
            int          rank  = i + 1;
            long         volId = r.getVolunteerId();
            double       score = r.getImpactScore() != null ? r.getImpactScore() : 0.0;

            sb.append("{")
              .append("\"rank\":").append(rank).append(",")
              .append("\"volunteerId\":").append(volId).append(",")
              .append("\"value\":").append(score)
              .append("}");

            if (i < records.size() - 1) sb.append(",");
        }
        sb.append("]");

        String snapshotJson = sb.toString();

        // Find existing GLOBAL/IMPACT_SCORE board or create one
        Optional<Leaderboard> existing = leaderboardRepository
                .findByScopeAndMetric(LeaderboardScope.GLOBAL, METRIC);

        Leaderboard board = existing.orElseGet(() -> {
            log.info("LeaderboardAutoService — no GLOBAL/IMPACT_SCORE board found, creating one.");
            return Leaderboard.builder()
                    .scope(LeaderboardScope.GLOBAL)
                    .metric(METRIC)
                    .build();
        });

        board.setSnapshotJson(snapshotJson);
        board.setLastRefreshedAt(LocalDateTime.now());
        leaderboardRepository.save(board);

        log.info("LeaderboardAutoService — leaderboard updated with {} entries.", records.size());
    }
}
