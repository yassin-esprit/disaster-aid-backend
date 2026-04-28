package tn.esprit.disasteraidbackend.modules.gamification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.gamification.dto.ChallengeRequest;
import tn.esprit.disasteraidbackend.modules.gamification.dto.ChallengeResponse;
import tn.esprit.disasteraidbackend.modules.gamification.entity.ChallengeStatus;
import tn.esprit.disasteraidbackend.modules.gamification.service.ChallengeService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    // ── Create ───────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<ChallengeResponse> createChallenge(@RequestBody ChallengeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(challengeService.createChallenge(request));
    }

    // ── Read ─────────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<ChallengeResponse>> getAllChallenges() {
        return ResponseEntity.ok(challengeService.getAllChallenges());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeResponse> getChallengeById(@PathVariable Long id) {
        return ResponseEntity.ok(challengeService.getChallengeById(id));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ChallengeResponse>> getByStatus(@PathVariable ChallengeStatus status) {
        return ResponseEntity.ok(challengeService.getByStatus(status));
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<List<ChallengeResponse>> getChallengesByVolunteer(
            @PathVariable Long volunteerId) {
        return ResponseEntity.ok(challengeService.getChallengesByVolunteer(volunteerId));
    }

    // ── Update ───────────────────────────────────────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<ChallengeResponse> updateChallenge(
            @PathVariable Long id,
            @RequestBody ChallengeRequest request) {
        return ResponseEntity.ok(challengeService.updateChallenge(id, request));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<ChallengeResponse> joinChallenge(
            @PathVariable Long id,
            @RequestParam Long volunteerId) {
        return ResponseEntity.ok(challengeService.joinChallenge(id, volunteerId));
    }

    @PostMapping("/{id}/leave")
    public ResponseEntity<ChallengeResponse> leaveChallenge(
            @PathVariable Long id,
            @RequestParam Long volunteerId) {
        return ResponseEntity.ok(challengeService.leaveChallenge(id, volunteerId));
    }

    // ── Delete ───────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChallenge(@PathVariable Long id) {
        challengeService.deleteChallenge(id);
        return ResponseEntity.noContent().build();
    }
}
