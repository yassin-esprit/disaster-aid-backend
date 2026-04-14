package tn.esprit.disasteraidbackend.modules.incident.service;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.incident.entity.IncidentReport;
import tn.esprit.disasteraidbackend.modules.incident.repository.ReportEvidenceRepository;

@Service
public class CredibilityService {

    private final ReportEvidenceRepository evidenceRepository;

    public CredibilityService(ReportEvidenceRepository evidenceRepository) {
        this.evidenceRepository = evidenceRepository;
    }

    /**
     * Computes a credibility score 0.0 → 1.0 for a given report.
     *
     * Weights:
     *  - Has at least 1 evidence file  : 0.30
     *  - Description length > 100 chars: 0.15
     *  - Description length > 50 chars : 0.10 (partial)
     *  - Severity is HIGH or CRITICAL  : 0.10 (higher severity = more scrutiny = more weight)
     *  - Location is not blank         : 0.15
     *  - Upvote count > 0              : 0.10
     *  - Upvote count > 5              : 0.10 (bonus)
     *  - Reporter ID present           : 0.10
     */
    public double compute(IncidentReport report) {
        double score = 0.0;

        // Evidence presence
        int evidenceCount = evidenceRepository.findByIncidentReportId(report.getId()).size();
        if (evidenceCount >= 1) score += 0.30;

        // Description quality
        String desc = report.getDescription();
        if (desc != null) {
            if (desc.length() > 100) score += 0.15;
            else if (desc.length() > 50) score += 0.10;
        }

        // Severity weight
        String sev = report.getSeverity();
        if ("CRITICAL".equals(sev) || "HIGH".equals(sev)) score += 0.10;

        // Location precision
        if (report.getLocation() != null && !report.getLocation().isBlank()) score += 0.15;

        // Upvotes
        int upvotes = report.getUpvoteCount() != null ? report.getUpvoteCount() : 0;
        if (upvotes > 0) score += 0.10;
        if (upvotes > 5) score += 0.10;

        // Reporter identity
        if (report.getReporterId() != null) score += 0.10;

        return Math.min(score, 1.0);
    }
}