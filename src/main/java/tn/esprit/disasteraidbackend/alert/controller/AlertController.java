package tn.esprit.disasteraidbackend.alert.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.disasteraidbackend.alert.dto.AlertRequestDto;
import tn.esprit.disasteraidbackend.alert.dto.AlertWithStatsDto;
import tn.esprit.disasteraidbackend.alert.dto.MapSummaryDto;
import tn.esprit.disasteraidbackend.alert.entity.Alert;
import tn.esprit.disasteraidbackend.alert.service.AlertService;
import tn.esprit.disasteraidbackend.common.ApiResponse;

import java.util.List;

@RestController("alertApiController")
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public ApiResponse<List<Alert>> getAllAlerts() {
        return ApiResponse.ok(alertService.getAllAlerts());
    }

    @GetMapping("/active")
    public ApiResponse<List<Alert>> getActiveAlerts() {
        return ApiResponse.ok(alertService.getActiveAlerts());
    }

    @GetMapping("/{alertId}")
    public ApiResponse<Alert> getAlertById(@PathVariable Integer alertId) {
        return alertService.getAlertById(alertId)
                .map(ApiResponse::ok)
                .orElseGet(() -> ApiResponse.error("Alert not found for id=" + alertId));
    }

    @GetMapping("/with-stats")
    public ApiResponse<List<AlertWithStatsDto>> getAlertsWithStats() {
        return ApiResponse.ok(alertService.getAlertsWithStats());
    }

    @GetMapping("/{alertId}/with-stats")
    public ApiResponse<AlertWithStatsDto> getAlertWithStats(@PathVariable Integer alertId) {
        return alertService.getAlertWithStats(alertId)
                .map(ApiResponse::ok)
                .orElseGet(() -> ApiResponse.error("Alert not found for id=" + alertId));
    }

    @GetMapping("/map-summary")
    public ApiResponse<MapSummaryDto> getMapSummary() {
        return ApiResponse.ok(alertService.getMapSummary());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Alert>> createAlert(
            @Valid @RequestBody AlertRequestDto request) {
        Alert created = alertService.createAlert(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Alert created", created));
    }

    @PutMapping("/{alertId}")
    public ResponseEntity<ApiResponse<Alert>> updateAlert(
            @PathVariable Integer alertId,
            @Valid @RequestBody AlertRequestDto request) {
        return alertService.updateAlert(alertId, request)
                .map(updated -> ResponseEntity.ok(ApiResponse.ok("Alert updated", updated)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Alert not found for id=" + alertId)));
    }

    @DeleteMapping("/{alertId}")
    public ResponseEntity<ApiResponse<Void>> deleteAlert(@PathVariable Integer alertId) {
        if (!alertService.deleteAlert(alertId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Alert not found for id=" + alertId));
        }
        return ResponseEntity.ok(ApiResponse.ok("Alert deleted", null));
    }
}
