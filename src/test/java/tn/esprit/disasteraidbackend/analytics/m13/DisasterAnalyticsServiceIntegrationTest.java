package tn.esprit.disasteraidbackend.analytics.m13;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.disasteraidbackend.analytics.m13.dto.DisasterAnalyticsReportDto;
import tn.esprit.disasteraidbackend.analytics.m13.repository.AuditLogRepository;
import tn.esprit.disasteraidbackend.analytics.m13.repository.DisasterStatisticsRepository;
import tn.esprit.disasteraidbackend.analytics.m13.repository.PredictiveModelRepository;
import tn.esprit.disasteraidbackend.analytics.m13.service.DisasterAnalyticsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DisasterAnalyticsServiceIntegrationTest {

    @Mock
    private DisasterStatisticsRepository disasterStatisticsRepository;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private PredictiveModelRepository predictiveModelRepository;

    @InjectMocks
    private DisasterAnalyticsService disasterAnalyticsService;

    @Test
    void shouldBuildGlobalReportFromStoredRecords() {
        when(disasterStatisticsRepository.aggregateGlobalKpis())
                .thenReturn(new Object[]{8L, 5L, 4L, 3L, 18.5D});
        when(auditLogRepository.count()).thenReturn(1L);
        when(predictiveModelRepository.countByIsActiveTrue()).thenReturn(1L);

        DisasterAnalyticsReportDto report = disasterAnalyticsService.getGlobalReport();

        assertEquals(8L, report.getTotalReports());
        assertEquals(5L, report.getVerifiedReports());
        assertEquals(4L, report.getTotalHelpRequests());
        assertEquals(3L, report.getResolvedRequests());
        assertEquals(18.5D, report.getAvgResponseTimeMin());
        assertEquals(1L, report.getAuditEntries());
        assertEquals(1L, report.getActiveModels());
    }
}
