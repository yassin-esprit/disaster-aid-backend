package tn.esprit.disasteraidbackend.modules.communication.DTOs;

import tn.esprit.disasteraidbackend.modules.communication.Entities.ConvoType;

import java.time.LocalDateTime;
import java.util.List;

public class ConversationRequestDTO {

    private ConvoType type;
    private Integer relatedEntityId;
    private String relatedEntityType;
    private List<Integer> participantIds;
    private LocalDateTime lastMessageAt;

    public ConvoType getType() {
        return type;
    }

    public void setType(ConvoType type) {
        this.type = type;
    }

    public Integer getRelatedEntityId() {
        return relatedEntityId;
    }

    public void setRelatedEntityId(Integer relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }

    public String getRelatedEntityType() {
        return relatedEntityType;
    }

    public void setRelatedEntityType(String relatedEntityType) {
        this.relatedEntityType = relatedEntityType;
    }

    public List<Integer> getParticipantIds() {
        return participantIds;
    }

    public void setParticipantIds(List<Integer> participantIds) {
        this.participantIds = participantIds;
    }

    public LocalDateTime getLastMessageAt() {
        return lastMessageAt;
    }

    public void setLastMessageAt(LocalDateTime lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }
}