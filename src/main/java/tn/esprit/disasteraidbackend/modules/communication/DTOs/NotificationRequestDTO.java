package tn.esprit.disasteraidbackend.modules.communication.DTOs;

import tn.esprit.disasteraidbackend.modules.communication.Entities.Channel;
import tn.esprit.disasteraidbackend.modules.communication.Entities.NotifType;

import java.time.LocalDateTime;

public class NotificationRequestDTO {

    private Integer recipientId;
    private NotifType type;
    private String title;
    private String body;
    private String data;
    private Channel channel;
    private Boolean isRead;
    private Boolean isCritical;
    private LocalDateTime sentAt;
    private LocalDateTime readAt;

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public NotifType getType() {
        return type;
    }

    public void setType(NotifType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean read) {
        isRead = read;
    }

    public Boolean getIsCritical() {
        return isCritical;
    }

    public void setIsCritical(Boolean critical) {
        isCritical = critical;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public LocalDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(LocalDateTime readAt) {
        this.readAt = readAt;
    }
}