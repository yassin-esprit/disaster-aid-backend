package tn.esprit.disasteraidbackend.modules.coordination.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.coordination.entity.VolunteerOffer;
import tn.esprit.disasteraidbackend.modules.coordination.repository.VolunteerOfferRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VolunteerOfferService {

    private final VolunteerOfferRepository repository;

    public List<VolunteerOffer> getAll() {
        return repository.findAll();
    }

    public VolunteerOffer getById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VolunteerOffer not found with id: " + id));
    }

    public VolunteerOffer create(VolunteerOffer offer) {
        offer.setStatus("ACTIVE");
        return repository.save(offer);
    }

    public VolunteerOffer update(Integer id, VolunteerOffer updated) {
        VolunteerOffer existing = getById(id);
        existing.setSkills(updated.getSkills());
        existing.setLocation(updated.getLocation());
        existing.setAvailableFrom(updated.getAvailableFrom());
        existing.setAvailableTo(updated.getAvailableTo());
        existing.setMaxRadiusKm(updated.getMaxRadiusKm());
        existing.setStatus(updated.getStatus());
        return repository.save(existing);
    }

    public void delete(Integer id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VolunteerOffer not found with id: " + id));
        repository.deleteById(id);
    }

    public List<VolunteerOffer> getByStatus(String status) {
        return repository.findByStatus(status);
    }
}
