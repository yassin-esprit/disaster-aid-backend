package tn.esprit.disasteraidbackend.modules.opportunity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.opportunity.entity.Application;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    List<Application> findByOpportunityId(Integer opportunityId);
    List<Application> findByVolunteerId(Integer volunteerId);
    List<Application> findByStatus(String status);
    Optional<Application> findByOpportunityIdAndVolunteerId(Integer opportunityId, Integer volunteerId);
    long countByStatus(String status);
}
