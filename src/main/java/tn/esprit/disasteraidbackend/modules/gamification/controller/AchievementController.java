package tn.esprit.disasteraidbackend.modules.gamification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.gamification.dto.AchievementRequest;
import tn.esprit.disasteraidbackend.modules.gamification.dto.AchievementResponse;
import tn.esprit.disasteraidbackend.modules.gamification.entity.AchievementCategory;
import tn.esprit.disasteraidbackend.modules.gamification.entity.Rarity;
import tn.esprit.disasteraidbackend.modules.gamification.service.AchievementService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/achievements")
@RequiredArgsConstructor
public class AchievementController {

    private final AchievementService achievementService;

    // ── Create ───────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<AchievementResponse> createAchievement(@RequestBody AchievementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(achievementService.createAchievement(request));
    }

    // ── Read ─────────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<AchievementResponse>> getAllAchievements() {
        return ResponseEntity.ok(achievementService.getAllAchievements());
    }

    @GetMapping("/active")
    public ResponseEntity<List<AchievementResponse>> getActiveAchievements() {
        return ResponseEntity.ok(achievementService.getActiveAchievements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AchievementResponse> getAchievementById(@PathVariable Long id) {
        return ResponseEntity.ok(achievementService.getAchievementById(id));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<AchievementResponse>> getByCategory(
            @PathVariable AchievementCategory category) {
        return ResponseEntity.ok(achievementService.getByCategory(category));
    }

    @GetMapping("/rarity/{rarity}")
    public ResponseEntity<List<AchievementResponse>> getByRarity(@PathVariable Rarity rarity) {
        return ResponseEntity.ok(achievementService.getByRarity(rarity));
    }

    // ── Update ───────────────────────────────────────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<AchievementResponse> updateAchievement(
            @PathVariable Long id,
            @RequestBody AchievementRequest request) {
        return ResponseEntity.ok(achievementService.updateAchievement(id, request));
    }

    // ── Delete ───────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        achievementService.deleteAchievement(id);
        return ResponseEntity.noContent().build();
    }
}
