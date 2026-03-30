package tn.esprit.disasteraidbackend.modules.resourceShelter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}