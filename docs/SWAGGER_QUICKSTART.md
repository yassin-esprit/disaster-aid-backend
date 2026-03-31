# Swagger Quickstart (minimal)

## 1) Ouvrir Swagger

Application sur port `8081` (config actuelle).

- Swagger UI: `http://localhost:8081/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8081/v3/api-docs`

---

## 2) Format de reponse API

Toutes les methodes retournent le format:

```json
{
  "success": true,
  "message": "OK",
  "data": {}
}
```

En cas de non trouve:

```json
{
  "success": false,
  "message": "No ... found ...",
  "data": null
}
```

---

## 3) Endpoints M13 (Disaster Analytics)

Base path: `/api/analytics/disaster`

### 3.1 GET `/report`
Exemple:

```json
{
  "success": true,
  "message": "OK",
  "data": {
    "totalReports": 125,
    "verifiedReports": 89,
    "totalHelpRequests": 77,
    "resolvedRequests": 51,
    "avgResponseTimeMin": 24.6,
    "auditEntries": 420,
    "activeModels": 2
  }
}
```

### 3.2 GET `/stats`
Exemple:

```json
{
  "success": true,
  "message": "OK",
  "data": [
    {
      "statId": 1,
      "alertId": 101,
      "totalReports": 30,
      "verifiedReports": 20,
      "totalHelpRequests": 15,
      "resolvedRequests": 9,
      "activeResponders": 12,
      "shelterOccupancy": "{\"S1\": 80, \"S2\": 45}",
      "avgResponseTimeMin": 18.2,
      "affectedAreaKm2": 12.3,
      "credibilityWeightedCount": 25.4,
      "computedAt": "2026-03-31T20:10:00"
    }
  ]
}
```

### 3.3 GET `/stats/alert/{alertId}`
Exemple `alertId=101`:

```json
{
  "success": true,
  "message": "OK",
  "data": [
    {
      "statId": 3,
      "alertId": 101,
      "totalReports": 40,
      "verifiedReports": 31,
      "totalHelpRequests": 22,
      "resolvedRequests": 16,
      "activeResponders": 19,
      "shelterOccupancy": "{\"S1\": 88}",
      "avgResponseTimeMin": 21.0,
      "affectedAreaKm2": 15.0,
      "credibilityWeightedCount": 34.1,
      "computedAt": "2026-03-31T20:30:00"
    }
  ]
}
```

### 3.4 GET `/stats/alert/{alertId}/latest`
Exemple succes:

```json
{
  "success": true,
  "message": "OK",
  "data": {
    "statId": 3,
    "alertId": 101,
    "totalReports": 40,
    "verifiedReports": 31,
    "totalHelpRequests": 22,
    "resolvedRequests": 16,
    "activeResponders": 19,
    "shelterOccupancy": "{\"S1\": 88}",
    "avgResponseTimeMin": 21.0,
    "affectedAreaKm2": 15.0,
    "credibilityWeightedCount": 34.1,
    "computedAt": "2026-03-31T20:30:00"
  }
}
```

Exemple non trouve:

```json
{
  "success": false,
  "message": "No statistics found for alertId=9999",
  "data": null
}
```

### 3.5 GET `/audit-logs`
Exemple:

```json
{
  "success": true,
  "message": "OK",
  "data": [
    {
      "logId": 1,
      "actorId": 42,
      "action": "UPDATE",
      "entityType": "DisasterStatistics",
      "entityId": 3,
      "previousValue": "{\"resolvedRequests\":15}",
      "newValue": "{\"resolvedRequests\":16}",
      "ipAddress": "127.0.0.1",
      "userAgent": "Swagger-UI",
      "timestamp": "2026-03-31T20:31:00",
      "isMLGenerated": false,
      "mlModelVersion": null
    }
  ]
}
```

### 3.6 GET `/predictive-models?activeOnly=true`
Exemple:

```json
{
  "success": true,
  "message": "OK",
  "data": [
    {
      "modelId": 10,
      "name": "Spread V1",
      "version": "1.0.0",
      "type": "SPREAD_PREDICTOR",
      "artifactUrl": "http://ml.local/models/spread-v1",
      "metrics": "{\"mae\":0.17,\"f1\":0.81}",
      "isActive": true,
      "trainedAt": "2026-03-20T09:00:00"
    }
  ]
}
```

### 3.7 POST `/add_model`
Body request (exemple):

```json
{
  "name": "Spread V2",
  "version": "2.0.0",
  "type": "SPREAD_PREDICTOR",
  "artifactUrl": "http://ml.local/models/spread-v2",
  "metrics": "{\"mae\":0.13,\"f1\":0.86}",
  "isActive": true,
  "trainedAt": "2026-03-31T20:00:00"
}
```

Response `201 Created` (exemple):

```json
{
  "success": true,
  "message": "Predictive model created",
  "data": {
    "modelId": 11,
    "name": "Spread V2",
    "version": "2.0.0",
    "type": "SPREAD_PREDICTOR",
    "artifactUrl": "http://ml.local/models/spread-v2",
    "metrics": "{\"mae\":0.13,\"f1\":0.86}",
    "isActive": true,
    "trainedAt": "2026-03-31T20:00:00"
  }
}
```

### 3.8 PUT `/modify_model?modelId={id}`
Body request (exemple):

```json
{
  "name": "Spread V2",
  "version": "2.0.1",
  "type": "SPREAD_PREDICTOR",
  "artifactUrl": "http://ml.local/models/spread-v2.0.1",
  "metrics": "{\"mae\":0.12,\"f1\":0.88}",
  "isActive": true,
  "trainedAt": "2026-03-31T21:00:00"
}
```

Response succes (exemple):

```json
{
  "success": true,
  "message": "Predictive model updated",
  "data": {
    "modelId": 11,
    "name": "Spread V2",
    "version": "2.0.1",
    "type": "SPREAD_PREDICTOR",
    "artifactUrl": "http://ml.local/models/spread-v2.0.1",
    "metrics": "{\"mae\":0.12,\"f1\":0.88}",
    "isActive": true,
    "trainedAt": "2026-03-31T21:00:00"
  }
}
```

Response non trouve (exemple):

```json
{
  "success": false,
  "message": "Predictive model not found for modelId=9999",
  "data": null
}
```

### 3.9 DELETE `/delete?modelId={id}`
Response succes (exemple):

```json
{
  "success": true,
  "message": "Predictive model deleted",
  "data": null
}
```

Response non trouve (exemple):

```json
{
  "success": false,
  "message": "Predictive model not found for modelId=9999",
  "data": null
}
```

---

## 4) Endpoints M14 (Volunteer Analytics)

Base path: `/api/analytics/volunteer`

### 4.1 GET `/report?highRiskThreshold=0.70`
Exemple:

```json
{
  "success": true,
  "message": "OK",
  "data": {
    "totalApprovedHours": 1580.5,
    "totalOpportunities": 430,
    "avgChurnProbability": 0.42,
    "highRiskVolunteers": 27,
    "organisationsTracked": 8
  }
}
```

### 4.2 GET `/stats/{volunteerId}`
Exemple succes:

```json
{
  "success": true,
  "message": "OK",
  "data": {
    "statId": 5,
    "volunteerId": 2001,
    "totalApprovedHours": 120.5,
    "totalOpportunities": 33,
    "causeDistribution": "{\"health\":40,\"food\":60}",
    "skillsUtilised": "[\"first_aid\",\"logistics\"]",
    "avgRating": 4.7,
    "currentStreak": 9,
    "longestStreak": 14,
    "achievementCount": 6,
    "lastActivityDate": "2026-03-28",
    "churnProbability": 0.21,
    "computedAt": "2026-03-31T18:00:00"
  }
}
```

Exemple non trouve:

```json
{
  "success": false,
  "message": "No volunteer statistics found for volunteerId=9999",
  "data": null
}
```

### 4.3 GET `/organisations/{orgId}`
Exemple:

```json
{
  "success": true,
  "message": "OK",
  "data": {
    "statId": 2,
    "orgId": 300,
    "totalVolunteers": 240,
    "activeVolunteers": 180,
    "totalHoursDelivered": 3520.0,
    "retentionRate": 0.74,
    "avgRating": 4.5,
    "skillCoverage": "{\"medical\":true,\"logistics\":true}",
    "openPositions": 12,
    "computedAt": "2026-03-31T19:10:00"
  }
}
```

### 4.4 GET `/churn/high-risk?threshold=0.70`
Exemple:

```json
{
  "success": true,
  "message": "OK",
  "data": [
    {
      "predictionId": 11,
      "volunteerId": 2001,
      "predictedChurnProb": 0.83,
      "interventionType": "TEAM_CHALLENGE",
      "predictedAt": "2026-03-31T17:30:00",
      "reactivated": false,
      "reactivatedAt": null,
      "modelVersion": "churn-xgb-1"
    }
  ]
}
```

### 4.5 GET `/churn/volunteer/{volunteerId}`
Exemple:

```json
{
  "success": true,
  "message": "OK",
  "data": {
    "predictionId": 11,
    "volunteerId": 2001,
    "predictedChurnProb": 0.83,
    "interventionType": "TEAM_CHALLENGE",
    "predictedAt": "2026-03-31T17:30:00",
    "reactivated": false,
    "reactivatedAt": null,
    "modelVersion": "churn-xgb-1"
  }
}
```

---

## 5) Workflow rapide pour tester les methodes (Swagger)

### Etape A - Lancer l'application

```powershell
cd C:\Users\21658\Documents\GitHub\disaster-aid-backend
.\mvnw.cmd spring-boot:run
```

### Etape B - Ouvrir Swagger

- `http://localhost:8081/swagger-ui/index.html`

### Etape C - Tester methodes de lecture globale
Ordre conseille:

1. `GET /api/analytics/disaster/report`
2. `GET /api/analytics/volunteer/report`
3. `GET /api/analytics/disaster/stats`
4. `GET /api/analytics/disaster/audit-logs`

### Etape D - Tester methodes par ID

Utiliser des IDs existants en base:

- `GET /api/analytics/disaster/stats/alert/{alertId}`
- `GET /api/analytics/disaster/stats/alert/{alertId}/latest`
- `GET /api/analytics/volunteer/stats/{volunteerId}`
- `GET /api/analytics/volunteer/organisations/{orgId}`
- `GET /api/analytics/volunteer/churn/volunteer/{volunteerId}`

### Etape E - Tester CRUD des modeles predictifs

Ordre conseille:

1. `POST /api/analytics/disaster/add_model`
2. `GET /api/analytics/disaster/predictive-models?activeOnly=false`
3. `PUT /api/analytics/disaster/modify_model?modelId={id}`
4. `DELETE /api/analytics/disaster/delete?modelId={id}`

### Etape F - Tester filtres query params

- `GET /api/analytics/disaster/predictive-models?activeOnly=true`
- `GET /api/analytics/volunteer/churn/high-risk?threshold=0.70`

### Etape G - Verifier les reponses

Check rapide:

- `success=true` et `data` non vide => OK
- `success=false` et message "No ... found ..." ou "Predictive model not found ..." => comportement attendu

---

## 6) Troubleshooting rapide

- Swagger n'ouvre pas:
  - verifier que l'app est demarree
  - verifier le port (`8081`) dans `application.properties`
- 500 erreur:
  - verifier MySQL/XAMPP en marche
  - verifier `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- data vide:
  - normal si aucune donnee n'a ete inseree dans les tables analytics

