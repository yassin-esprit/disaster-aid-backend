package tn.esprit.disasteraidbackend.modules.gamification.service;

import tn.esprit.disasteraidbackend.modules.gamification.dto.VolunteerAchievementRequest;
import tn.esprit.disasteraidbackend.modules.gamification.dto.VolunteerAchievementResponse;

import java.util.List;

public interface VolunteerAchievementService {

    VolunteerAchievementResponse awardAchievement(VolunteerAchievementRequest request);

    VolunteerAchievementResponse getById(Long id);

    List<VolunteerAchievementResponse> getAll();

    List<VolunteerAchievementResponse> getByVolunteer(Long volunteerId);

    List<VolunteerAchievementResponse> getDisplayedByVolunteer(Long volunteerId);

    VolunteerAchievementResponse toggleDisplay(Long id, boolean isDisplayed);

    void delete(Long id);
}
