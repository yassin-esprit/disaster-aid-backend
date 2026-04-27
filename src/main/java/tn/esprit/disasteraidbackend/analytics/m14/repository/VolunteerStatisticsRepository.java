package tn.esprit.disasteraidbackend.analytics.m14.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.disasteraidbackend.analytics.m14.entity.VolunteerStatistics;

import java.util.List;
import java.util.Optional;

public interface VolunteerStatisticsRepository extends JpaRepository<VolunteerStatistics, Integer> {

    Optional<VolunteerStatistics> findTopByVolunteerIdOrderByComputedAtDesc(Integer volunteerId);

    @Query("""
            select coalesce(sum(vs.totalApprovedHours), 0),
                   coalesce(sum(vs.totalOpportunities), 0),
                   coalesce(avg(vs.churnProbability), 0)
            from VolunteerStatistics vs
            """)
    List<Object[]> aggregateGlobalKpis();
}

