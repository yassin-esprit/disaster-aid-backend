package tn.esprit.disasteraidbackend.modules.shelteragent.dto;

public class ShelterRecommendationRequest {
    private double lat;
    private double lng;
    private Double maxDistanceKm;
    private Boolean needsMedical;
    private Boolean needsAccessible;
    private Boolean hasPets;
    private Integer peopleCount;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Double getMaxDistanceKm() {
        return maxDistanceKm;
    }

    public void setMaxDistanceKm(Double maxDistanceKm) {
        this.maxDistanceKm = maxDistanceKm;
    }

    public Boolean getNeedsMedical() {
        return needsMedical;
    }

    public void setNeedsMedical(Boolean needsMedical) {
        this.needsMedical = needsMedical;
    }

    public Boolean getNeedsAccessible() {
        return needsAccessible;
    }

    public void setNeedsAccessible(Boolean needsAccessible) {
        this.needsAccessible = needsAccessible;
    }

    public Boolean getHasPets() {
        return hasPets;
    }

    public void setHasPets(Boolean hasPets) {
        this.hasPets = hasPets;
    }

    public Integer getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(Integer peopleCount) {
        this.peopleCount = peopleCount;
    }
}