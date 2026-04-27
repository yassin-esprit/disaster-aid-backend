package tn.esprit.disasteraidbackend.modules.shelteragent.dto;

import java.util.ArrayList;
import java.util.List;

public class ShelterRecommendationResponse {
    private List<ShelterRecommendationDto> recommendations = new ArrayList<>();

    public List<ShelterRecommendationDto> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<ShelterRecommendationDto> recommendations) {
        this.recommendations = recommendations;
    }
}