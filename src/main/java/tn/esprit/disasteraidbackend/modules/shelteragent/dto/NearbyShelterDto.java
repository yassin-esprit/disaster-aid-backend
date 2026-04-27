package tn.esprit.disasteraidbackend.modules.shelteragent.dto;

import tn.esprit.disasteraidbackend.modules.shelteragent.entity.ShelterStatus;
import tn.esprit.disasteraidbackend.modules.shelteragent.entity.ShelterType;

public class NearbyShelterDto {
    private Integer shelteragentId;
    private String name;
    private String address;
    private String city;
    private ShelterType type;
    private ShelterStatus status;
    private int capacity;
    private int occupancy;
    private int remaining;
    private boolean hasAccessibility;
    private boolean acceptsPets;
    private String phone;
    private String locationUrl;
    private double distanceKm;
    private Double avgRating;
    private long reviewCount;

    public Integer getShelteragentId() {
        return shelteragentId;
    }

    public void setShelteragentId(Integer shelteragentId) {
        this.shelteragentId = shelteragentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ShelterType getType() {
        return type;
    }

    public void setType(ShelterType type) {
        this.type = type;
    }

    public ShelterStatus getStatus() {
        return status;
    }

    public void setStatus(ShelterStatus status) {
        this.status = status;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public boolean isHasAccessibility() {
        return hasAccessibility;
    }

    public void setHasAccessibility(boolean hasAccessibility) {
        this.hasAccessibility = hasAccessibility;
    }

    public boolean isAcceptsPets() {
        return acceptsPets;
    }

    public void setAcceptsPets(boolean acceptsPets) {
        this.acceptsPets = acceptsPets;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocationUrl() {
        return locationUrl;
    }

    public void setLocationUrl(String locationUrl) {
        this.locationUrl = locationUrl;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(long reviewCount) {
        this.reviewCount = reviewCount;
    }
}