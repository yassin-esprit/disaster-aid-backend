# M13 + M14 Implementation Documentation

## 1) Scope
This document explains what was implemented for:

- M13: Disaster Analytics & Reporting (DisasterAid)
- M14: Volunteer Analytics & Impact (VolunteerHub)

Goal: provide analytics and reporting layers from stored data (aggregation/analysis), with MySQL persistence and REST APIs.

---

## 2) High-level architecture
Pattern used:

- Controller layer: REST endpoints
- Service layer: business logic and KPI aggregation
- Repository layer: JPA data access
- Entity layer: JPA tables for analytics modules
- DTO layer: API output models for dashboard/report views

Main app entrypoint:

- `src/main/java/tn/esprit/disasteraidbackend/DisasterAidBackendApplication.java`

Common reusable classes:

- `src/main/java/tn/esprit/disasteraidbackend/common/ApiResponse.java`
- `src/main/java/tn/esprit/disasteraidbackend/common/BaseEntity.java`

---

## 3) What was implemented (M13)

### 3.1 Entities
Package: `src/main/java/tn/esprit/disasteraidbackend/analytics/m13/entity`

Implemented:

- `DisasterStatistics`
  - Stores computed disaster KPIs by `alertId`
  - Fields: total reports, verified reports, requests/resolutions, avg response time, area, credibility-weighted metrics, computed timestamp
- `AuditLog`
  - Immutable-style audit trail model (write once semantics by design)
  - Tracks actor, action, entity type/id, values diff, metadata, timestamp, ML origin
- `PredictiveModel`
  - Registry of ML models
  - Fields: name, version, type, metrics, artifact URL, active flag, trained timestamp
- `ModelType` enum
  - Model categories (spread predictor, churn predictor, etc.)

### 3.2 Repositories
Package: `src/main/java/tn/esprit/disasteraidbackend/analytics/m13/repository`

Implemented:

- `DisasterStatisticsRepository`
  - Find by alert id
  - Latest statistic per alert
  - Global aggregated KPI query (`aggregateGlobalKpis`)
- `AuditLogRepository`
  - Latest 100 logs
  - Count by actor
- `PredictiveModelRepository`
  - Query by model type
  - Query active models
  - Count active models

### 3.3 Service
Package: `src/main/java/tn/esprit/disasteraidbackend/analytics/m13/service`

- `DisasterAnalyticsService`
  - Builds global disaster analytics report
  - Exposes read methods for stats, audit logs, and predictive models
  - Uses explicit constructor injection (no Lombok requirement)

### 3.4 DTO
Package: `src/main/java/tn/esprit/disasteraidbackend/analytics/m13/dto`

- `DisasterAnalyticsReportDto`
  - Final fields + explicit constructor + getters
  - Removed Lombok builder dependency for IDE-safe compilation

### 3.5 Controller
Package: `src/main/java/tn/esprit/disasteraidbackend/analytics/m13/controller`

- `DisasterAnalyticsController`
  - Base path: `/api/analytics/disaster`
  - Endpoints:
    - `GET /report`
    - `GET /stats`
    - `GET /stats/alert/{alertId}`
    - `GET /stats/alert/{alertId}/latest`
    - `GET /audit-logs`
    - `GET /predictive-models?activeOnly=true|false`
  - Returns standardized `ApiResponse`

---

## 4) What was implemented (M14)

### 4.1 Entities
Package: `src/main/java/tn/esprit/disasteraidbackend/analytics/m14/entity`

Implemented:

- `VolunteerStatistics`
  - Personal volunteer metrics (hours, opportunities, streaks, churn probability, etc.)
- `OrganisationStats`
  - NGO/organization metrics (volunteers, retention, delivered hours, open positions)
- `ChurnPrediction`
  - Churn score + intervention recommendation + reactivation tracking
- `InterventionType` enum
  - Suggested intervention categories

### 4.2 Repositories
Package: `src/main/java/tn/esprit/disasteraidbackend/analytics/m14/repository`

Implemented:

- `VolunteerStatisticsRepository`
  - Latest stats by volunteer
  - Global KPI aggregation (`aggregateGlobalKpis`)
- `OrganisationStatsRepository`
  - Latest stats by organization
- `ChurnPredictionRepository`
  - High-risk predictions by threshold
  - Latest prediction by volunteer

### 4.3 Service
Package: `src/main/java/tn/esprit/disasteraidbackend/analytics/m14/service`

- `VolunteerAnalyticsService`
  - Builds volunteer global report
  - Fetches latest volunteer/org stats
  - Fetches high-risk churn predictions
  - Uses explicit constructor injection

### 4.4 DTO
Package: `src/main/java/tn/esprit/disasteraidbackend/analytics/m14/dto`

- `VolunteerAnalyticsReportDto`
  - Final fields + explicit constructor + getters
  - Removed Lombok builder dependency for compatibility

### 4.5 Controller
Package: `src/main/java/tn/esprit/disasteraidbackend/analytics/m14/controller`

- `VolunteerAnalyticsController`
  - Base path: `/api/analytics/volunteer`
  - Endpoints:
    - `GET /report?highRiskThreshold=0.70`
    - `GET /stats/{volunteerId}`
    - `GET /organisations/{orgId}`
    - `GET /churn/high-risk?threshold=0.70`
    - `GET /churn/volunteer/{volunteerId}`
  - Returns standardized `ApiResponse`

---

## 5) Platform configuration (MySQL / XAMPP)
Main config file:

- `src/main/resources/application.properties`

Current important properties:

- `server.port=${SERVER_PORT:8081}`
- `spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/disaster_aid?...}`
- `spring.datasource.username=${DB_USERNAME:root}`
- `spring.datasource.password=${DB_PASSWORD:}`
- `spring.jpa.hibernate.ddl-auto=update`
- SQL logging enabled

Notes:

- Default server port changed to `8081` to avoid collision on `8080`.
- Database startup and schema creation worked with MySQL (observed from run logs).

---

## 6) Stability fixes applied
Main issues encountered and fixes:

1. Lombok-generated constructors/getters not always available in IDE run path
   - Symptoms: `variable ... not initialized in the default constructor`
   - Fix: replace Lombok constructor dependency with explicit constructors in services/controllers

2. DTO builder not resolved
   - Symptom: `cannot find symbol method builder()`
   - Fix: remove Lombok builder usage and instantiate DTOs with explicit constructors

3. Generic inference issue in `ApiResponse`
   - Symptom: `cannot infer type arguments for ApiResponse<>`
   - Fix: implement explicit constructor/getters in `ApiResponse` and keep static factory methods

Key files hardened:

- `src/main/java/tn/esprit/disasteraidbackend/common/ApiResponse.java`
- `src/main/java/tn/esprit/disasteraidbackend/analytics/m13/controller/DisasterAnalyticsController.java`
- `src/main/java/tn/esprit/disasteraidbackend/analytics/m13/service/DisasterAnalyticsService.java`
- `src/main/java/tn/esprit/disasteraidbackend/analytics/m13/dto/DisasterAnalyticsReportDto.java`
- `src/main/java/tn/esprit/disasteraidbackend/analytics/m14/controller/VolunteerAnalyticsController.java`
- `src/main/java/tn/esprit/disasteraidbackend/analytics/m14/service/VolunteerAnalyticsService.java`
- `src/main/java/tn/esprit/disasteraidbackend/analytics/m14/dto/VolunteerAnalyticsReportDto.java`

---

## 7) Tests
Test files:

- `src/test/java/tn/esprit/disasteraidbackend/DisasterAidBackendApplicationTests.java`
- `src/test/java/tn/esprit/disasteraidbackend/analytics/m13/DisasterAnalyticsServiceIntegrationTest.java`
- `src/test/java/tn/esprit/disasteraidbackend/analytics/m14/VolunteerAnalyticsServiceIntegrationTest.java`

Execution used:

```powershell
C:\Users\21658\Documents\GitHub\disaster-aid-backend\mvnw.cmd test -q
```

Current state from recent runs: tests pass.

---

## 8) API quick check
After app startup (default `8081`):

```powershell
curl http://localhost:8081/api/analytics/disaster/report
curl http://localhost:8081/api/analytics/volunteer/report
```

---

## 9) Known limitations and next steps
Current limitations:

- Analytics tables are ready, but no ingestion pipeline from upstream modules yet.
- Some entities still use Lombok annotations (`entity/common` level), which is acceptable but can be fully hardened if needed.
- `hibernate.dialect` explicit property can be removed (Hibernate can auto-detect).

Recommended next steps:

1. Add write endpoints or scheduled jobs to refresh analytics snapshots.
2. Add DB indexes for high-frequency filters (ex: `alert_id`, `volunteer_id`, `org_id`, `predicted_at`).
3. Add integration tests against real MySQL test schema.
4. Add pagination/filter DTOs for audit logs and high-risk prediction endpoints.
5. Add OpenAPI documentation for analytics endpoints.

---

## 10) Summary
M13 and M14 analytics foundations are implemented end-to-end:

- data model
- repositories
- services
- REST controllers
- MySQL runtime configuration
- stability fixes for IDE/runtime compilation paths

The backend is now ready for data feeding from other modules and dashboard consumption.

