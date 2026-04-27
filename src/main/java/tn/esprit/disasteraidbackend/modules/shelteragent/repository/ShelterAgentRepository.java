package tn.esprit.disasteraidbackend.modules.shelteragent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.shelteragent.entity.ShelterAgent;
import tn.esprit.disasteraidbackend.modules.shelteragent.entity.ShelterStatus;

import java.util.List;

public interface ShelterAgentRepository extends JpaRepository<ShelterAgent, Integer> {
    List<ShelterAgent> findByStatusNot(ShelterStatus status);

    List<ShelterAgent> findByCityIgnoreCaseAndStatusNot(String city, ShelterStatus status);
}
