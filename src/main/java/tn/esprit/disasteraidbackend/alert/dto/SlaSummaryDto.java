package tn.esprit.disasteraidbackend.alert.dto;

public class SlaSummaryDto {
    private double avgAcknowledgeMin;
    private double avgResolveMin;
    private double avgTotalMin;
    private long totalIncidents;
    private long withinSla;
    private long atRisk;
    private long breached;

    public SlaSummaryDto(double avgAcknowledgeMin, double avgResolveMin, double avgTotalMin,
                         long totalIncidents, long withinSla, long atRisk, long breached) {
        this.avgAcknowledgeMin = avgAcknowledgeMin;
        this.avgResolveMin = avgResolveMin;
        this.avgTotalMin = avgTotalMin;
        this.totalIncidents = totalIncidents;
        this.withinSla = withinSla;
        this.atRisk = atRisk;
        this.breached = breached;
    }

    public double getAvgAcknowledgeMin() { return avgAcknowledgeMin; }
    public double getAvgResolveMin() { return avgResolveMin; }
    public double getAvgTotalMin() { return avgTotalMin; }
    public long getTotalIncidents() { return totalIncidents; }
    public long getWithinSla() { return withinSla; }
    public long getAtRisk() { return atRisk; }
    public long getBreached() { return breached; }
}
