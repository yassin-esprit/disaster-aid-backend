package tn.esprit.disasteraidbackend.modules.coordination.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.coordination.entity.TaskAssignment;

import java.util.List;

@Repository
public interface TaskAssignmentRepository extends JpaRepository<TaskAssignment, Integer> {
    List<TaskAssignment> findByHelpRequestId(Integer helpRequestId);
    List<TaskAssignment> findByResponderId(Integer responderId);
    List<TaskAssignment> findByStatus(String status);
}