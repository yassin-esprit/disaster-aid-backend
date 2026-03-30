package tn.esprit.disasteraidbackend.modules.resourceShelter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.hibernate.validator.constraints.URL;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.ShelterStatus;

public class ShelterCreateDto {
    @NotBlank private String name;

    @NotBlank @URL
    private String locationUrl;

    @NotBlank private String address;

    @NotNull @Positive
    private Integer capacity;

    @NotNull
    private Integer managerId;

    private List<String> amenities;

    @NotNull
    private ShelterStatus status;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocationUrl() { return locationUrl; }
    public void setLocationUrl(String locationUrl) { this.locationUrl = locationUrl; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public Integer getManagerId() { return managerId; }
    public void setManagerId(Integer managerId) { this.managerId = managerId; }
    public List<String> getAmenities() { return amenities; }
    public void setAmenities(List<String> amenities) { this.amenities = amenities; }
    public ShelterStatus getStatus() { return status; }
    public void setStatus(ShelterStatus status) { this.status = status; }
}