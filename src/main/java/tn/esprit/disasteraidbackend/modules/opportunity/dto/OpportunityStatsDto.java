package tn.esprit.disasteraidbackend.modules.opportunity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpportunityStatsDto {
    private long totalOpportunities;
    private long openOpportunities;
    private long totalApplications;
    private long pendingApplications;
    private long acceptedApplications;
}
