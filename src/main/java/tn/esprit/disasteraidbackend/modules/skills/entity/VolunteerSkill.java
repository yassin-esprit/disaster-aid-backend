package tn.esprit.disasteraidbackend.modules.skills.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.disasteraidbackend.common.BaseEntity;

@Entity
@Table(name = "volunteer_skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerSkill extends BaseEntity {

    @Column(name = "volunteer_id", nullable = false)
    private Integer volunteerId;

    @Column(name = "skill_id", nullable = false)
    private Integer skillId;

    @Column(nullable = false)
    private String proficiency;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Column(name = "verification_doc_url")
    private String verificationDocUrl;

    @Column(name = "endorsement_count", nullable = false)
    private int endorsementCount = 0;
}
