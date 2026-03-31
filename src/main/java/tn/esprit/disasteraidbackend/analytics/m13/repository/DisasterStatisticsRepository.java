package tn.esprit.disasteraidbackend.analytics.m13.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.disasteraidbackend.analytics.m13.entity.DisasterStatistics;

import java.util.List;
import java.util.Optional;

public interface DisasterStatisticsRepository extends JpaRepository<DisasterStatistics, Integer> {

    List<DisasterStatistics> findByAlertIdOrderByComputedAtDesc(Integer alertId);

    Optional<DisasterStatistics> findTopByAlertIdOrderByComputedAtDesc(Integer alertId);

    @Query("""
            select coalesce(sum(ds.totalReports), 0),
                   coalesce(sum(ds.verifiedReports), 0),
                   coalesce(sum(ds.totalHelpRequests), 0),
                   coalesce(sum(ds.resolvedRequests), 0),
                   coalesce(avg(ds.avgResponseTimeMin), 0)
            from DisasterStatistics ds
            """)
    Object[] aggregateGlobalKpis();
}

