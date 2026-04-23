package tn.esprit.disasteraidbackend.modules.communication.Services;

import tn.esprit.disasteraidbackend.modules.communication.DTOs.AiPredictionResponseDTO;

public interface AiPredictionService {
    AiPredictionResponseDTO predict(String subject, String description);
}