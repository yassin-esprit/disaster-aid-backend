package tn.esprit.disasteraidbackend.modules.communication.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.communication.Entities.NotificationPreference;
@Repository
public interface NotificationPreferenceRepository extends JpaRepository<NotificationPreference, Integer> {
}