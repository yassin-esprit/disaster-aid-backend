package tn.esprit.disasteraidbackend.modules.communication.DTOs;

import tn.esprit.disasteraidbackend.modules.communication.Entities.Channel;
import tn.esprit.disasteraidbackend.modules.communication.Entities.NotifType;

import java.time.LocalTime;
import java.util.List;

public class NotificationPreferenceResponseDTO {

    private Integer prefId;
    private Integer userId;
    private String userEmail;
    private NotifType type;
    private List<Channel> channels;
    private LocalTime quietHoursFrom;
    private LocalTime quietHoursTo;
    private Boolean enabled;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    public Integer getPrefId() {
        return prefId;
    }

    public void setPrefId(Integer prefId) {
        this.prefId = prefId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public NotifType getType() {
        return type;
    }

    public void setType(NotifType type) {
        this.type = type;
    }

    public List<Channel> getChannels() {
        return channels;
    }

    public void setChannels(List<Channel> channels) {
        this.channels = channels;
    }



    public LocalTime getQuietHoursFrom() {
        return quietHoursFrom;
    }

    public void setQuietHoursFrom(LocalTime quietHoursFrom) {
        this.quietHoursFrom = quietHoursFrom;
    }

    public LocalTime getQuietHoursTo() {
        return quietHoursTo;
    }

    public void setQuietHoursTo(LocalTime quietHoursTo) {
        this.quietHoursTo = quietHoursTo;
    }
}