# Disaster Aid Backend - M13/M14 Analytics

This iteration starts implementation of:

- `M13`: Disaster Analytics & Reporting
- `M14`: Volunteer Analytics & Impact

## Implemented packages

- `tn.esprit.disasteraidbackend.analytics.m13`
- `tn.esprit.disasteraidbackend.analytics.m14`

Each module includes:

- JPA entities
- Spring Data repositories
- Services for aggregation
- REST controllers
- Integration tests

## Main endpoints

- `GET /api/analytics/disaster/report`
- `GET /api/analytics/disaster/stats`
- `GET /api/analytics/disaster/stats/alert/{alertId}`
- `GET /api/analytics/disaster/stats/alert/{alertId}/latest`
- `GET /api/analytics/disaster/audit-logs`
- `GET /api/analytics/disaster/predictive-models?activeOnly=true`

- `GET /api/analytics/volunteer/report?highRiskThreshold=0.70`
- `GET /api/analytics/volunteer/stats/{volunteerId}`
- `GET /api/analytics/volunteer/organisations/{orgId}`
- `GET /api/analytics/volunteer/churn/high-risk?threshold=0.70`
- `GET /api/analytics/volunteer/churn/volunteer/{volunteerId}`

## Local test setup

Tests use in-memory H2 with config in `src/test/resources/application.properties`.

## Try it

```powershell
.\mvnw.cmd test
.\mvnw.cmd spring-boot:run
```

