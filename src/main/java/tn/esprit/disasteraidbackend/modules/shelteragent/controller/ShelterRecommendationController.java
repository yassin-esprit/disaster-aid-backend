package tn.esprit.disasteraidbackend.modules.shelteragent.controller;

import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.modules.shelteragent.dto.NearbyShelterDto;
import tn.esprit.disasteraidbackend.modules.shelteragent.dto.ShelterRecommendationRequest;
import tn.esprit.disasteraidbackend.modules.shelteragent.dto.ShelterRecommendationResponse;
import tn.esprit.disasteraidbackend.modules.shelteragent.entity.ShelterAgent;
import tn.esprit.disasteraidbackend.modules.shelteragent.service.ShelterRecommendationService;

import java.util.List;

@RestController
@RequestMapping("/api/shelters")
public class ShelterRecommendationController {

    private final ShelterRecommendationService recommendationService;

    public ShelterRecommendationController(ShelterRecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public List<ShelterAgent> getAllShelters() {
        return recommendationService.getAllShelters();
    }

    @GetMapping("/nearby")
    public List<NearbyShelterDto> getNearbyShelters(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(required = false) Double maxDistanceKm,
            @RequestParam(required = false) String city
    ) {
        return recommendationService.fetchCandidates(lat, lng, maxDistanceKm, city);
    }

    @PostMapping("/recommend")
    public ShelterRecommendationResponse recommend(@RequestBody ShelterRecommendationRequest request) {
        return recommendationService.rankShelters(request);
    }
}