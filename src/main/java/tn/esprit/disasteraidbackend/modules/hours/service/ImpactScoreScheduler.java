package tn.esprit.disasteraidbackend.modules.hours.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImpactScoreScheduler {

    private final ImpactScoreService     impactScoreService;
    private final LeaderboardAutoService leaderboardAutoService;  // ← injected

    /**
     * Runs every night at midnight.
     * Step 1 → recalculate all impact scores (+ auto-award badges)
     * Step 2 → refresh the leaderboard with the new scores
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void recalculateImpactScores() {
        log.info("═══════════════════════════════════════════════════");
        log.info("  ImpactScoreScheduler fired at {}", LocalDateTime.now());
        log.info("═══════════════════════════════════════════════════");

        // Step 1 — scores + badges
        impactScoreService.recalculateAll();

        // Step 2 — leaderboard
        leaderboardAutoService.refreshImpactScoreLeaderboard();
    }

    /**
     * Runs once 10 seconds after startup so you can see it working
     * immediately without waiting until midnight.
     */
    @Scheduled(initialDelay = 10000, fixedDelay = Long.MAX_VALUE)
    public void recalculateOnStartup() {
        log.info("ImpactScoreScheduler — initial run on startup…");

        impactScoreService.recalculateAll();
        leaderboardAutoService.refreshImpactScoreLeaderboard();
    }
}
