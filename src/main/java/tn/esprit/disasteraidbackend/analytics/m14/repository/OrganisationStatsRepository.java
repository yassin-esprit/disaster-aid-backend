package tn.esprit.disasteraidbackend.analytics.m14.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.disasteraidbackend.analytics.m14.entity.OrganisationStats;

import java.util.Optional;

public interface OrganisationStatsRepository extends JpaRepository<OrganisationStats, Integer> {

    Optional<OrganisationStats> findTopByOrgIdOrderByComputedAtDesc(Integer orgId);
}

