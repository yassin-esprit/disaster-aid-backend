package tn.esprit.disasteraidbackend.modules.coordination.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.coordination.entity.HelpRequest;

import java.util.List;

@Repository
public interface HelpRequestRepository extends JpaRepository<HelpRequest, Integer> {
    List<HelpRequest> findByStatus(String status);
    List<HelpRequest> findByType(String type);
    List<HelpRequest> findByReporterId(Integer reporterId);
}