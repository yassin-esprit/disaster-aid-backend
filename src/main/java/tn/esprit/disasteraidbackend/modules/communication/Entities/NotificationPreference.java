package tn.esprit.disasteraidbackend.modules.communication.Entities;

import jakarta.persistence.*;
import tn.esprit.disasteraidbackend.modules.identity.Entities.User;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "notification_preferences")
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer prefId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotifType type;

    @ElementCollection(targetClass = Channel.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "notification_preference_channels", joinColumns = @JoinColumn(name = "pref_id"))
    @Column(name = "channel")
    private List<Channel> channels;

    @Column(name = "is_enabled")
    private Boolean enabled;

    private LocalTime quietHoursFrom;
    private LocalTime quietHoursTo;

    public Integer getPrefId() {
        return prefId;
    }

    public void setPrefId(Integer prefId) {
        this.prefId = prefId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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

    @PrePersist
    public void prePersist() {
        if (enabled == null) {
            enabled = true;
        }
    }
}