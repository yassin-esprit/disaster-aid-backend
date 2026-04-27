package tn.esprit.disasteraidbackend.modules.resourceShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.Shelter;

public interface ShelterRepository extends JpaRepository<Shelter, Integer> {
}