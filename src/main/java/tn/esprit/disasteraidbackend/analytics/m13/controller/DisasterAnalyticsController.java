package tn.esprit.disasteraidbackend.analytics.m13.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.disasteraidbackend.analytics.m13.dto.DisasterAnalyticsReportDto;
import tn.esprit.disasteraidbackend.analytics.m13.dto.PredictiveModelRequestDto;
import tn.esprit.disasteraidbackend.analytics.m13.entity.AuditLog;
import tn.esprit.disasteraidbackend.analytics.m13.entity.DisasterStatistics;
import tn.esprit.disasteraidbackend.analytics.m13.entity.PredictiveModel;
import tn.esprit.disasteraidbackend.analytics.m13.service.DisasterAnalyticsService;
import tn.esprit.disasteraidbackend.common.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/analytics/disaster")
public class DisasterAnalyticsController {

    private final DisasterAnalyticsService disasterAnalyticsService;

    public DisasterAnalyticsController(DisasterAnalyticsService disasterAnalyticsService) {
        this.disasterAnalyticsService = disasterAnalyticsService;
    }

    @GetMapping("/report")
    public ApiResponse<DisasterAnalyticsReportDto> getGlobalReport() {
        return ApiResponse.ok(disasterAnalyticsService.getGlobalReport());
    }

    @GetMapping("/stats")
    public ApiResponse<List<DisasterStatistics>> getAllStats() {
        return ApiResponse.ok(disasterAnalyticsService.getAllStatistics());
    }

    @GetMapping("/stats/alert/{alertId}")
    public ApiResponse<List<DisasterStatistics>> getStatsByAlert(@PathVariable Integer alertId) {
        return ApiResponse.ok(disasterAnalyticsService.getStatisticsByAlert(alertId));
    }

    @GetMapping("/stats/alert/{alertId}/latest")
    public ApiResponse<DisasterStatistics> getLatestByAlert(@PathVariable Integer alertId) {
        return disasterAnalyticsService.getLatestStatisticByAlert(alertId)
                .map(ApiResponse::ok)
                .orElseGet(() -> ApiResponse.error("No statistics found for alertId=" + alertId));
    }

    @GetMapping("/audit-logs")
    public ApiResponse<List<AuditLog>> getLatestAuditLogs() {
        return ApiResponse.ok(disasterAnalyticsService.getLatestAuditLogs());
    }

    @GetMapping("/predictive-models")
    public ApiResponse<List<PredictiveModel>> getPredictiveModels(
            @RequestParam(defaultValue = "false") boolean activeOnly) {
        return ApiResponse.ok(disasterAnalyticsService.getPredictiveModels(activeOnly));
    }

    @PostMapping("/add_model")
    public ResponseEntity<ApiResponse<PredictiveModel>> addModel(@Valid @RequestBody PredictiveModelRequestDto request) {
        PredictiveModel created = disasterAnalyticsService.addPredictiveModel(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Predictive model created", created));
    }

    @PutMapping("/modify_model")
    public ResponseEntity<ApiResponse<PredictiveModel>> modifyModel(
            @RequestParam Integer modelId,
            @Valid @RequestBody PredictiveModelRequestDto request) {
        return disasterAnalyticsService.modifyPredictiveModel(modelId, request)
                .map(updated -> ResponseEntity.ok(ApiResponse.ok("Predictive model updated", updated)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Predictive model not found for modelId=" + modelId)));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteModel(@RequestParam Integer modelId) {
        if (!disasterAnalyticsService.deletePredictiveModel(modelId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Predictive model not found for modelId=" + modelId));
        }
        return ResponseEntity.ok(ApiResponse.ok("Predictive model deleted", null));
    }
}
