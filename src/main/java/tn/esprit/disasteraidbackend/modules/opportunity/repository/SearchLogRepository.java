package tn.esprit.disasteraidbackend.modules.opportunity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.opportunity.entity.SearchLog;

import java.util.List;

@Repository
public interface SearchLogRepository extends JpaRepository<SearchLog, Integer> {
    List<SearchLog> findByVolunteerId(Integer volunteerId);
    List<SearchLog> findByOpportunityId(Integer opportunityId);
    List<SearchLog> findByEventType(String eventType);
}
