package tn.esprit.disasteraidbackend.modules.opportunity.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OpportunityResponseDto {
    private Integer id;
    private Integer orgId;
    private String title;
    private String description;
    private String category;
    private String location;
    private boolean isRemote;
    private LocalDate scheduledDate;
    private Double durationHours;
    private int slots;
    private int filledSlots;
    private String status;
    private Integer minAge;
    private boolean requiresBackgroundCheck;
    private List<Integer> requiredSkillIds;
    private LocalDateTime closedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
