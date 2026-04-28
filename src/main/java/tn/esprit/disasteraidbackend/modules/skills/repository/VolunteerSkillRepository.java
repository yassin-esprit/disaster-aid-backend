package tn.esprit.disasteraidbackend.modules.skills.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.skills.entity.VolunteerSkill;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerSkillRepository extends JpaRepository<VolunteerSkill, Integer> {
    List<VolunteerSkill> findByVolunteerId(Integer volunteerId);
    List<VolunteerSkill> findBySkillId(Integer skillId);
    Optional<VolunteerSkill> findByVolunteerIdAndSkillId(Integer volunteerId, Integer skillId);
}
