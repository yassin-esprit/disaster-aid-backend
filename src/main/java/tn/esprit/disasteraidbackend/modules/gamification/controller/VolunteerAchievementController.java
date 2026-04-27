package tn.esprit.disasteraidbackend.modules.gamification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.gamification.dto.VolunteerAchievementRequest;
import tn.esprit.disasteraidbackend.modules.gamification.dto.VolunteerAchievementResponse;
import tn.esprit.disasteraidbackend.modules.gamification.service.VolunteerAchievementService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/volunteer-achievements")
@RequiredArgsConstructor
public class VolunteerAchievementController {

    private final VolunteerAchievementService volunteerAchievementService;

    // ── Award (Create) ───────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<VolunteerAchievementResponse> awardAchievement(
            @RequestBody VolunteerAchievementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(volunteerAchievementService.awardAchievement(request));
    }

    // ── Read ─────────────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<VolunteerAchievementResponse>> getAll() {
        return ResponseEntity.ok(volunteerAchievementService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VolunteerAchievementResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(volunteerAchievementService.getById(id));
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<List<VolunteerAchievementResponse>> getByVolunteer(
            @PathVariable Long volunteerId) {
        return ResponseEntity.ok(volunteerAchievementService.getByVolunteer(volunteerId));
    }

    @GetMapping("/volunteer/{volunteerId}/displayed")
    public ResponseEntity<List<VolunteerAchievementResponse>> getDisplayedByVolunteer(
            @PathVariable Long volunteerId) {
        return ResponseEntity.ok(volunteerAchievementService.getDisplayedByVolunteer(volunteerId));
    }

    // ── Toggle Display ───────────────────────────────────────────────────────

    @PutMapping("/{id}/display")
    public ResponseEntity<VolunteerAchievementResponse> toggleDisplay(
            @PathVariable Long id,
            @RequestParam boolean value) {
        return ResponseEntity.ok(volunteerAchievementService.toggleDisplay(id, value));
    }

    // ── Delete ───────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        volunteerAchievementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
