package tn.esprit.disasteraidbackend.alert.controller;

import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.alert.dto.RiskZoneDto;
import tn.esprit.disasteraidbackend.alert.service.RiskZoneService;
import tn.esprit.disasteraidbackend.common.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/risk-zones")
public class RiskZoneController {

    private final RiskZoneService service;

    public RiskZoneController(RiskZoneService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<RiskZoneDto>> getRiskZones() {
        return ApiResponse.ok(service.getRiskZones());
    }
}
