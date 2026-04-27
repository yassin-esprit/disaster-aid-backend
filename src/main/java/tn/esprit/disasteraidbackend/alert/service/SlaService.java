package tn.esprit.disasteraidbackend.alert.service;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.alert.dto.SlaSummaryDto;
import tn.esprit.disasteraidbackend.alert.entity.SlaMetric;
import tn.esprit.disasteraidbackend.alert.repository.SlaMetricRepository;

import java.util.List;

@Service
public class SlaService {

    private final SlaMetricRepository repo;

    public SlaService(SlaMetricRepository repo) {
        this.repo = repo;
    }

    public List<SlaMetric> getAll() {
        return repo.findAll();
    }

    public List<SlaMetric> getByAlert(Integer alertId) {
        return repo.findByAlertIdOrderByIncidentAtDesc(alertId);
    }

    public SlaSummaryDto getSummary() {
        List<Object[]> results = repo.aggregateSlaKpis();
        Object[] row = results.isEmpty() ? new Object[]{0.0, 0.0, 0.0, 0L} : results.get(0);

        long withinSla = repo.countBySlaStatus(SlaMetric.SlaStatus.WITHIN_SLA);
        long atRisk = repo.countBySlaStatus(SlaMetric.SlaStatus.AT_RISK);
        long breached = repo.countBySlaStatus(SlaMetric.SlaStatus.BREACHED);

        return new SlaSummaryDto(
                ((Number) row[0]).doubleValue(),
                ((Number) row[1]).doubleValue(),
                ((Number) row[2]).doubleValue(),
                ((Number) row[3]).longValue(),
                withinSla, atRisk, breached
        );
    }

    public SlaMetric create(SlaMetric metric) {
        return repo.save(metric);
    }
}
