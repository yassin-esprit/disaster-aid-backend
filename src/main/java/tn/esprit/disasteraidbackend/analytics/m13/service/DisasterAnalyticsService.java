package tn.esprit.disasteraidbackend.analytics.m13.service;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.analytics.m13.dto.DisasterAnalyticsReportDto;
import tn.esprit.disasteraidbackend.analytics.m13.entity.AuditLog;
import tn.esprit.disasteraidbackend.analytics.m13.entity.DisasterStatistics;
import tn.esprit.disasteraidbackend.analytics.m13.entity.PredictiveModel;
import tn.esprit.disasteraidbackend.analytics.m13.repository.AuditLogRepository;
import tn.esprit.disasteraidbackend.analytics.m13.repository.DisasterStatisticsRepository;
import tn.esprit.disasteraidbackend.analytics.m13.repository.PredictiveModelRepository;

import java.util.List;
import java.util.Optional;

@Service
public class DisasterAnalyticsService {

    private final DisasterStatisticsRepository disasterStatisticsRepository;
    private final AuditLogRepository auditLogRepository;
    private final PredictiveModelRepository predictiveModelRepository;

    public DisasterAnalyticsService(DisasterStatisticsRepository disasterStatisticsRepository,
                                   AuditLogRepository auditLogRepository,
                                   PredictiveModelRepository predictiveModelRepository) {
        this.disasterStatisticsRepository = disasterStatisticsRepository;
        this.auditLogRepository = auditLogRepository;
        this.predictiveModelRepository = predictiveModelRepository;
    }

    public DisasterAnalyticsReportDto getGlobalReport() {
        Object[] row = disasterStatisticsRepository.aggregateGlobalKpis();
        return new DisasterAnalyticsReportDto(
                toLong(row[0]),
                toLong(row[1]),
                toLong(row[2]),
                toLong(row[3]),
                toDouble(row[4]),
                auditLogRepository.count(),
                predictiveModelRepository.countByIsActiveTrue()
        );
    }

    public List<DisasterStatistics> getAllStatistics() {
        return disasterStatisticsRepository.findAll();
    }

    public List<DisasterStatistics> getStatisticsByAlert(Integer alertId) {
        return disasterStatisticsRepository.findByAlertIdOrderByComputedAtDesc(alertId);
    }

    public Optional<DisasterStatistics> getLatestStatisticByAlert(Integer alertId) {
        return disasterStatisticsRepository.findTopByAlertIdOrderByComputedAtDesc(alertId);
    }

    public List<AuditLog> getLatestAuditLogs() {
        return auditLogRepository.findTop100ByOrderByTimestampDesc();
    }

    public List<PredictiveModel> getPredictiveModels(boolean activeOnly) {
        if (activeOnly) {
            return predictiveModelRepository.findByIsActiveTrueOrderByTrainedAtDesc();
        }
        return predictiveModelRepository.findAll();
    }

    private long toLong(Object value) {
        return value == null ? 0L : ((Number) value).longValue();
    }

    private double toDouble(Object value) {
        return value == null ? 0D : ((Number) value).doubleValue();
    }
}
