package tn.esprit.disasteraidbackend.modules.shelteragent.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity(name = "ShelterAgent")
@Table(name = "shelteragent")
public class ShelterAgent {

    @Id
    @Column(name = "shelter_agent_id")
    private Integer shelteragentId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false, length = 120)
    private String city;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "shelter_type", nullable = false, length = 20)
    private ShelterType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ShelterStatus status;

    @Column(nullable = false)
    private int capacity;

    @Column(name = "current_occupancy", nullable = false)
    private int currentOccupancy;

    @Column(name = "has_accessibility", nullable = false)
    private boolean hasAccessibility;

    @Column(name = "accepts_pets", nullable = false)
    private boolean acceptsPets;

    private String phone;

    @Column(name = "location_url")
    private String locationUrl;

    @Column(name = "opened_at")
    private LocalDateTime openedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    public void setCurrentOccupancy(int currentOccupancy) {
        this.currentOccupancy = currentOccupancy;
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

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(LocalDateTime openedAt) {
        this.openedAt = openedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
