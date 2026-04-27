package tn.esprit.disasteraidbackend.modules.shelteragent.service;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.shelteragent.dto.NearbyShelterDto;
import tn.esprit.disasteraidbackend.modules.shelteragent.dto.ShelterRecommendationDto;
import tn.esprit.disasteraidbackend.modules.shelteragent.dto.ShelterRecommendationRequest;
import tn.esprit.disasteraidbackend.modules.shelteragent.dto.ShelterRecommendationResponse;
import tn.esprit.disasteraidbackend.modules.shelteragent.entity.ShelterAgent;
import tn.esprit.disasteraidbackend.modules.shelteragent.entity.ShelterStatus;
import tn.esprit.disasteraidbackend.modules.shelteragent.entity.ShelterType;
import tn.esprit.disasteraidbackend.modules.shelteragent.repository.ShelterAgentRepository;
import tn.esprit.disasteraidbackend.modules.shelteragent.repository.ShelterReviewRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShelterRecommendationService {

    private static final double DEFAULT_MAX_DISTANCE_KM = 40.0;
    private static final double DISTANCE_WEIGHT = 25.0;
    private static final double REMAINING_WEIGHT = 20.0;
    private static final double MEDICAL_MATCH_BONUS = 50.0;
    private static final double RATING_WEIGHT = 5.0;
    private static final double FULL_PENALTY = 60.0;

    private final ShelterAgentRepository shelterRepository;
    private final ShelterReviewRepository shelterReviewRepository;

    public ShelterRecommendationService(
            ShelterAgentRepository shelterRepository,
            ShelterReviewRepository shelterReviewRepository
    ) {
        this.shelterRepository = shelterRepository;
        this.shelterReviewRepository = shelterReviewRepository;
    }

    public List<ShelterAgent> getAllShelters() {
        return shelterRepository.findAll();
    }

    public List<NearbyShelterDto> fetchCandidates(double lat, double lng, Double maxDistanceKm, String city) {
        List<ShelterAgent> base = (city == null || city.isBlank())
                ? shelterRepository.findByStatusNot(ShelterStatus.CLOSED)
                : shelterRepository.findByCityIgnoreCaseAndStatusNot(city.trim(), ShelterStatus.CLOSED);

        double threshold = maxDistanceKm == null ? DEFAULT_MAX_DISTANCE_KM : maxDistanceKm;

        Map<Integer, ShelterReviewRepository.ShelterRatingSummary> ratingMap = loadRatings(base);

        return base.stream()
            .map(shelter -> toNearbyDto(shelter, lat, lng, ratingMap.get(shelter.getShelteragentId())))
                .filter(dto -> dto.getDistanceKm() <= threshold)
                .sorted(Comparator.comparingDouble(NearbyShelterDto::getDistanceKm))
                .collect(Collectors.toList());
    }

    public ShelterRecommendationResponse rankShelters(ShelterRecommendationRequest request) {
        Double maxDistance = request.getMaxDistanceKm() == null ? DEFAULT_MAX_DISTANCE_KM : request.getMaxDistanceKm();
        List<NearbyShelterDto> candidates = fetchCandidates(request.getLat(), request.getLng(), maxDistance, null);

        if (Boolean.TRUE.equals(request.getNeedsAccessible())) {
            candidates = candidates.stream().filter(NearbyShelterDto::isHasAccessibility).collect(Collectors.toList());
        }
        if (Boolean.TRUE.equals(request.getHasPets())) {
            candidates = candidates.stream().filter(NearbyShelterDto::isAcceptsPets).collect(Collectors.toList());
        }

        List<ShelterRecommendationDto> ranked = candidates.stream()
                .map(candidate -> scoreShelter(candidate, request))
                .sorted(Comparator.comparingDouble(ShelterRecommendationDto::getScore).reversed())
                .limit(3)
                .collect(Collectors.toList());

        ShelterRecommendationResponse response = new ShelterRecommendationResponse();
        response.setRecommendations(ranked);
        return response;
    }

    public ShelterRecommendationDto scoreShelter(NearbyShelterDto shelter, ShelterRecommendationRequest request) {
        int remaining = Math.max(0, shelter.getCapacity() - shelter.getOccupancy());
        boolean isFull = shelter.getStatus() == ShelterStatus.FULL || remaining == 0;
        boolean typeMatch = Boolean.TRUE.equals(request.getNeedsMedical()) && shelter.getType() == ShelterType.MEDICAL;

        double distanceScore = DISTANCE_WEIGHT * clamp01(1.0 - shelter.getDistanceKm() / 10.0);
        double remainingScore = REMAINING_WEIGHT * clamp01(remaining / 10.0);
        double ratingNorm = shelter.getAvgRating() == null ? 0.0 : shelter.getAvgRating() / 5.0;
        double ratingScore = RATING_WEIGHT * clamp01(ratingNorm);
        double score = distanceScore + remainingScore + ratingScore;

        if (typeMatch) {
            score += MEDICAL_MATCH_BONUS;
        }
        if (isFull) {
            score -= FULL_PENALTY;
        }

        ShelterRecommendationDto dto = new ShelterRecommendationDto();
        dto.setShelteragentId(shelter.getShelteragentId());
        dto.setName(shelter.getName());
        dto.setCapacity(shelter.getCapacity());
        dto.setOccupancy(shelter.getOccupancy());
        dto.setAddress(shelter.getAddress());
        dto.setLocationUrl(shelter.getLocationUrl());
        dto.setDistanceKm(round2(shelter.getDistanceKm()));
        dto.setRemaining(remaining);
        dto.setType(shelter.getType());
        dto.setStatus(shelter.getStatus());
        dto.setScore(round2(score));
        dto.setReasons(buildReasons(shelter, remaining, isFull, typeMatch, request));

        return dto;
    }

    private List<String> buildReasons(
            NearbyShelterDto shelter,
            int remaining,
            boolean isFull,
            boolean typeMatch,
            ShelterRecommendationRequest request
    ) {
        List<String> reasons = new ArrayList<>();
        reasons.add(round2(shelter.getDistanceKm()) + " km away");
        reasons.add(remaining + " places remaining");

        if (typeMatch) {
            reasons.add("Medical shelter matches your need");
        }
        if (Boolean.TRUE.equals(request.getNeedsAccessible()) && shelter.isHasAccessibility()) {
            reasons.add("Accessibility support available");
        }
        if (Boolean.TRUE.equals(request.getHasPets()) && shelter.isAcceptsPets()) {
            reasons.add("Accepts pets");
        }
        if (shelter.getAvgRating() != null && shelter.getReviewCount() > 0) {
            reasons.add("Rating " + round2(shelter.getAvgRating()) + "/5 (" + shelter.getReviewCount() + " reviews)");
        }
        if (isFull) {
            reasons.add("Currently FULL");
        }

        return reasons;
    }

    private NearbyShelterDto toNearbyDto(
            ShelterAgent shelter,
            double userLat,
            double userLng,
            ShelterReviewRepository.ShelterRatingSummary ratingSummary
    ) {
        NearbyShelterDto dto = new NearbyShelterDto();
        dto.setShelteragentId(shelter.getShelteragentId());
        dto.setName(shelter.getName());
        dto.setAddress(shelter.getAddress());
        dto.setCity(shelter.getCity());
        dto.setType(shelter.getType());
        dto.setStatus(shelter.getStatus());
        dto.setCapacity(shelter.getCapacity());
        dto.setOccupancy(shelter.getCurrentOccupancy());
        dto.setRemaining(Math.max(0, shelter.getCapacity() - shelter.getCurrentOccupancy()));
        dto.setHasAccessibility(shelter.isHasAccessibility());
        dto.setAcceptsPets(shelter.isAcceptsPets());
        dto.setPhone(shelter.getPhone());
        dto.setLocationUrl(shelter.getLocationUrl());
        dto.setDistanceKm(haversineKm(userLat, userLng, shelter.getLatitude(), shelter.getLongitude()));

        if (ratingSummary != null) {
            dto.setAvgRating(ratingSummary.getAvgRating());
            dto.setReviewCount(ratingSummary.getReviewCount());
        } else {
            dto.setAvgRating(null);
            dto.setReviewCount(0L);
        }

        return dto;
    }

    private Map<Integer, ShelterReviewRepository.ShelterRatingSummary> loadRatings(List<ShelterAgent> shelters) {
        if (shelters.isEmpty()) {
            return Map.of();
        }
        List<Integer> ids = shelters.stream().map(ShelterAgent::getShelteragentId).toList();
        return shelterReviewRepository.summarizeByShelterIds(ids)
                .stream()
                .collect(Collectors.toMap(ShelterReviewRepository.ShelterRatingSummary::getShelteragentId, v -> v, (a, b) -> a, HashMap::new));
    }

    private double clamp01(double value) {
        if (value < 0) {
            return 0;
        }
        return Math.min(value, 1);
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private double haversineKm(double lat1, double lng1, double lat2, double lng2) {
        final double earthRadiusKm = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusKm * c;
    }
}