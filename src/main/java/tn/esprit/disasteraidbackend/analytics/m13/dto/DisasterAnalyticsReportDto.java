package tn.esprit.disasteraidbackend.analytics.m13.dto;

public class DisasterAnalyticsReportDto {

    private final long totalReports;
    private final long verifiedReports;
    private final long totalHelpRequests;
    private final long resolvedRequests;
    private final double avgResponseTimeMin;
    private final long auditEntries;
    private final long activeModels;

    public DisasterAnalyticsReportDto(long totalReports,
                                      long verifiedReports,
                                      long totalHelpRequests,
                                      long resolvedRequests,
                                      double avgResponseTimeMin,
                                      long auditEntries,
                                      long activeModels) {
        this.totalReports = totalReports;
        this.verifiedReports = verifiedReports;
        this.totalHelpRequests = totalHelpRequests;
        this.resolvedRequests = resolvedRequests;
        this.avgResponseTimeMin = avgResponseTimeMin;
        this.auditEntries = auditEntries;
        this.activeModels = activeModels;
    }

    public long getTotalReports() {
        return totalReports;
    }

    public long getVerifiedReports() {
        return verifiedReports;
    }

    public long getTotalHelpRequests() {
        return totalHelpRequests;
    }

    public long getResolvedRequests() {
        return resolvedRequests;
    }

    public double getAvgResponseTimeMin() {
        return avgResponseTimeMin;
    }

    public long getAuditEntries() {
        return auditEntries;
    }

    public long getActiveModels() {
        return activeModels;
    }
}
