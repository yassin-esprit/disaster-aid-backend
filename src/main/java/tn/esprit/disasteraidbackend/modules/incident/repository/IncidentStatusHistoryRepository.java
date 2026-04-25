package tn.esprit.disasteraidbackend.modules.incident.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.incident.entity.IncidentStatusHistory;

import java.util.List;

@Repository
public interface IncidentStatusHistoryRepository
        extends JpaRepository<IncidentStatusHistory, Integer> {

    List<IncidentStatusHistory> findByIncidentReportIdOrderByChangedAtAsc(Integer incidentReportId);
}