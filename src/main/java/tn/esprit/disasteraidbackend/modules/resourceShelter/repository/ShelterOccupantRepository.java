package tn.esprit.disasteraidbackend.modules.resourceShelter.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.ShelterOccupant;

public interface ShelterOccupantRepository extends JpaRepository<ShelterOccupant, Integer> {
    List<ShelterOccupant> findByShelter_ShelterId(Integer shelterId);
}