# Rapport de travail - Modules M13 et M14

## 1) Contexte
Dans ce projet Spring Boot `disaster-aid-backend`, ma responsabilite couvre la couche intelligence/analytics:

- **M13 (Disaster Analytics & Reporting)**: produire des indicateurs sur les catastrophes.
- **M14 (Volunteer Analytics & Impact)**: analyser la performance des benevoles et predire le churn.

Important: je ne cree pas les donnees metier source, je les **aggrege et analyse** pour fournir des tableaux de bord exploitables.

---

## 2) Objectifs fonctionnels realises

### M13 - Cote DisasterAid
- Suivre les statistiques par alerte/catastrophe.
- Exposer des rapports globaux (volume, resolution, temps moyen).
- Journaliser les actions via AuditLog.
- Referencer les modeles ML utilises en production.

### M14 - Cote VolunteerHub
- Calculer les statistiques individuelles des benevoles.
- Suivre les indicateurs organisationnels (ONG).
- Detecter les benevoles a risque de desengagement (churn).
- Exposer les recommandations d intervention.

---

## 3) Travail technique implemente

## 3.1 Entites JPA (base MySQL)

### M13
- `DisasterStatistics`
- `AuditLog`
- `PredictiveModel`
- `ModelType` (enum)

### M14
- `VolunteerStatistics`
- `OrganisationStats`
- `ChurnPrediction`
- `InterventionType` (enum)

Ces entites sont dans:
- `src/main/java/tn/esprit/disasteraidbackend/analytics/m13/entity`
- `src/main/java/tn/esprit/disasteraidbackend/analytics/m14/entity`

## 3.2 Repositories
J ai implemente les interfaces JPA pour lecture/filtrage/aggregation:

- M13: `DisasterStatisticsRepository`, `AuditLogRepository`, `PredictiveModelRepository`
- M14: `VolunteerStatisticsRepository`, `OrganisationStatsRepository`, `ChurnPredictionRepository`

## 3.3 Services
J ai implemente la logique metier dans:

- `DisasterAnalyticsService`
- `VolunteerAnalyticsService`

Points cle:
- aggregation KPI globale,
- recuperation des dernieres stats par identifiant,
- filtres (modeles actifs, seuil churn),
- construction des DTO de rapport.

## 3.4 Controllers REST
API exposees:

- `DisasterAnalyticsController` (`/api/analytics/disaster`)
- `VolunteerAnalyticsController` (`/api/analytics/volunteer`)

Toutes les reponses utilisent un format standard via:
- `src/main/java/tn/esprit/disasteraidbackend/common/ApiResponse.java`

---

## 4) Endpoints principaux livres

### M13
- `GET /api/analytics/disaster/report`
- `GET /api/analytics/disaster/stats`
- `GET /api/analytics/disaster/stats/alert/{alertId}`
- `GET /api/analytics/disaster/stats/alert/{alertId}/latest`
- `GET /api/analytics/disaster/audit-logs`
- `GET /api/analytics/disaster/predictive-models?activeOnly=true|false`

### M14
- `GET /api/analytics/volunteer/report?highRiskThreshold=0.70`
- `GET /api/analytics/volunteer/stats/{volunteerId}`
- `GET /api/analytics/volunteer/organisations/{orgId}`
- `GET /api/analytics/volunteer/churn/high-risk?threshold=0.70`
- `GET /api/analytics/volunteer/churn/volunteer/{volunteerId}`

---

## 5) Environnement et base de donnees

Le projet est configure pour MySQL (XAMPP) sans Docker:

- Port application: `8081`
- URL DB par defaut: `jdbc:mysql://localhost:3306/disaster_aid?...`
- User par defaut: `root`
- `spring.jpa.hibernate.ddl-auto=update`

Fichier de configuration:
- `src/main/resources/application.properties`

Au demarrage, les tables analytics sont creees/mises a jour automatiquement.

---

## 6) Problemes rencontres et corrections appliquees

Pendant l implementation, plusieurs erreurs de compilation/boot ont ete corrigees:

1. **Injection Spring non initialisee** (`... not initialized in the default constructor`)
   - Correction: injection par constructeur explicite dans services/controllers.

2. **DTO builder introuvable** (`cannot find symbol method builder()`)
   - Correction: suppression dependance builder Lombok dans les DTO reports; utilisation de constructeurs explicites.

3. **Erreur de generics ApiResponse** (`cannot infer type arguments`)
   - Correction: consolidation du constructeur et des methodes utilitaires de `ApiResponse`.

4. **Conflit de port** (`Port 8080 was already in use`)
   - Correction: execution sur `8081`.

5. **Swagger 404 sur `/swagger-ui.html`**
   - Route correcte utilisee: `/swagger-ui/index.html` (selon la version springdoc).

---

## 7) Validation effectuee

- Demarrage applicatif valide avec MySQL actif.
- Scans JPA OK et creation des tables analytics observee.
- Endpoints M13/M14 exposent des reponses structurees.
- Documentation Swagger rapide ajoutee dans:
  - `docs/SWAGGER_QUICKSTART.md`

---

## 8) Valeur ajoutee de mon travail

- Mise en place complete d une couche analytics modulable.
- API claires pour dashboard et reporting.
- Base solide pour branchement futur de pipelines ML/realtime.
- Structure maintenable (entity -> repository -> service -> controller).

---

## 9) Limites actuelles et suite proposee

Limites:
- Le module lit surtout des snapshots analytics; peu de mecanismes automatiques de recalcul periodique.
- Donnees de demo a inserer selon les besoins de test.

Etapes suivantes proposees:
1. Ajouter un seed SQL automatique pour jeux de donnees de demonstration.
2. Ajouter pagination/filtres avances (audit logs, churn).
3. Renforcer la couverture de tests d integration API + DB reelle.
4. Ajouter des index SQL sur les champs les plus filtres (`alert_id`, `volunteer_id`, `org_id`).

---

## 10) Conclusion
Le socle M13 + M14 est implemente de bout en bout sur Spring Boot + MySQL:

- modeles de donnees,
- repositories,
- services analytics,
- controllers REST,
- documentation de test via Swagger.

Ce travail rend le backend exploitable pour le suivi des catastrophes et l impact benevole, avec une base propre pour l evolution future.

