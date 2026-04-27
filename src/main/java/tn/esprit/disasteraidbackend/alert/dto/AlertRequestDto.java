package tn.esprit.disasteraidbackend.alert.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tn.esprit.disasteraidbackend.alert.entity.AlertSeverity;
import tn.esprit.disasteraidbackend.alert.entity.AlertStatus;
import tn.esprit.disasteraidbackend.alert.entity.AlertType;

public class AlertRequestDto {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private AlertType type;

    @NotNull
    private AlertSeverity severity;

    private AlertStatus status;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private Double radiusKm;

    private String region;

    private String country;

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public AlertType getType() { return type; }
    public AlertSeverity getSeverity() { return severity; }
    public AlertStatus getStatus() { return status; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public Double getRadiusKm() { return radiusKm; }
    public String getRegion() { return region; }
    public String getCountry() { return country; }
}
