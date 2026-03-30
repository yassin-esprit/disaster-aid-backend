package tn.esprit.disasteraidbackend.modules.resourceShelter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shelter_occupants")
public class ShelterOccupant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer recordId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shelter_id", nullable = false)
    private Shelter shelter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime checkInAt;

    private LocalDateTime checkOutAt;

    @OneToMany(mappedBy = "occupant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShelterOccupantNeed> needsFlag = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        if (checkInAt == null) checkInAt = LocalDateTime.now();
    }

    // helpers
    public void setNeedStrings(List<String> values) {
        needsFlag.clear();
        if (values == null) return;
        for (String v : values) {
            if (v == null || v.isBlank()) continue;
            ShelterOccupantNeed n = new ShelterOccupantNeed();
            n.setOccupant(this);
            n.setNeed(v.trim());
            needsFlag.add(n);
        }
    }

    public List<String> getNeedStrings() {
        List<String> out = new ArrayList<>();
        for (ShelterOccupantNeed n : needsFlag) out.add(n.getNeed());
        return out;
    }

    // getters/setters
    public Integer getRecordId() { return recordId; }
    public void setRecordId(Integer recordId) { this.recordId = recordId; }

    public Shelter getShelter() { return shelter; }
    public void setShelter(Shelter shelter) { this.shelter = shelter; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getCheckInAt() { return checkInAt; }
    public void setCheckInAt(LocalDateTime checkInAt) { this.checkInAt = checkInAt; }

    public LocalDateTime getCheckOutAt() { return checkOutAt; }
    public void setCheckOutAt(LocalDateTime checkOutAt) { this.checkOutAt = checkOutAt; }

    public List<ShelterOccupantNeed> getNeedsFlag() { return needsFlag; }
    public void setNeedsFlag(List<ShelterOccupantNeed> needsFlag) { this.needsFlag = needsFlag; }
}