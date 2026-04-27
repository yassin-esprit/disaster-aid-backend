package tn.esprit.disasteraidbackend.modules.hours.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.disasteraidbackend.modules.hours.entity.Certificate;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findByVolunteerId(Long volunteerId);

    List<Certificate> findByOrgId(Long orgId);

    List<Certificate> findByVolunteerIdAndOrgId(Long volunteerId, Long orgId);
}
