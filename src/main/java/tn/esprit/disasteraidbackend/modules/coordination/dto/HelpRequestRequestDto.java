package tn.esprit.disasteraidbackend.modules.coordination.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelpRequestRequestDto {

    private Integer reporterId;
    private Integer alertId;
    private String type;
    private Integer urgency;
    private String location;
    private String description;
}