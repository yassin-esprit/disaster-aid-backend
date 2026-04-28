package tn.esprit.disasteraidbackend.modules.opportunity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.opportunity.entity.Opportunity;

import java.util.List;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Integer> {
    List<Opportunity> findByStatus(String status);
    List<Opportunity> findByOrgId(Integer orgId);
    List<Opportunity> findByCategory(String category);
    List<Opportunity> findByIsRemoteTrue();
    long countByStatus(String status);
}
