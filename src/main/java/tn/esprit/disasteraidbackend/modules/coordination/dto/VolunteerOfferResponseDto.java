package tn.esprit.disasteraidbackend.modules.coordination.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VolunteerOfferResponseDto {

    private Integer id;
    private Integer volunteerId;
    private String skills;
    private String location;
    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;
    private Double maxRadiusKm;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}