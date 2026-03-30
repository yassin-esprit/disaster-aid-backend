package tn.esprit.disasteraidbackend.modules.resourceShelter.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shelters")
public class Shelter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shelterId;

    @Column(nullable = false)
    private String name;

    @Column(name = "location_url", nullable = false, length = 1000)
    private String locationUrl;

    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false)
    private Integer capacity;

    @Column(nullable = false)
    private Integer currentOccupancy = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ShelterStatus status;

    private LocalDateTime openedAt;
    private LocalDateTime closedAt;

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShelterAmenity> amenities = new ArrayList<>();

    public void setAmenityStrings(List<String> values) {
        amenities.clear();
        if (values == null) return;
        for (String v : values) {
            if (v == null || v.isBlank()) continue;
            ShelterAmenity a = new ShelterAmenity();
            a.setShelter(this);
            a.setAmenity(v.trim());
            amenities.add(a);
        }
    }

    public List<String> getAmenityStrings() {
        List<String> out = new ArrayList<>();
        for (ShelterAmenity a : amenities) out.add(a.getAmenity());
        return out;
    }

    public Integer getShelterId() { return shelterId; }
    public void setShelterId(Integer shelterId) { this.shelterId = shelterId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocationUrl() { return locationUrl; }
    public void setLocationUrl(String locationUrl) { this.locationUrl = locationUrl; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Integer getCurrentOccupancy() { return currentOccupancy; }
    public void setCurrentOccupancy(Integer currentOccupancy) { this.currentOccupancy = currentOccupancy; }

    public User getManager() { return manager; }
    public void setManager(User manager) { this.manager = manager; }

    public ShelterStatus getStatus() { return status; }
    public void setStatus(ShelterStatus status) { this.status = status; }

    public LocalDateTime getOpenedAt() { return openedAt; }
    public void setOpenedAt(LocalDateTime openedAt) { this.openedAt = openedAt; }

    public LocalDateTime getClosedAt() { return closedAt; }
    public void setClosedAt(LocalDateTime closedAt) { this.closedAt = closedAt; }

    public List<ShelterAmenity> getAmenities() { return amenities; }
    public void setAmenities(List<ShelterAmenity> amenities) { this.amenities = amenities; }
}