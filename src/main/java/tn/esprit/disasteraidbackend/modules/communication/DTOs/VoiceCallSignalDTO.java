package tn.esprit.disasteraidbackend.modules.communication.DTOs;

import tn.esprit.disasteraidbackend.modules.communication.Entities.VoiceCallSignalType;

import java.time.LocalDateTime;

public class VoiceCallSignalDTO {

    private Integer conversationId;
    private Integer fromUserId;
    private Integer toUserId;
    private String callId;
    private VoiceCallSignalType type;
    private String payload;
    private LocalDateTime timestamp;

    public Integer getConversationId() {
        return conversationId;
    }

    public void setConversationId(Integer conversationId) {
        this.conversationId = conversationId;
    }

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public VoiceCallSignalType getType() {
        return type;
    }

    public void setType(VoiceCallSignalType type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
