package tn.esprit.disasteraidbackend.modules.gamification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.gamification.entity.Leaderboard;
import tn.esprit.disasteraidbackend.modules.gamification.entity.LeaderboardScope;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {

    List<Leaderboard> findByScope(LeaderboardScope scope);

    Optional<Leaderboard> findByScopeAndMetric(LeaderboardScope scope, String metric);

    List<Leaderboard> findByMetric(String metric);
}
