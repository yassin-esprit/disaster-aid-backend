package tn.esprit.disasteraidbackend.modules.opportunity.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OpportunityRequestDto {

    @NotNull
    private Integer orgId;

    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String category;

    private String location;

    @NotNull
    private Boolean isRemote;

    private LocalDate scheduledDate;

    private Double durationHours;

    @NotNull
    @Min(1)
    private Integer slots;

    private String status;

    private Integer minAge;

    private Boolean requiresBackgroundCheck;

    private List<Integer> requiredSkillIds;
}
