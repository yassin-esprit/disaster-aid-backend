package tn.esprit.disasteraidbackend.modules.communication.DTOs;

public class DiscussionRequestCreateDTO {

    private String subject;
    private String description;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}