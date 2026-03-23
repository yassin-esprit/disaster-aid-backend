package tn.esprit.disasteraidbackend.modules.communication.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.communication.Entities.NotificationPreference;

public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Integer> {
}