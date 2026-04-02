package tn.esprit.disasteraidbackend.modules.communication.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.communication.Entities.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    void deleteBySender_UserId(Integer userId);
}