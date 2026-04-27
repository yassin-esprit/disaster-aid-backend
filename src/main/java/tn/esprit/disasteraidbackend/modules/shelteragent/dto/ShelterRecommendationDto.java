package tn.esprit.disasteraidbackend.modules.shelteragent.dto;

import tn.esprit.disasteraidbackend.modules.shelteragent.entity.ShelterStatus;
import tn.esprit.disasteraidbackend.modules.shelteragent.entity.ShelterType;

import java.util.ArrayList;
import java.util.List;

public class ShelterRecommendationDto {
    private Integer shelteragentId;
    private String name;
    private int capacity;
    private int occupancy;
    private String address;
    private String locationUrl;
    private double distanceKm;
    private int remaining;
    private ShelterType type;
    private ShelterStatus status;
    private double score;
    private List<String> reasons = new ArrayList<>();

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
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

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }
}