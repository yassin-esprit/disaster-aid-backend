package tn.esprit.disasteraidbackend.alert.controller;

import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.alert.dto.DataQualityReportDto;
import tn.esprit.disasteraidbackend.alert.service.DataQualityService;
import tn.esprit.disasteraidbackend.common.ApiResponse;

@RestController
@RequestMapping("/api/data-quality")
public class DataQualityController {

    private final DataQualityService service;

    public DataQualityController(DataQualityService service) {
        this.service = service;
    }

    @GetMapping("/analyze")
    public ApiResponse<DataQualityReportDto> analyze() {
        return ApiResponse.ok(service.analyze());
    }
}
