package tn.esprit.disasteraidbackend.modules.hours.dto;

import lombok.Data;
import tn.esprit.disasteraidbackend.modules.hours.entity.ConfidenceLevel;

@Data
public class ImpactRecordRequest {
    private Long volunteerId;
    private Float totalApprovedHours;
    private Float co2OffsetKg;
    private Integer mealsServed;
    private Integer peopleHelped;
    private Float wasteRemovedKg;
    private Float hoursOfTutoring;
    private ConfidenceLevel confidenceLevel;
}
