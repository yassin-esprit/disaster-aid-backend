package tn.esprit.disasteraidbackend.analytics.m14;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.disasteraidbackend.analytics.m14.dto.VolunteerAnalyticsReportDto;
import tn.esprit.disasteraidbackend.analytics.m14.entity.ChurnPrediction;
import tn.esprit.disasteraidbackend.analytics.m14.repository.ChurnPredictionRepository;
import tn.esprit.disasteraidbackend.analytics.m14.repository.OrganisationStatsRepository;
import tn.esprit.disasteraidbackend.analytics.m14.repository.VolunteerStatisticsRepository;
import tn.esprit.disasteraidbackend.analytics.m14.service.VolunteerAnalyticsService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VolunteerAnalyticsServiceIntegrationTest {

    @Mock
    private VolunteerStatisticsRepository volunteerStatisticsRepository;

    @Mock
    private OrganisationStatsRepository organisationStatsRepository;

    @Mock
    private ChurnPredictionRepository churnPredictionRepository;

    @InjectMocks
    private VolunteerAnalyticsService volunteerAnalyticsService;

    @Test
    void shouldBuildVolunteerReportAndDetectHighRisk() {
        when(volunteerStatisticsRepository.aggregateGlobalKpis())
                .thenReturn(new Object[]{25.0D, 6L, 0.62D});
        when(churnPredictionRepository.findByPredictedChurnProbGreaterThanOrderByPredictedChurnProbDesc(0.70D))
                .thenReturn(List.of(new ChurnPrediction()));
        when(organisationStatsRepository.count()).thenReturn(1L);

        VolunteerAnalyticsReportDto report = volunteerAnalyticsService.getGlobalReport(0.70D);

        assertEquals(25.0D, report.getTotalApprovedHours());
        assertEquals(6L, report.getTotalOpportunities());
        assertEquals(0.62D, report.getAvgChurnProbability());
        assertEquals(1L, report.getHighRiskVolunteers());
        assertEquals(1L, report.getOrganisationsTracked());
    }
}
