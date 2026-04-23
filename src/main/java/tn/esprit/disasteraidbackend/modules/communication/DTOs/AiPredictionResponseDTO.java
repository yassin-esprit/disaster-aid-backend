package tn.esprit.disasteraidbackend.modules.communication.DTOs;

public class AiPredictionResponseDTO {

    private String category;
    private String priority;

    public AiPredictionResponseDTO() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}