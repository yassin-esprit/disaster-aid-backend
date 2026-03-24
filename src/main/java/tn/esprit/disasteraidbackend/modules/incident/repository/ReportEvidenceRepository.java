package tn.esprit.disasteraidbackend.modules.incident.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.incident.entity.ReportEvidence;

import java.util.List;

@Repository
public interface ReportEvidenceRepository extends JpaRepository<ReportEvidence, Integer> {
    List<ReportEvidence> findByIncidentReportId(Integer incidentReportId);
}