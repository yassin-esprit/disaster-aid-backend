package tn.esprit.disasteraidbackend.modules.coordination.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.coordination.entity.HelpRequest;
import tn.esprit.disasteraidbackend.modules.coordination.repository.TaskAssignmentRepository;

@Service
@RequiredArgsConstructor
public class MlMatchingService {

    private final TaskAssignmentRepository assignmentRepository;

    /**
     * Computes a match score 0.0 → 1.0 between a help request and a responder.
     *
     * Weights:
     *  - Urgency level (higher = higher score weight)    : 0.30
     *  - Request type matching (simplified)              : 0.20
     *  - Responder past completions (trust proxy)        : 0.30
     *  - Responder not overloaded (< 3 active tasks)     : 0.20
     */
    public double computeScore(HelpRequest request, Integer responderId) {
        double score = 0.0;

        // Urgency factor (urgency 1-5, normalized)
        int urgency = request.getUrgency() != null ? request.getUrgency() : 1;
        score += (urgency / 5.0) * 0.30;

        // Past completions for this responder
        long completions = assignmentRepository.findByResponderId(responderId)
                .stream()
                .filter(a -> "COMPLETED".equals(a.getStatus()))
                .count();
        double completionScore = Math.min(completions / 10.0, 1.0); // cap at 10 completions
        score += completionScore * 0.30;

        // Workload — penalize if responder has too many active assignments
        long activeCount = assignmentRepository.findByResponderId(responderId)
                .stream()
                .filter(a -> "ASSIGNED".equals(a.getStatus()) || "ACCEPTED".equals(a.getStatus()))
                .count();
        if (activeCount < 3) score += 0.20;
        else if (activeCount < 5) score += 0.10;

        // Type match bonus (RESCUE/MEDICAL = high urgency types get bonus)
        String type = request.getType();
        if ("RESCUE".equals(type) || "MEDICAL".equals(type)) score += 0.20;
        else score += 0.10;

        return Math.min(Math.round(score * 100.0) / 100.0, 1.0);
    }
}