package tn.esprit.disasteraidbackend.modules.resourceShelter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "shelter_occupant_needs")
public class ShelterOccupantNeed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "record_id", nullable = false)
    private ShelterOccupant occupant;

    @Column(nullable = false, length = 100)
    private String need;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ShelterOccupant getOccupant() { return occupant; }
    public void setOccupant(ShelterOccupant occupant) { this.occupant = occupant; }

    public String getNeed() { return need; }
    public void setNeed(String need) { this.need = need; }
}