package tn.esprit.disasteraidbackend.alert.controller;

import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.alert.dto.ComparisonDto;
import tn.esprit.disasteraidbackend.alert.entity.Alert;
import tn.esprit.disasteraidbackend.alert.repository.AlertRepository;
import tn.esprit.disasteraidbackend.analytics.m13.entity.DisasterStatistics;
import tn.esprit.disasteraidbackend.analytics.m13.repository.DisasterStatisticsRepository;
import tn.esprit.disasteraidbackend.common.ApiResponse;

@RestController
@RequestMapping("/api/alerts/compare")
public class ComparisonController {

    private final AlertRepository alertRepo;
    private final DisasterStatisticsRepository statsRepo;

    public ComparisonController(AlertRepository alertRepo, DisasterStatisticsRepository statsRepo) {
        this.alertRepo = alertRepo;
        this.statsRepo = statsRepo;
    }

    @GetMapping
    public ApiResponse<ComparisonDto> compare(
            @RequestParam Integer alertIdA,
            @RequestParam Integer alertIdB) {

        Alert a = alertRepo.findById(alertIdA).orElse(null);
        Alert b = alertRepo.findById(alertIdB).orElse(null);
        if (a == null || b == null) {
            return ApiResponse.error("One or both alerts not found");
        }

        DisasterStatistics statsA = statsRepo.findTopByAlertIdOrderByComputedAtDesc(alertIdA).orElse(null);
        DisasterStatistics statsB = statsRepo.findTopByAlertIdOrderByComputedAtDesc(alertIdB).orElse(null);

        return ApiResponse.ok(new ComparisonDto(a, statsA, b, statsB));
    }
}
