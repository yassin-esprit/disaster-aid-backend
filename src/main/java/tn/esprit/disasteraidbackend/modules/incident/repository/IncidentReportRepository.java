package tn.esprit.disasteraidbackend.modules.incident.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.incident.entity.IncidentReport;

import java.util.List;

@Repository
public interface IncidentReportRepository extends JpaRepository<IncidentReport, Integer> {

    List<IncidentReport> findByStatus(String status);
    List<IncidentReport> findByReporterId(Integer reporterId);
    List<IncidentReport> findByDisasterType(String disasterType);
    List<IncidentReport> findBySeverity(String severity);
}