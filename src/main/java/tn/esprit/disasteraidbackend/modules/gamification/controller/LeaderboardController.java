package tn.esprit.disasteraidbackend.modules.gamification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.gamification.dto.LeaderboardRequest;
import tn.esprit.disasteraidbackend.modules.gamification.dto.LeaderboardResponse;
import tn.esprit.disasteraidbackend.modules.gamification.entity.LeaderboardScope;
import tn.esprit.disasteraidbackend.modules.gamification.service.LeaderboardService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/leaderboards")
@RequiredArgsConstructor
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    // ── Create ───────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<LeaderboardResponse> createLeaderboard(@RequestBody LeaderboardRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(leaderboardService.createLeaderboard(request));
    }

    // ── Read ─────────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<LeaderboardResponse>> getAllLeaderboards() {
        return ResponseEntity.ok(leaderboardService.getAllLeaderboards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaderboardResponse> getLeaderboardById(@PathVariable Long id) {
        return ResponseEntity.ok(leaderboardService.getLeaderboardById(id));
    }

    @GetMapping("/scope/{scope}")
    public ResponseEntity<List<LeaderboardResponse>> getByScope(@PathVariable LeaderboardScope scope) {
        return ResponseEntity.ok(leaderboardService.getByScope(scope));
    }

    @GetMapping("/metric/{metric}")
    public ResponseEntity<List<LeaderboardResponse>> getByMetric(@PathVariable String metric) {
        return ResponseEntity.ok(leaderboardService.getByMetric(metric));
    }

    // ── Update ───────────────────────────────────────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<LeaderboardResponse> updateLeaderboard(
            @PathVariable Long id,
            @RequestBody LeaderboardRequest request) {
        return ResponseEntity.ok(leaderboardService.updateLeaderboard(id, request));
    }

    @PutMapping("/{id}/refresh")
    public ResponseEntity<LeaderboardResponse> refreshSnapshot(
            @PathVariable Long id,
            @RequestBody String snapshotJson) {
        return ResponseEntity.ok(leaderboardService.refreshSnapshot(id, snapshotJson));
    }

    // ── Delete ───────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeaderboard(@PathVariable Long id) {
        leaderboardService.deleteLeaderboard(id);
        return ResponseEntity.noContent().build();
    }
}
