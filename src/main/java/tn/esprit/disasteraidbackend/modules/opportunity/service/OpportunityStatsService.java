package tn.esprit.disasteraidbackend.modules.opportunity.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.opportunity.dto.OpportunityStatsDto;
import tn.esprit.disasteraidbackend.modules.opportunity.repository.ApplicationRepository;
import tn.esprit.disasteraidbackend.modules.opportunity.repository.OpportunityRepository;

@Service
@RequiredArgsConstructor
public class OpportunityStatsService {

    private final OpportunityRepository opportunityRepository;
    private final ApplicationRepository applicationRepository;

    public OpportunityStatsDto getStats() {
        OpportunityStatsDto dto = new OpportunityStatsDto();
        dto.setTotalOpportunities(opportunityRepository.count());
        dto.setOpenOpportunities(opportunityRepository.countByStatus("OPEN"));
        dto.setTotalApplications(applicationRepository.count());
        dto.setPendingApplications(applicationRepository.countByStatus("PENDING"));
        dto.setAcceptedApplications(applicationRepository.countByStatus("ACCEPTED"));
        return dto;
    }
}
