package tn.esprit.disasteraidbackend.modules.communication.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Message;

public interface MessageRepository extends JpaRepository<Message, Integer> {
}