package tn.esprit.disasteraidbackend.modules.coordination.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.coordination.dto.VolunteerOfferRequestDto;
import tn.esprit.disasteraidbackend.modules.coordination.dto.VolunteerOfferResponseDto;
import tn.esprit.disasteraidbackend.modules.coordination.entity.HelpRequest;
import tn.esprit.disasteraidbackend.modules.coordination.entity.VolunteerOffer;
import tn.esprit.disasteraidbackend.modules.coordination.repository.HelpRequestRepository;
import tn.esprit.disasteraidbackend.modules.coordination.repository.VolunteerOfferRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VolunteerOfferService {

    private final VolunteerOfferRepository repository;
    private final HelpRequestRepository helpRequestRepository;
    private Double matchScore;

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



    public List<VolunteerOfferResponseDto> getMatchesForRequest(Integer helpRequestId) {
        HelpRequest request = helpRequestRepository.findById(helpRequestId)
                .orElseThrow(() -> new RuntimeException("HelpRequest not found: " + helpRequestId));

        return repository.findByStatus("ACTIVE")
                .stream()
                .map(offer -> {
                    VolunteerOfferResponseDto dto = toResponse(offer);
                    dto.setMatchScore(computeMatchScore(offer, request));
                    return dto;
                })
                .filter(dto -> dto.getMatchScore() > 0.0)
                .sorted((a, b) -> Double.compare(b.getMatchScore(), a.getMatchScore()))
                .collect(Collectors.toList());
    }

    private double computeMatchScore(VolunteerOffer offer, HelpRequest request) {
        double score = 0.0;

        // Availability — offer is currently active
        if ("ACTIVE".equals(offer.getStatus())) score += 0.30;

        // Urgency match — higher urgency = higher score
        int urgency = request.getUrgency() != null ? request.getUrgency() : 1;
        score += (urgency / 5.0) * 0.40;

        // Skills keyword match (simple string contains check)
        String skills = offer.getSkills() != null ? offer.getSkills().toLowerCase() : "";
        String type   = request.getType()  != null ? request.getType().toLowerCase()  : "";
        if (!skills.isEmpty() && !type.isEmpty()) {
            if (skills.contains(type) ||
                    (type.equals("medical")    && skills.contains("first aid")) ||
                    (type.equals("rescue")     && skills.contains("rescue"))    ||
                    (type.equals("evacuation") && skills.contains("transport"))) {
                score += 0.30;
            }
        }

        return Math.min(Math.round(score * 100.0) / 100.0, 1.0);
    }
}