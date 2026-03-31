package tn.esprit.disasteraidbackend.analytics.m14.dto;

public class VolunteerAnalyticsReportDto {

    private final double totalApprovedHours;
    private final long totalOpportunities;
    private final double avgChurnProbability;
    private final long highRiskVolunteers;
    private final long organisationsTracked;

    public VolunteerAnalyticsReportDto(double totalApprovedHours,
                                       long totalOpportunities,
                                       double avgChurnProbability,
                                       long highRiskVolunteers,
                                       long organisationsTracked) {
        this.totalApprovedHours = totalApprovedHours;
        this.totalOpportunities = totalOpportunities;
        this.avgChurnProbability = avgChurnProbability;
        this.highRiskVolunteers = highRiskVolunteers;
        this.organisationsTracked = organisationsTracked;
    }

    public double getTotalApprovedHours() {
        return totalApprovedHours;
    }

    public long getTotalOpportunities() {
        return totalOpportunities;
    }

    public double getAvgChurnProbability() {
        return avgChurnProbability;
    }

    public long getHighRiskVolunteers() {
        return highRiskVolunteers;
    }

    public long getOrganisationsTracked() {
        return organisationsTracked;
    }
}
