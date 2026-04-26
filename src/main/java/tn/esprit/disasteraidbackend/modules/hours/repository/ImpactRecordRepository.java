package tn.esprit.disasteraidbackend.modules.hours.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.hours.entity.ImpactRecord;

import java.util.Optional;
import java.util.List;

@Repository
public interface ImpactRecordRepository extends JpaRepository<ImpactRecord, Long> {

    Optional<ImpactRecord> findByVolunteerId(Long volunteerId);

    List<ImpactRecord> findAll();
}
