package tn.esprit.disasteraidbackend.alert.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.alert.entity.AlertSnapshot;

import java.util.List;

public interface AlertSnapshotRepository extends JpaRepository<AlertSnapshot, Integer> {
    List<AlertSnapshot> findByAlertIdOrderBySnapshotAtAsc(Integer alertId);
}
