package tn.esprit.disasteraidbackend.analytics.m14.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.disasteraidbackend.analytics.m14.dto.VolunteerAnalyticsReportDto;
import tn.esprit.disasteraidbackend.analytics.m14.entity.ChurnPrediction;
import tn.esprit.disasteraidbackend.analytics.m14.entity.OrganisationStats;
import tn.esprit.disasteraidbackend.analytics.m14.entity.VolunteerStatistics;
import tn.esprit.disasteraidbackend.analytics.m14.service.VolunteerAnalyticsService;
import tn.esprit.disasteraidbackend.common.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/analytics/volunteer")
public class VolunteerAnalyticsController {

    private final VolunteerAnalyticsService volunteerAnalyticsService;

    public VolunteerAnalyticsController(VolunteerAnalyticsService volunteerAnalyticsService) {
        this.volunteerAnalyticsService = volunteerAnalyticsService;
    }

    @GetMapping("/report")
    public ApiResponse<VolunteerAnalyticsReportDto> getGlobalReport(
            @RequestParam(defaultValue = "0.70") double highRiskThreshold) {
        return ApiResponse.ok(volunteerAnalyticsService.getGlobalReport(highRiskThreshold));
    }

    @GetMapping("/stats/{volunteerId}")
    public ApiResponse<VolunteerStatistics> getLatestVolunteerStats(@PathVariable Integer volunteerId) {
        return volunteerAnalyticsService.getLatestVolunteerStats(volunteerId)
                .map(ApiResponse::ok)
                .orElseGet(() -> ApiResponse.error("No volunteer statistics found for volunteerId=" + volunteerId));
    }

    @GetMapping("/organisations/{orgId}")
    public ApiResponse<OrganisationStats> getLatestOrganisationStats(@PathVariable Integer orgId) {
        return volunteerAnalyticsService.getLatestOrganisationStats(orgId)
                .map(ApiResponse::ok)
                .orElseGet(() -> ApiResponse.error("No organisation statistics found for orgId=" + orgId));
    }

    @GetMapping("/churn/high-risk")
    public ApiResponse<List<ChurnPrediction>> getHighRiskPredictions(
            @RequestParam(defaultValue = "0.70") double threshold) {
        return ApiResponse.ok(volunteerAnalyticsService.getHighRiskPredictions(threshold));
    }

    @GetMapping("/churn/volunteer/{volunteerId}")
    public ApiResponse<ChurnPrediction> getLatestPredictionByVolunteer(@PathVariable Integer volunteerId) {
        return volunteerAnalyticsService.getLatestPredictionByVolunteer(volunteerId)
                .map(ApiResponse::ok)
                .orElseGet(() -> ApiResponse.error("No churn prediction found for volunteerId=" + volunteerId));
    }
}
