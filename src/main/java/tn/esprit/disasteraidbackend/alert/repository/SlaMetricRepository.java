package tn.esprit.disasteraidbackend.alert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.disasteraidbackend.alert.entity.SlaMetric;

import java.util.List;

public interface SlaMetricRepository extends JpaRepository<SlaMetric, Integer> {

    List<SlaMetric> findByAlertIdOrderByIncidentAtDesc(Integer alertId);

    List<SlaMetric> findBySlaStatusOrderByIncidentAtDesc(SlaMetric.SlaStatus status);

    long countBySlaStatus(SlaMetric.SlaStatus status);

    @Query("""
        select coalesce(avg(s.timeToAcknowledgeMin), 0),
               coalesce(avg(s.timeToResolveMin), 0),
               coalesce(avg(s.totalResolutionMin), 0),
               count(s)
        from SlaMetric s
        """)
    List<Object[]> aggregateSlaKpis();
}
