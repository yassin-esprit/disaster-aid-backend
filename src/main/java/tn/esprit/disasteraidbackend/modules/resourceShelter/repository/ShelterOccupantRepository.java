package tn.esprit.disasteraidbackend.modules.resourceShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.ShelterOccupant;

import java.util.List;

public interface ShelterOccupantRepository extends JpaRepository<ShelterOccupant, Integer> {
    List<ShelterOccupant> findByShelter_ShelterId(Integer shelterId);
    long deleteByShelter_ShelterId(Integer shelterId);
}