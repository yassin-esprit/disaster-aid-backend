# 🎯 Disaster Aid Backend - DÉMONSTRATION COMPLÈTE

## Objectif
Présenter un système de **reporting analytique** pour une plateforme d'aide aux catastrophes avec **2 modules avancés** :
- **M13** : Disaster Analytics & Reporting (Analyse des catastrophes)
- **M14** : Volunteer Analytics & Impact (Analyse de l'impact volontaire)

---

## 📋 Plan de la Démonstration

### 1. Architecture générale
### 2. Démarrage de l'application
### 3. Démonstration M13 (Disaster Analytics)
### 4. Démonstration M14 (Volunteer Analytics)
### 5. Exécution des tests
### 6. Points clés à mettre en avant

---

## 1️⃣ ARCHITECTURE GÉNÉRALE

### Schéma d'architecture
```
┌─────────────────────────────────────────────────────────┐
│                   REST Controllers                       │
│  (/api/analytics/disaster)  (/api/analytics/volunteer)  │
└──────────────────┬──────────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────────┐
│                 Service Layer                           │
│  DisasterAnalyticsService  VolunteerAnalyticsService   │
│  - Business Logic                                       │
│  - KPI Aggregation                                      │
│  - Data Transformation                                  │
└──────────────────┬──────────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────────┐
│              Repository Layer (JPA)                     │
│  - DisasterStatisticsRepository                         │
│  - AuditLogRepository                                   │
│  - VolunteerStatisticsRepository                        │
│  - ChurnPredictionRepository                            │
└──────────────────┬──────────────────────────────────────┘
                   │
┌──────────────────▼──────────────────────────────────────┐
│                  MySQL Database                         │
│  Tables: disaster_statistics, audit_logs,              │
│          volunteer_statistics, churn_prediction        │
└─────────────────────────────────────────────────────────┘
```

### Stack Technologique
```
✓ Java 17
✓ Spring Boot 4.0.4
✓ Spring Data JPA + Hibernate
✓ MySQL
✓ Maven
✓ JUnit 5 + Mockito (Tests)
✓ OpenAPI/Swagger
```

---

## 2️⃣ DÉMARRAGE DE L'APPLICATION

### Étape 1 : Vérifier les prérequis
```powershell
# Vérifier Java 17
java -version

# Vérifier MySQL est running (XAMPP/MySQL Community)
# Base de données: disaster_aid
# Utilisateur: root
# Mot de passe: (vide par défaut)
```

### Étape 2 : Démarrer l'application
```powershell
# Naviguer au répertoire du projet
cd C:\Users\21658\Documents\GitHub\disaster-aid-backend

# Compiler et démarrer
.\mvnw.cmd spring-boot:run
```

### Output attendu
```
...
[INFO] --- spring-boot:4.0.4:run (default-cli) > test-compile @ disaster-aid-backend ---
[INFO] Building disaster-aid-backend 0.0.1-SNAPSHOT
[INFO]
[INFO] >>> spring-boot:4.0.4:run (default-cli) > test-compile @ disaster-aid-backend >>>
...
o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat started on port(s): 8081 (http)
o.s.b.a.DisasterAidBackendApplication : Started DisasterAidBackendApplication in 3.456 seconds
```

### ✅ Application prête
- URL : **http://localhost:8081**
- Swagger UI : **http://localhost:8081/swagger-ui.html**

---

## 3️⃣ DÉMONSTRATION M13 - DISASTER ANALYTICS

### 📊 Module M13 : Analyse des catastrophes et reporting

#### Entités principales
- **DisasterStatistics** : Métriques de catastrophes (rapports totaux, vérifiés, temps de réponse, etc.)
- **AuditLog** : Journal d'audit immutable des actions
- **PredictiveModel** : Registre des modèles ML actifs

#### Données de test pré-chargées
```sql
-- Via data.sql
INSERT INTO disaster_statistics (alert_id, total_reports, verified_reports, avg_response_time, ...)
INSERT INTO audit_logs (actor, action, entity_type, ...)
INSERT INTO predictive_models (name, model_type, accuracy_score, is_active, ...)
```

---

### 🔍 Endpoint 1 : Rapport global des catastrophes
```
GET /api/analytics/disaster/report
```

**Commande cURL :**
```powershell
curl -X GET "http://localhost:8081/api/analytics/disaster/report" `
  -H "Content-Type: application/json"
```

**Réponse attendue (JSON) :**
```json
{
  "success": true,
  "message": "Disaster analytics report retrieved successfully",
  "data": {
    "totalAlertsAnalyzed": 5,
    "totalReports": 1250,
    "verifiedReports": 1100,
    "avgResponseTime": 35.5,
    "avgCredibilityScore": 0.92,
    "avgCoverageArea": 45.3,
    "requestsResolved": 980,
    "resolutionRate": 0.784,
    "generatedAt": "2026-04-16T10:30:00Z"
  },
  "timestamp": "2026-04-16T10:30:05Z"
}
```

**🎯 Ce que montre cet endpoint :**
- Agrégation de données de **5 catastrophes** dans la BD
- Calcul de KPIs critiques
- Taux de résolution des demandes d'aide

---

### 🔍 Endpoint 2 : Statistiques globales
```
GET /api/analytics/disaster/stats
```

**Commande cURL :**
```powershell
curl -X GET "http://localhost:8081/api/analytics/disaster/stats"
```

**Réponse attendue :**
```json
{
  "success": true,
  "message": "Disaster statistics retrieved successfully",
  "data": [
    {
      "id": 1,
      "alertId": 101,
      "totalReports": 250,
      "verifiedReports": 220,
      "avgResponseTime": 32.5,
      "credibilityScore": 0.93,
      "coverageArea": 50.5,
      "predictedAt": "2026-04-15T14:00:00Z"
    },
    {
      "id": 2,
      "alertId": 102,
      "totalReports": 180,
      "verifiedReports": 165,
      "avgResponseTime": 28.3,
      "credibilityScore": 0.91,
      "coverageArea": 42.1,
      "predictedAt": "2026-04-15T14:05:00Z"
    }
  ],
  "timestamp": "2026-04-16T10:30:05Z"
}
```

---

### 🔍 Endpoint 3 : Statistiques par alerte
```
GET /api/analytics/disaster/stats/alert/101
```

**Commande cURL :**
```powershell
curl -X GET "http://localhost:8081/api/analytics/disaster/stats/alert/101"
```

**Réponse attendue :**
```json
{
  "success": true,
  "message": "Disaster statistics retrieved successfully",
  "data": {
    "id": 1,
    "alertId": 101,
    "totalReports": 250,
    "verifiedReports": 220,
    "requestsCreated": 210,
    "requestsResolved": 200,
    "avgResponseTime": 32.5,
    "credibilityScore": 0.93,
    "coverageArea": 50.5,
    "predictedAt": "2026-04-15T14:00:00Z"
  },
  "timestamp": "2026-04-16T10:30:05Z"
}
```

---

### 🔍 Endpoint 4 : Dernière statistique d'une alerte
```
GET /api/analytics/disaster/stats/alert/101/latest
```

**Commande cURL :**
```powershell
curl -X GET "http://localhost:8081/api/analytics/disaster/stats/alert/101/latest"
```

**Réponse :** (Même structure que Endpoint 3)

---

### 🔍 Endpoint 5 : Logs d'audit
```
GET /api/analytics/disaster/audit-logs
```

**Commande cURL :**
```powershell
curl -X GET "http://localhost:8081/api/analytics/disaster/audit-logs"
```

**Réponse attendue :**
```json
{
  "success": true,
  "message": "Audit logs retrieved successfully",
  "data": [
    {
      "id": 1,
      "actor": "system",
      "action": "DISASTER_CREATED",
      "entityType": "ALERT",
      "entityId": 101,
      "valuesBefore": null,
      "valuesAfter": "{\"severity\": \"HIGH\", \"status\": \"ACTIVE\"}",
      "metadata": "Alerte créée automatiquement",
      "isFromMl": false,
      "createdAt": "2026-04-15T12:00:00Z"
    },
    {
      "id": 2,
      "actor": "analytics_engine",
      "action": "STATISTICS_COMPUTED",
      "entityType": "DISASTER_STATISTICS",
      "entityId": 1,
      "valuesBefore": null,
      "valuesAfter": "{\"totalReports\": 250}",
      "metadata": "Statistiques calculées par ML",
      "isFromMl": true,
      "createdAt": "2026-04-15T14:00:00Z"
    }
  ],
  "timestamp": "2026-04-16T10:30:05Z"
}
```

**🎯 Ce que montre cet endpoint :**
- Traçabilité complète des actions
- Audit trail immutable
- Traçage des actions générées par ML

---

### 🔍 Endpoint 6 : Modèles prédictifs actifs
```
GET /api/analytics/disaster/predictive-models?activeOnly=true
```

**Commande cURL :**
```powershell
curl -X GET "http://localhost:8081/api/analytics/disaster/predictive-models?activeOnly=true"
```

**Réponse attendue :**
```json
{
  "success": true,
  "message": "Predictive models retrieved successfully",
  "data": [
    {
      "id": 1,
      "name": "Spread Predictor v2.1",
      "version": "2.1",
      "modelType": "SPREAD_PREDICTOR",
      "accuracyScore": 0.96,
      "recallScore": 0.94,
      "precisionScore": 0.97,
      "artifactUrl": "s3://models/spread-predictor-v2.1.pkl",
      "isActive": true,
      "trainedAt": "2026-03-01T00:00:00Z"
    },
    {
      "id": 3,
      "name": "Resource Optimizer v1.5",
      "version": "1.5",
      "modelType": "RESOURCE_OPTIMIZER",
      "accuracyScore": 0.88,
      "recallScore": 0.90,
      "precisionScore": 0.85,
      "artifactUrl": "s3://models/resource-optimizer-v1.5.pkl",
      "isActive": true,
      "trainedAt": "2026-02-15T00:00:00Z"
    }
  ],
  "timestamp": "2026-04-16T10:30:05Z"
}
```

**🎯 Ce que montre cet endpoint :**
- Registre des modèles ML en production
- Métriques d'accuracy/recall/precision
- Versions et dates d'entraînement

---

## 4️⃣ DÉMONSTRATION M14 - VOLUNTEER ANALYTICS

### 👥 Module M14 : Analyse de l'impact volontaire

#### Entités principales
- **VolunteerStatistics** : Métriques individuelles (heures travaillées, opportunities, churn risk)
- **OrganisationStats** : Métriques d'organisations (volontaires, rétention)
- **ChurnPrediction** : Prédiction de départ + recommandations d'intervention

---

### 🔍 Endpoint 1 : Rapport global des volontaires
```
GET /api/analytics/volunteer/report?highRiskThreshold=0.70
```

**Commande cURL :**
```powershell
curl -X GET "http://localhost:8081/api/analytics/volunteer/report?highRiskThreshold=0.70" `
  -H "Content-Type: application/json"
```

**Réponse attendue :**
```json
{
  "success": true,
  "message": "Volunteer analytics report retrieved successfully",
  "data": {
    "totalApprovedHours": 1250.5,
    "totalOpportunities": 45,
    "avgChurnProbability": 0.35,
    "highRiskVolunteers": 8,
    "organisationsTracked": 12,
    "totalVolunteers": 95,
    "generatedAt": "2026-04-16T10:30:00Z"
  },
  "timestamp": "2026-04-16T10:30:05Z"
}
```

**🎯 Ce que montre cet endpoint :**
- **1250.5 heures** travaillées par les volontaires
- **45 opportunities** disponibles
- **8 volontaires à haut risque** (départ probable > 70%)
- **12 organisations** suivies

---

### 🔍 Endpoint 2 : Statistiques d'un volontaire
```
GET /api/analytics/volunteer/stats/42
```

**Commande cURL :**
```powershell
curl -X GET "http://localhost:8081/api/analytics/volunteer/stats/42"
```

**Réponse attendue :**
```json
{
  "success": true,
  "message": "Volunteer statistics retrieved successfully",
  "data": {
    "id": 5,
    "volunteerId": 42,
    "approvedHours": 125.5,
    "totalOpportunities": 6,
    "completedOpportunities": 5,
    "currentStreak": 3,
    "longestStreak": 8,
    "churnProbability": 0.42,
    "predictedAt": "2026-04-15T15:00:00Z"
  },
  "timestamp": "2026-04-16T10:30:05Z"
}
```

**🎯 Ce que montre cet endpoint :**
- Volontaire a complété **125.5 heures**
- **Streak courant de 3 semaines** (engagement actif)
- **Probabilité de départ: 42%** (risque modéré)

---

### 🔍 Endpoint 3 : Statistiques d'une organisation
```
GET /api/analytics/volunteer/organisations/7
```

**Commande cURL :**
```powershell
curl -X GET "http://localhost:8081/api/analytics/volunteer/organisations/7"
```

**Réponse attendue :**
```json
{
  "success": true,
  "message": "Organisation statistics retrieved successfully",
  "data": {
    "id": 3,
    "organisationId": 7,
    "totalVolunteers": 12,
    "activeVolunteers": 10,
    "retentionRate": 0.95,
    "totalDeliveredHours": 450.5,
    "avgHoursPerVolunteer": 37.5,
    "openPositions": 2,
    "computedAt": "2026-04-15T16:00:00Z"
  },
  "timestamp": "2026-04-16T10:30:05Z"
}
```

**🎯 Ce que montre cet endpoint :**
- Organisation a **12 volontaires**
- **95% taux de rétention** (excellent)
- **450.5 heures** livrées
- **2 postes ouverts**

---

### 🔍 Endpoint 4 : Volontaires à haut risque
```
GET /api/analytics/volunteer/churn/high-risk?threshold=0.70
```

**Commande cURL :**
```powershell
curl -X GET "http://localhost:8081/api/analytics/volunteer/churn/high-risk?threshold=0.70"
```

**Réponse attendue :**
```json
{
  "success": true,
  "message": "High-risk churn predictions retrieved successfully",
  "data": [
    {
      "id": 1,
      "volunteerId": 15,
      "predictedChurnProb": 0.85,
      "interventionType": "SCHEDULE_INTERVIEW",
      "recommendedAction": "Rencontrer rapidement le volontaire pour discuter de ses préoccupations",
      "isReactivated": false,
      "reactivatedAt": null,
      "predictedAt": "2026-04-14T10:00:00Z"
    },
    {
      "id": 2,
      "volunteerId": 28,
      "predictedChurnProb": 0.72,
      "interventionType": "PERSONALIZED_OPPORTUNITY",
      "recommendedAction": "Proposer une opportunité adaptée aux intérêts du volontaire",
      "isReactivated": false,
      "reactivatedAt": null,
      "predictedAt": "2026-04-14T11:00:00Z"
    }
  ],
  "timestamp": "2026-04-16T10:30:05Z"
}
```

**🎯 Ce que montre cet endpoint :**
- **2 volontaires** risquent de partir (> 70%)
- **Recommandations actionables** pour chaque cas
- Actions: entretien, opportunité personnalisée, etc.

---

### 🔍 Endpoint 5 : Prédiction de départ pour un volontaire
```
GET /api/analytics/volunteer/churn/volunteer/15
```

**Commande cURL :**
```powershell
curl -X GET "http://localhost:8081/api/analytics/volunteer/churn/volunteer/15"
```

**Réponse attendue :**
```json
{
  "success": true,
  "message": "Churn prediction retrieved successfully",
  "data": {
    "id": 1,
    "volunteerId": 15,
    "predictedChurnProb": 0.85,
    "interventionType": "SCHEDULE_INTERVIEW",
    "recommendedAction": "Rencontrer rapidement le volontaire pour discuter de ses préoccupations",
    "isReactivated": false,
    "reactivatedAt": null,
    "predictedAt": "2026-04-14T10:00:00Z"
  },
  "timestamp": "2026-04-16T10:30:05Z"
}
```

---

## 5️⃣ EXÉCUTION DES TESTS

### 🧪 Test M13 : DisasterAnalyticsServiceIntegrationTest
```powershell
# Exécuter les tests M13
.\mvnw.cmd test -Dtest=DisasterAnalyticsServiceIntegrationTest -q
```

**Output attendu :**
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running tn.esprit.disasteraidbackend.analytics.m13.DisasterAnalyticsServiceIntegrationTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.234 s
[INFO] -------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] -------------------------------------------------------
```

**Ce que teste ce test :**
- Agrégation correcte des statistiques de catastrophes
- Calcul correct des KPIs globaux
- Récupération des modèles prédictifs

---

### 🧪 Test M14 : VolunteerAnalyticsServiceIntegrationTest
```powershell
# Exécuter les tests M14
.\mvnw.cmd test -Dtest=VolunteerAnalyticsServiceIntegrationTest -q
```

**Output attendu :**
```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running tn.esprit.disasteraidbackend.analytics.m14.VolunteerAnalyticsServiceIntegrationTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.987 s
[INFO] -------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] -------------------------------------------------------
```

**Ce que teste ce test :**
- Agrégation des statistiques de volontaires
- Identification correct des volontaires à haut risque
- Calcul des métriques d'impact

---

### 🧪 Exécuter tous les tests
```powershell
# Tous les tests du projet
.\mvnw.cmd test -q

# Avec verbosité
.\mvnw.cmd test
```

---

## 6️⃣ POINTS CLÉS À METTRE EN AVANT

### ✨ Architecture & Design

#### 1. **Séparation des couches** 
```
Controller (REST API)
    ↓
Service (Business Logic)
    ↓
Repository (Data Access)
    ↓
Entity (Database Model)
```
- **Avantage :** Testabilité, maintenabilité, scalabilité

#### 2. **Pattern de réponse standardisé**
```java
ApiResponse<DisasterAnalyticsReportDto> {
  - success: boolean
  - message: String
  - data: T
  - timestamp: LocalDateTime
}
```
- **Avantage :** Cohérence API, gestion d'erreurs uniforme

#### 3. **Agrégation de données efficace**
Requêtes JPA custom pour calculer KPIs directement en base :
```java
@Query("SELECT SUM(d.totalReports), SUM(d.verifiedReports), AVG(d.credibilityScore) FROM DisasterStatistics d")
List<Object[]> aggregateGlobalKpis();
```
- **Avantage :** Performance (pas de traitement Java), réduction mémoire

---

### 🎯 Cas d'usage pratiques

#### M13 - Dashboard catastrophes
```
Chef d'alerte → Visualise rapport global
              → Consulte statistiques par alerte
              → Vérifie score de crédibilité
              → Valide modèles ML en production
```

#### M14 - Gestion des volontaires
```
Manager ONG   → Voit impact global (heures, opportunities)
              → Identifie volontaires à risque (churn > 70%)
              → Reçoit recommandations d'intervention
              → Suit le taux de rétention par org
```

---

### 💡 Points techniques importants

#### 1. **Constructeurs explicites (pas de Lombok builder)**
```java
// ✅ Production-safe
public DisasterAnalyticsReportDto(int totalAlerts, ...) {
    this.totalAlerts = totalAlerts;
    ...
}

// ❌ Évité (instabilité IDE)
@Builder
public class DisasterAnalyticsReportDto { ... }
```

#### 2. **Injection de dépendances via constructeur**
```java
@Service
public class DisasterAnalyticsService {
    private final DisasterStatisticsRepository repo;
    
    public DisasterAnalyticsService(DisasterStatisticsRepository repo) {
        this.repo = repo;  // ✅ Testable
    }
}
```

#### 3. **Tests avec Mockito**
```java
@Mock DisasterStatisticsRepository repo;
@InjectMocks DisasterAnalyticsService service;

@Test
void test() {
    when(repo.aggregateGlobalKpis()).thenReturn(...);
    // Test comportement isolé
}
```

---

### 📊 Résumé des livrables

| Composant | M13 | M14 |
|-----------|-----|-----|
| **Entités** | 3 (DisasterStatistics, AuditLog, PredictiveModel) | 3 (VolunteerStatistics, OrganisationStats, ChurnPrediction) |
| **Repositories** | 3 avec requêtes JPA custom | 3 avec agrégation |
| **Services** | 1 (DisasterAnalyticsService) | 1 (VolunteerAnalyticsService) |
| **DTOs** | 1 (DisasterAnalyticsReportDto) | 1 (VolunteerAnalyticsReportDto) |
| **Controllers** | 6 endpoints | 5 endpoints |
| **Tests** | 1 test d'intégration | 1 test d'intégration |
| **Total** | **11 endpoints REST** documentés |

---

## 🎓 Conclusion

Ce projet démontre :

✅ **Maîtrise de Spring Boot** - Framework moderne, configuration optimisée  
✅ **Architecture solide** - Séparation des couches, patterns SOLID  
✅ **Requêtes JPA optimisées** - Agrégation en base de données  
✅ **API RESTful robuste** - Endpoints cohérents et documentés  
✅ **Testing professionnel** - Tests d'intégration avec mocks  
✅ **Production-ready** - Gestion d'erreurs, logging, configuration MySQL  

**Prêt à émerger en production !** 🚀

---

## 📝 Notes pour la présentation

1. **Commencer par l'architecture** (5 min)
2. **Démarrer l'application** (2 min)
3. **Tester 2-3 endpoints clés** (5 min)
4. **Montrer les tests** (3 min)
5. **Discuter points techniques** (5 min)

**Total : 20 minutes de démo professionnelle**


