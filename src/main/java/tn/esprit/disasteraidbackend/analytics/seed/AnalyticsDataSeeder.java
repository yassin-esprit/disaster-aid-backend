package tn.esprit.disasteraidbackend.analytics.seed;

import org.jspecify.annotations.Nullable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.esprit.disasteraidbackend.analytics.m13.entity.AuditLog;
import tn.esprit.disasteraidbackend.analytics.m13.entity.DisasterStatistics;
import tn.esprit.disasteraidbackend.analytics.m13.entity.ModelType;
import tn.esprit.disasteraidbackend.analytics.m13.entity.PredictiveModel;
import tn.esprit.disasteraidbackend.analytics.m13.repository.AuditLogRepository;
import tn.esprit.disasteraidbackend.analytics.m13.repository.DisasterStatisticsRepository;
import tn.esprit.disasteraidbackend.analytics.m13.repository.PredictiveModelRepository;
import tn.esprit.disasteraidbackend.analytics.m14.entity.ChurnPrediction;
import tn.esprit.disasteraidbackend.analytics.m14.entity.InterventionType;
import tn.esprit.disasteraidbackend.analytics.m14.entity.OrganisationStats;
import tn.esprit.disasteraidbackend.analytics.m14.entity.VolunteerStatistics;
import tn.esprit.disasteraidbackend.analytics.m14.repository.ChurnPredictionRepository;
import tn.esprit.disasteraidbackend.analytics.m14.repository.OrganisationStatsRepository;
import tn.esprit.disasteraidbackend.analytics.m14.repository.VolunteerStatisticsRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class AnalyticsDataSeeder implements CommandLineRunner {

    private final DisasterStatisticsRepository disasterStatisticsRepository;
    private final AuditLogRepository auditLogRepository;
    private final PredictiveModelRepository predictiveModelRepository;
    private final VolunteerStatisticsRepository volunteerStatisticsRepository;
    private final OrganisationStatsRepository organisationStatsRepository;
    private final ChurnPredictionRepository churnPredictionRepository;

    public AnalyticsDataSeeder(DisasterStatisticsRepository disasterStatisticsRepository,
                               AuditLogRepository auditLogRepository,
                               PredictiveModelRepository predictiveModelRepository,
                               VolunteerStatisticsRepository volunteerStatisticsRepository,
                               OrganisationStatsRepository organisationStatsRepository,
                               ChurnPredictionRepository churnPredictionRepository) {
        this.disasterStatisticsRepository = disasterStatisticsRepository;
        this.auditLogRepository = auditLogRepository;
        this.predictiveModelRepository = predictiveModelRepository;
        this.volunteerStatisticsRepository = volunteerStatisticsRepository;
        this.organisationStatsRepository = organisationStatsRepository;
        this.churnPredictionRepository = churnPredictionRepository;
    }

    @Override
    public void run(String @Nullable ... args) {
        seedDisasterStatistics();
        seedAuditLogs();
        seedPredictiveModels();
        seedVolunteerStatistics();
        seedOrganisationStats();
        seedChurnPredictions();
    }

    private void seedDisasterStatistics() {
        if (disasterStatisticsRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        disasterStatisticsRepository.saveAll(List.of(
                DisasterStatistics.builder()
                        .alertId(101)
                        .totalReports(30)
                        .verifiedReports(20)
                        .totalHelpRequests(15)
                        .resolvedRequests(9)
                        .activeResponders(12)
                        .shelterOccupancy("{\"S1\":80,\"S2\":45}")
                        .avgResponseTimeMin(18.2)
                        .affectedAreaKm2(12.3)
                        .credibilityWeightedCount(25.4)
                        .computedAt(now.minusHours(4))
                        .build(),
                DisasterStatistics.builder()
                        .alertId(101)
                        .totalReports(40)
                        .verifiedReports(31)
                        .totalHelpRequests(22)
                        .resolvedRequests(16)
                        .activeResponders(19)
                        .shelterOccupancy("{\"S1\":88}")
                        .avgResponseTimeMin(21.0)
                        .affectedAreaKm2(15.0)
                        .credibilityWeightedCount(34.1)
                        .computedAt(now.minusHours(1))
                        .build(),
                DisasterStatistics.builder()
                        .alertId(202)
                        .totalReports(55)
                        .verifiedReports(38)
                        .totalHelpRequests(40)
                        .resolvedRequests(26)
                        .activeResponders(25)
                        .shelterOccupancy("{\"S3\":65,\"S4\":30}")
                        .avgResponseTimeMin(26.8)
                        .affectedAreaKm2(23.5)
                        .credibilityWeightedCount(42.9)
                        .computedAt(now.minusHours(2))
                        .build()
        ));
    }

    private void seedAuditLogs() {
        if (auditLogRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        auditLogRepository.saveAll(List.of(
                AuditLog.builder()
                        .actorId(42)
                        .action("UPDATE")
                        .entityType("DisasterStatistics")
                        .entityId(3)
                        .previousValue("{\"resolvedRequests\":15}")
                        .newValue("{\"resolvedRequests\":16}")
                        .ipAddress("127.0.0.1")
                        .userAgent("Swagger-UI")
                        .timestamp(now.minusMinutes(45))
                        .isMLGenerated(false)
                        .build(),
                AuditLog.builder()
                        .actorId(7)
                        .action("PREDICT")
                        .entityType("PredictiveModel")
                        .entityId(2)
                        .previousValue(null)
                        .newValue("{\"riskZone\":\"A2\",\"score\":0.81}")
                        .ipAddress("127.0.0.1")
                        .userAgent("System")
                        .timestamp(now.minusMinutes(20))
                        .isMLGenerated(true)
                        .mlModelVersion("spread-v1.2")
                        .build()
        ));
    }

    private void seedPredictiveModels() {
        if (predictiveModelRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        predictiveModelRepository.saveAll(List.of(
                PredictiveModel.builder()
                        .name("Spread V1")
                        .version("1.0.0")
                        .type(ModelType.SPREAD_PREDICTOR)
                        .artifactUrl("http://ml.local/models/spread-v1")
                        .metrics("{\"mae\":0.17,\"f1\":0.81}")
                        .isActive(true)
                        .trainedAt(now.minusDays(12))
                        .build(),
                PredictiveModel.builder()
                        .name("Credibility Score V2")
                        .version("2.1.0")
                        .type(ModelType.CREDIBILITY_SCORER)
                        .artifactUrl("http://ml.local/models/credibility-v2")
                        .metrics("{\"auc\":0.88}")
                        .isActive(true)
                        .trainedAt(now.minusDays(6))
                        .build(),
                PredictiveModel.builder()
                        .name("Spread Legacy")
                        .version("0.9.3")
                        .type(ModelType.SPREAD_PREDICTOR)
                        .artifactUrl("http://ml.local/models/spread-legacy")
                        .metrics("{\"mae\":0.23}")
                        .isActive(false)
                        .trainedAt(now.minusDays(30))
                        .build()
        ));
    }

    private void seedVolunteerStatistics() {
        if (volunteerStatisticsRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        volunteerStatisticsRepository.saveAll(List.of(
                VolunteerStatistics.builder()
                        .volunteerId(2001)
                        .totalApprovedHours(120.5)
                        .totalOpportunities(33)
                        .causeDistribution("{\"health\":40,\"food\":60}")
                        .skillsUtilised("[\"first_aid\",\"logistics\"]")
                        .avgRating(4.7)
                        .currentStreak(9)
                        .longestStreak(14)
                        .achievementCount(6)
                        .lastActivityDate(LocalDate.now().minusDays(3))
                        .churnProbability(0.21)
                        .computedAt(now.minusHours(3))
                        .build(),
                VolunteerStatistics.builder()
                        .volunteerId(2002)
                        .totalApprovedHours(85.0)
                        .totalOpportunities(21)
                        .causeDistribution("{\"shelter\":50,\"medical\":50}")
                        .skillsUtilised("[\"coordination\"]")
                        .avgRating(4.3)
                        .currentStreak(4)
                        .longestStreak(10)
                        .achievementCount(4)
                        .lastActivityDate(LocalDate.now().minusDays(6))
                        .churnProbability(0.56)
                        .computedAt(now.minusHours(2))
                        .build(),
                VolunteerStatistics.builder()
                        .volunteerId(2003)
                        .totalApprovedHours(42.0)
                        .totalOpportunities(12)
                        .causeDistribution("{\"food\":100}")
                        .skillsUtilised("[\"distribution\"]")
                        .avgRating(4.1)
                        .currentStreak(1)
                        .longestStreak(5)
                        .achievementCount(2)
                        .lastActivityDate(LocalDate.now().minusDays(14))
                        .churnProbability(0.82)
                        .computedAt(now.minusHours(1))
                        .build()
        ));
    }

    private void seedOrganisationStats() {
        if (organisationStatsRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        organisationStatsRepository.saveAll(List.of(
                OrganisationStats.builder()
                        .orgId(300)
                        .totalVolunteers(240)
                        .activeVolunteers(180)
                        .totalHoursDelivered(3520.0)
                        .retentionRate(0.74)
                        .avgRating(4.5)
                        .skillCoverage("{\"medical\":true,\"logistics\":true}")
                        .openPositions(12)
                        .computedAt(now.minusHours(2))
                        .build(),
                OrganisationStats.builder()
                        .orgId(301)
                        .totalVolunteers(120)
                        .activeVolunteers(84)
                        .totalHoursDelivered(1480.5)
                        .retentionRate(0.68)
                        .avgRating(4.2)
                        .skillCoverage("{\"food\":true,\"it\":false}")
                        .openPositions(7)
                        .computedAt(now.minusHours(1))
                        .build()
        ));
    }

    private void seedChurnPredictions() {
        if (churnPredictionRepository.count() > 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        churnPredictionRepository.saveAll(List.of(
                ChurnPrediction.builder()
                        .volunteerId(2001)
                        .predictedChurnProb(0.83)
                        .interventionType(InterventionType.TEAM_CHALLENGE)
                        .predictedAt(now.minusMinutes(55))
                        .reactivated(false)
                        .modelVersion("churn-xgb-1")
                        .build(),
                ChurnPrediction.builder()
                        .volunteerId(2002)
                        .predictedChurnProb(0.61)
                        .interventionType(InterventionType.OPPORTUNITY_REC)
                        .predictedAt(now.minusMinutes(25))
                        .reactivated(false)
                        .modelVersion("churn-xgb-1")
                        .build(),
                ChurnPrediction.builder()
                        .volunteerId(2003)
                        .predictedChurnProb(0.91)
                        .interventionType(InterventionType.ACHIEVEMENT_NUDGE)
                        .predictedAt(now.minusMinutes(10))
                        .reactivated(false)
                        .modelVersion("churn-xgb-2")
                        .build()
        ));
    }
}
