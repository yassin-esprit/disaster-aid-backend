package tn.esprit.disasteraidbackend.modules.resourceShelter.service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ShelterOccupantCreateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ShelterOccupantUpdateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.Shelter;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.ShelterOccupant;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.ShelterStatus;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.User;
import tn.esprit.disasteraidbackend.modules.resourceShelter.repository.ShelterOccupantRepository;
import tn.esprit.disasteraidbackend.modules.resourceShelter.repository.ShelterRepository;
import tn.esprit.disasteraidbackend.modules.resourceShelter.repository.UserRepository;

@Service
public class ShelterOccupantServiceImpl implements IShelterOccupantService {

    private final ShelterOccupantRepository occupantRepository;
    private final ShelterRepository shelterRepository;
    private final UserRepository userRepository;

    public ShelterOccupantServiceImpl(ShelterOccupantRepository occupantRepository,
                                      ShelterRepository shelterRepository,
                                      UserRepository userRepository) {
        this.occupantRepository = occupantRepository;
        this.shelterRepository = shelterRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public ShelterOccupant checkIn(ShelterOccupantCreateDto dto) {
        Shelter shelter = shelterRepository.findById(dto.getShelterId())
                .orElseThrow(() -> new EntityNotFoundException("Shelter not found: " + dto.getShelterId()));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + dto.getUserId()));

        if (shelter.getStatus() == ShelterStatus.CLOSED) {
            throw new IllegalStateException("Shelter is CLOSED");
        }
        if (shelter.getCurrentOccupancy() >= shelter.getCapacity()) {
            shelter.setStatus(ShelterStatus.FULL);
            shelterRepository.save(shelter);
            throw new IllegalStateException("Shelter is FULL");
        }

        ShelterOccupant o = new ShelterOccupant();
        o.setShelter(shelter);
        o.setUser(user);
        o.setNeedStrings(dto.getNeedsFlag());
        // checkInAt auto via @PrePersist

        // update occupancy
        shelter.setCurrentOccupancy(shelter.getCurrentOccupancy() + 1);
        if (shelter.getCurrentOccupancy().equals(shelter.getCapacity())) {
            shelter.setStatus(ShelterStatus.FULL);
        } else if (shelter.getStatus() != ShelterStatus.OPEN) {
            shelter.setStatus(ShelterStatus.OPEN);
        }

        shelterRepository.save(shelter);
        return occupantRepository.save(o);
    }

    @Override
    @Transactional
    public ShelterOccupant checkOut(Integer recordId) {
        ShelterOccupant o = occupantRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException("Occupant record not found: " + recordId));

        if (o.getCheckOutAt() != null) {
            return o;
        }

        o.setCheckOutAt(LocalDateTime.now());

        Shelter shelter = o.getShelter();
        int newOcc = Math.max(0, shelter.getCurrentOccupancy() - 1);
        shelter.setCurrentOccupancy(newOcc);

        if (shelter.getStatus() == ShelterStatus.FULL && newOcc < shelter.getCapacity()) {
            shelter.setStatus(ShelterStatus.OPEN);
        }

        shelterRepository.save(shelter);
        return occupantRepository.save(o);
    }

    @Override
    public ShelterOccupant getShelterOccupant(Integer id) {
        return occupantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Occupant record not found: " + id));
    }

    @Override
    @Transactional
    public ShelterOccupant updateShelterOccupant(Integer id, ShelterOccupantUpdateDto dto) {
        ShelterOccupant o = getShelterOccupant(id);

        Shelter shelter = shelterRepository.findById(dto.getShelterId())
                .orElseThrow(() -> new EntityNotFoundException("Shelter not found: " + dto.getShelterId()));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + dto.getUserId()));

        o.setShelter(shelter);
        o.setUser(user);

        if (dto.getCheckInAt() != null) o.setCheckInAt(dto.getCheckInAt());
        if (dto.getCheckOutAt() != null) o.setCheckOutAt(dto.getCheckOutAt());

        o.setNeedStrings(dto.getNeedsFlag());

        return occupantRepository.save(o);
    }

    @Override
    public void deleteShelterOccupant(Integer id) {
        occupantRepository.deleteById(id);
    }

    @Override
    public List<ShelterOccupant> getAllShelterOccupants() {
        return occupantRepository.findAll();
    }

    @Override
    public List<ShelterOccupant> getOccupantsByShelterId(Integer shelterId) {
        return occupantRepository.findByShelter_ShelterId(shelterId);
    }
}