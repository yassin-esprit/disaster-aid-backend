package tn.esprit.disasteraidbackend.modules.resourceShelter.service;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ShelterCreateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ShelterUpdateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.Shelter;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.ShelterStatus;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.User;
import tn.esprit.disasteraidbackend.modules.resourceShelter.repository.ShelterRepository;
import tn.esprit.disasteraidbackend.modules.resourceShelter.repository.UserRepository;

@Service
public class ShelterServiceImpl implements IShelterService {

    private final ShelterRepository shelterRepository;
    private final UserRepository userRepository;

    public ShelterServiceImpl(ShelterRepository shelterRepository, UserRepository userRepository) {
        this.shelterRepository = shelterRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Shelter addShelter(ShelterCreateDto dto) {
        User manager = userRepository.findById(dto.getManagerId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + dto.getManagerId()));

        Shelter s = new Shelter();
        s.setName(dto.getName());
        s.setLocationUrl(dto.getLocationUrl());
        s.setAddress(dto.getAddress());
        s.setCapacity(dto.getCapacity());
        s.setManager(manager);
        s.setStatus(dto.getStatus());
        s.setAmenityStrings(dto.getAmenities());

        if (dto.getStatus() == ShelterStatus.OPEN && s.getOpenedAt() == null) {
            s.setOpenedAt(LocalDateTime.now());
        }

        return shelterRepository.save(s);
    }

    @Override
    public Shelter getShelter(Integer id) {
        return shelterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Shelter not found: " + id));
    }

    @Override
    public Shelter updateShelter(Integer id, ShelterUpdateDto dto) {
        Shelter s = getShelter(id);

        User manager = userRepository.findById(dto.getManagerId())
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + dto.getManagerId()));

        ShelterStatus oldStatus = s.getStatus();

        s.setName(dto.getName());
        s.setLocationUrl(dto.getLocationUrl());
        s.setAddress(dto.getAddress());
        s.setCapacity(dto.getCapacity());
        s.setManager(manager);
        s.setStatus(dto.getStatus());
        s.setAmenityStrings(dto.getAmenities());

        if (oldStatus != ShelterStatus.OPEN && dto.getStatus() == ShelterStatus.OPEN && s.getOpenedAt() == null) {
            s.setOpenedAt(LocalDateTime.now());
        }
        if (dto.getStatus() == ShelterStatus.CLOSED && s.getClosedAt() == null) {
            s.setClosedAt(LocalDateTime.now());
        }

        return shelterRepository.save(s);
    }

    @Override
    public void deleteShelter(Integer id) {
        shelterRepository.deleteById(id);
    }

    @Override
    public List<Shelter> getAllShelters() {
        return shelterRepository.findAll();
    }
}