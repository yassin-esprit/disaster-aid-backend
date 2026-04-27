package tn.esprit.disasteraidbackend.analytics.m14.service;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.analytics.m14.dto.VolunteerAnalyticsReportDto;
import tn.esprit.disasteraidbackend.analytics.m14.entity.ChurnPrediction;
import tn.esprit.disasteraidbackend.analytics.m14.entity.OrganisationStats;
import tn.esprit.disasteraidbackend.analytics.m14.entity.VolunteerStatistics;
import tn.esprit.disasteraidbackend.analytics.m14.repository.ChurnPredictionRepository;
import tn.esprit.disasteraidbackend.analytics.m14.repository.OrganisationStatsRepository;
import tn.esprit.disasteraidbackend.analytics.m14.repository.VolunteerStatisticsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VolunteerAnalyticsService {

    private final VolunteerStatisticsRepository volunteerStatisticsRepository;
    private final OrganisationStatsRepository organisationStatsRepository;
    private final ChurnPredictionRepository churnPredictionRepository;

    public VolunteerAnalyticsService(VolunteerStatisticsRepository volunteerStatisticsRepository,
                                     OrganisationStatsRepository organisationStatsRepository,
                                     ChurnPredictionRepository churnPredictionRepository) {
        this.volunteerStatisticsRepository = volunteerStatisticsRepository;
        this.organisationStatsRepository = organisationStatsRepository;
        this.churnPredictionRepository = churnPredictionRepository;
    }

    public VolunteerAnalyticsReportDto getGlobalReport(double highRiskThreshold) {
        List<Object[]> results = volunteerStatisticsRepository.aggregateGlobalKpis();
        Object[] row = results.isEmpty() ? new Object[3] : results.get(0);
        return new VolunteerAnalyticsReportDto(
                toDouble(row[0]),
                toLong(row[1]),
                toDouble(row[2]),
                churnPredictionRepository
                        .findByPredictedChurnProbGreaterThanOrderByPredictedChurnProbDesc(highRiskThreshold)
                        .size(),
                organisationStatsRepository.count()
        );
    }

    public Optional<VolunteerStatistics> getLatestVolunteerStats(Integer volunteerId) {
        return volunteerStatisticsRepository.findTopByVolunteerIdOrderByComputedAtDesc(volunteerId);
    }

    public Optional<OrganisationStats> getLatestOrganisationStats(Integer orgId) {
        return organisationStatsRepository.findTopByOrgIdOrderByComputedAtDesc(orgId);
    }

    public List<ChurnPrediction> getHighRiskPredictions(Double threshold) {
        return churnPredictionRepository.findByPredictedChurnProbGreaterThanOrderByPredictedChurnProbDesc(threshold);
    }

    public Optional<ChurnPrediction> getLatestPredictionByVolunteer(Integer volunteerId) {
        return churnPredictionRepository.findTopByVolunteerIdOrderByPredictedAtDesc(volunteerId);
    }

    private long toLong(Object value) {
        return value == null ? 0L : ((Number) value).longValue();
    }

    private double toDouble(Object value) {
        return value == null ? 0D : ((Number) value).doubleValue();
    }
}
