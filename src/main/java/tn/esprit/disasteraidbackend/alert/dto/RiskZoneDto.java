package tn.esprit.disasteraidbackend.alert.dto;

import java.util.List;

public class RiskZoneDto {
    private String region;
    private String country;
    private int totalAlerts;
    private int activeAlerts;
    private int resolvedAlerts;
    private double avgLatitude;
    private double avgLongitude;
    private String riskLevel; // CRITICAL, HIGH, MEDIUM, LOW
    private List<String> disasterTypes;

    public RiskZoneDto(String region, String country, int totalAlerts, int activeAlerts, int resolvedAlerts,
                       double avgLatitude, double avgLongitude, String riskLevel, List<String> disasterTypes) {
        this.region = region;
        this.country = country;
        this.totalAlerts = totalAlerts;
        this.activeAlerts = activeAlerts;
        this.resolvedAlerts = resolvedAlerts;
        this.avgLatitude = avgLatitude;
        this.avgLongitude = avgLongitude;
        this.riskLevel = riskLevel;
        this.disasterTypes = disasterTypes;
    }

    public String getRegion() { return region; }
    public String getCountry() { return country; }
    public int getTotalAlerts() { return totalAlerts; }
    public int getActiveAlerts() { return activeAlerts; }
    public int getResolvedAlerts() { return resolvedAlerts; }
    public double getAvgLatitude() { return avgLatitude; }
    public double getAvgLongitude() { return avgLongitude; }
    public String getRiskLevel() { return riskLevel; }
    public List<String> getDisasterTypes() { return disasterTypes; }
}
