package tn.esprit.disasteraidbackend.modules.communication.DTOs;

import tn.esprit.disasteraidbackend.modules.communication.Entities.Channel;
import tn.esprit.disasteraidbackend.modules.communication.Entities.NotifType;

import java.time.LocalTime;
import java.util.List;

public class NotificationPreferenceRequestDTO {

    private Integer userId;
    private NotifType type;
    private List<Channel> channels;
    private Boolean enabled;
    private LocalTime quietHoursFrom;
    private LocalTime quietHoursTo;

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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