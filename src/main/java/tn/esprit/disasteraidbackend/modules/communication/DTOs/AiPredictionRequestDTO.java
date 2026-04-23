package tn.esprit.disasteraidbackend.modules.communication.DTOs;

public class AiPredictionRequestDTO {

    private String text;

    public AiPredictionRequestDTO() {
    }

    public AiPredictionRequestDTO(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}