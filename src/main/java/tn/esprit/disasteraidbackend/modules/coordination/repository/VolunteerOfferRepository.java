package tn.esprit.disasteraidbackend.modules.coordination.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.coordination.entity.VolunteerOffer;

import java.util.List;

@Repository
public interface VolunteerOfferRepository extends JpaRepository<VolunteerOffer, Integer> {
    List<VolunteerOffer> findByStatus(String status);
    List<VolunteerOffer> findByVolunteerId(Integer volunteerId);
}