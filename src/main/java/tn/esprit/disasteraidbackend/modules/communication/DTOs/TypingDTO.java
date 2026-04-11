package tn.esprit.disasteraidbackend.modules.communication.DTOs;

public class TypingDTO {

    private Integer conversationId;
    private Integer userId;
    private String email;

    public Integer getConversationId() {
        return conversationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}