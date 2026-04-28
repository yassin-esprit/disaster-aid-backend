package tn.esprit.disasteraidbackend.modules.opportunity.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.disasteraidbackend.common.ApiResponse;
import tn.esprit.disasteraidbackend.modules.opportunity.dto.OpportunityStatsDto;
import tn.esprit.disasteraidbackend.modules.opportunity.service.OpportunityStatsService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/opportunities/stats")
@RequiredArgsConstructor
public class OpportunityStatsController {

    private final OpportunityStatsService service;

    @GetMapping
    public ResponseEntity<ApiResponse<OpportunityStatsDto>> getStats() {
        return ResponseEntity.ok(ApiResponse.ok(service.getStats()));
    }
}
