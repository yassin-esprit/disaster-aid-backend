package tn.esprit.disasteraidbackend.alert.dto;

public class MapSummaryDto {

    private final long activeAlerts;
    private final long monitoringAlerts;
    private final long resolvedAlerts;
    private final long totalResponders;
    private final long totalHelpRequests;
    private final long resolvedRequests;

    public MapSummaryDto(long activeAlerts, long monitoringAlerts, long resolvedAlerts,
                         long totalResponders, long totalHelpRequests, long resolvedRequests) {
        this.activeAlerts = activeAlerts;
        this.monitoringAlerts = monitoringAlerts;
        this.resolvedAlerts = resolvedAlerts;
        this.totalResponders = totalResponders;
        this.totalHelpRequests = totalHelpRequests;
        this.resolvedRequests = resolvedRequests;
    }

    public long getActiveAlerts() { return activeAlerts; }
    public long getMonitoringAlerts() { return monitoringAlerts; }
    public long getResolvedAlerts() { return resolvedAlerts; }
    public long getTotalResponders() { return totalResponders; }
    public long getTotalHelpRequests() { return totalHelpRequests; }
    public long getResolvedRequests() { return resolvedRequests; }
}
