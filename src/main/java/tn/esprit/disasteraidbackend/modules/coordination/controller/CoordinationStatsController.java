package tn.esprit.disasteraidbackend.modules.coordination.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.common.ApiResponse;
import tn.esprit.disasteraidbackend.modules.coordination.dto.CoordinationStatsDto;
import tn.esprit.disasteraidbackend.modules.coordination.service.CoordinationStatsService;

@RestController
@RequestMapping("/api/v1/coordination")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CoordinationStatsController {

    private final CoordinationStatsService statsService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<CoordinationStatsDto>> getStats() {
        return ResponseEntity.ok(ApiResponse.ok(statsService.getStats()));
    }
}