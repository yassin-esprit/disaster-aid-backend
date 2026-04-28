package tn.esprit.disasteraidbackend.modules.opportunity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationRequestDto {

    @NotNull
    private Integer opportunityId;

    @NotNull
    private Integer volunteerId;

    private String motivationLetter;
}
