package tn.esprit.disasteraidbackend.alert.controller;

import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.alert.dto.SlaSummaryDto;
import tn.esprit.disasteraidbackend.alert.entity.SlaMetric;
import tn.esprit.disasteraidbackend.alert.service.SlaService;
import tn.esprit.disasteraidbackend.common.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/sla")
public class SlaController {

    private final SlaService slaService;

    public SlaController(SlaService slaService) {
        this.slaService = slaService;
    }

    @GetMapping
    public ApiResponse<List<SlaMetric>> getAll() {
        return ApiResponse.ok(slaService.getAll());
    }

    @GetMapping("/alert/{alertId}")
    public ApiResponse<List<SlaMetric>> getByAlert(@PathVariable Integer alertId) {
        return ApiResponse.ok(slaService.getByAlert(alertId));
    }

    @GetMapping("/summary")
    public ApiResponse<SlaSummaryDto> getSummary() {
        return ApiResponse.ok(slaService.getSummary());
    }
}
