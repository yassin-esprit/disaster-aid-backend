package tn.esprit.disasteraidbackend.modules.coordination.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.coordination.dto.VolunteerOfferRequestDto;
import tn.esprit.disasteraidbackend.modules.coordination.dto.VolunteerOfferResponseDto;
import tn.esprit.disasteraidbackend.modules.coordination.entity.VolunteerOffer;
import tn.esprit.disasteraidbackend.modules.coordination.repository.VolunteerOfferRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VolunteerOfferService {

    private final VolunteerOfferRepository repository;

    public List<VolunteerOfferResponseDto> getAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public VolunteerOfferResponseDto getById(Integer id) {
        return toResponse(findById(id));
    }

    public VolunteerOfferResponseDto create(VolunteerOfferRequestDto dto) {
        VolunteerOffer offer = toEntity(dto);
        offer.setStatus("ACTIVE");
        return toResponse(repository.save(offer));
    }

    public VolunteerOfferResponseDto update(Integer id, VolunteerOfferRequestDto dto) {
        VolunteerOffer existing = findById(id);
        existing.setSkills(dto.getSkills());
        existing.setLocation(dto.getLocation());
        existing.setAvailableFrom(dto.getAvailableFrom());
        existing.setAvailableTo(dto.getAvailableTo());
        existing.setMaxRadiusKm(dto.getMaxRadiusKm());
        return toResponse(repository.save(existing));
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    public List<VolunteerOfferResponseDto> getByStatus(String status) {
        return repository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private VolunteerOffer findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("VolunteerOffer not found with id: " + id));
    }

    private VolunteerOffer toEntity(VolunteerOfferRequestDto dto) {
        VolunteerOffer offer = new VolunteerOffer();
        offer.setVolunteerId(dto.getVolunteerId());
        offer.setSkills(dto.getSkills());
        offer.setLocation(dto.getLocation());
        offer.setAvailableFrom(dto.getAvailableFrom());
        offer.setAvailableTo(dto.getAvailableTo());
        offer.setMaxRadiusKm(dto.getMaxRadiusKm());
        return offer;
    }

    private VolunteerOfferResponseDto toResponse(VolunteerOffer offer) {
        VolunteerOfferResponseDto dto = new VolunteerOfferResponseDto();
        dto.setId(offer.getId());
        dto.setVolunteerId(offer.getVolunteerId());
        dto.setSkills(offer.getSkills());
        dto.setLocation(offer.getLocation());
        dto.setAvailableFrom(offer.getAvailableFrom());
        dto.setAvailableTo(offer.getAvailableTo());
        dto.setMaxRadiusKm(offer.getMaxRadiusKm());
        dto.setStatus(offer.getStatus());
        dto.setCreatedAt(offer.getCreatedAt());
        dto.setUpdatedAt(offer.getUpdatedAt());
        return dto;
    }
}