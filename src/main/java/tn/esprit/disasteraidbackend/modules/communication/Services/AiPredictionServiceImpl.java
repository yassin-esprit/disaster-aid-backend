package tn.esprit.disasteraidbackend.modules.communication.Services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.AiPredictionRequestDTO;
import tn.esprit.disasteraidbackend.modules.communication.DTOs.AiPredictionResponseDTO;

@Service
public class AiPredictionServiceImpl implements AiPredictionService {

    private final RestTemplate restTemplate;

    public AiPredictionServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public AiPredictionResponseDTO predict(String subject, String description) {
        try {
            StringBuilder textBuilder = new StringBuilder();

            if (subject != null && !subject.isBlank()) {
                textBuilder.append(subject).append(" ");
            }

            if (description != null && !description.isBlank()) {
                textBuilder.append(description);
            }

            String text = textBuilder.toString().trim();

            if (text.isBlank()) {
                return null;
            }

            String flaskUrl = "http://127.0.0.1:5000/predict";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            AiPredictionRequestDTO requestDTO = new AiPredictionRequestDTO(text);
            HttpEntity<AiPredictionRequestDTO> requestEntity = new HttpEntity<>(requestDTO, headers);

            return restTemplate.postForObject(
                    flaskUrl,
                    requestEntity,
                    AiPredictionResponseDTO.class
            );

        } catch (Exception e) {
            System.out.println("Erreur lors de l'appel au modèle IA : " + e.getMessage());
            return null;
        }
    }
}