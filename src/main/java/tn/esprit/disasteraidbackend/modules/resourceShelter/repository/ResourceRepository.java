package tn.esprit.disasteraidbackend.modules.resourceShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.Resource;

public interface ResourceRepository extends JpaRepository<Resource, Integer> {
}