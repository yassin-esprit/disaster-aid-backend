package tn.esprit.disasteraidbackend.modules.communication.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}