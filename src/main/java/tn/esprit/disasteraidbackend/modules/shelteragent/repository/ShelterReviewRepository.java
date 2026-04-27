package tn.esprit.disasteraidbackend.modules.shelteragent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.disasteraidbackend.modules.shelteragent.entity.ShelterReview;

import java.util.Collection;
import java.util.List;

public interface ShelterReviewRepository extends JpaRepository<ShelterReview, Long> {

    @Query("""
            select r.shelterAgent.shelteragentId as shelteragentId,
                   avg(r.rating) as avgRating,
                   count(r.id) as reviewCount
            from ShelterReview r
            where r.shelterAgent.shelteragentId in :shelterIds
            group by r.shelterAgent.shelteragentId
            """)
    List<ShelterRatingSummary> summarizeByShelterIds(@Param("shelterIds") Collection<Integer> shelterIds);

    interface ShelterRatingSummary {
        Integer getShelteragentId();

        Double getAvgRating();

        Long getReviewCount();
    }
}