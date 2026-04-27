# 🎓 GUIDE DE PRÉSENTATION - DISASTER AID BACKEND

## ⏱️ Timeline recommandée : 20 minutes

---

## Phase 1️⃣ : Introduction (2 minutes)

### À dire au professeur :

> "Bonjour, je vous présente **Disaster Aid Backend** - un système d'analyse et de reporting pour une plateforme d'aide aux catastrophes naturelles et de gestion de volontariat."

> "Le projet implémente **2 modules analytics critiques** :
> - **M13** : Analyse consolidée des catastrophes (KPIs, audit trail, modèles ML)
> - **M14** : Analyse de l'impact volontaire (statistiques, prédiction de churn)"

> "Stack technologique : **Spring Boot 4.0.4**, **Java 17**, **MySQL**, **JPA/Hibernate**, **Maven**"

---

## Phase 2️⃣ : Architecture (3 minutes)

### Schéma à montrer :

```
┌─────────────────────────────────────────────┐
│          REST CONTROLLERS                   │
│  /api/analytics/disaster                    │
│  /api/analytics/volunteer                   │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│          SERVICE LAYER                      │
│  Business Logic + KPI Aggregation           │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│      REPOSITORY LAYER (JPA)                 │
│  Custom queries for aggregation             │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│          MySQL DATABASE                     │
│  8 tables : 4 for M13 + 4 for M14           │
└─────────────────────────────────────────────┘
```

### Points clés :
- ✅ **MVC Pattern** : Séparation claire des responsabilités
- ✅ **Testabilité** : Chaque couche testable indépendamment
- ✅ **Scalabilité** : Architecture prête pour évolution

---

## Phase 3️⃣ : Démarrage de l'application (2 minutes)

### Étapes :

```powershell
# 1. Ouvrir PowerShell dans le répertoire du projet
cd C:\Users\21658\Documents\GitHub\disaster-aid-backend

# 2. Vérifier que MySQL est running (XAMPP)
# Menu XAMPP → Start MySQL

# 3. Démarrer l'app
.\mvnw.cmd spring-boot:run
```

### À attendre :
```
[INFO] Tomcat started on port(s): 8081 (http)
[INFO] Started DisasterAidBackendApplication in 3.456 seconds
```

**✅ Application prête sur http://localhost:8081**

---

## Phase 4️⃣ : Démonstration M13 (5 minutes)

### Endpoint 1 : Rapport global
```
URL: http://localhost:8081/api/analytics/disaster/report
```

**Montrer et expliquer :**
- ✅ Agrégation de 5 catastrophes
- ✅ KPIs : totalReports, verifiedReports, avgResponseTime
- ✅ Credibility Score (0.92)
- ✅ Coverage Area (45.3 km²)

**À dire :**
> "Cet endpoint agrège les données de toutes les catastrophes en base pour donner une vue globale. Requête JPA optimisée exécutée directement en MySQL."

### Endpoint 2 : Statistiques détaillées
```
URL: http://localhost:8081/api/analytics/disaster/stats
```

**Montrer :**
- Breakdown par alerte (alertId 101, 102, etc.)
- Chaque catastrophe avec ses métriques spécifiques

**À dire :**
> "Les managers de catastrophe peuvent drill-down pour voir les détails d'une alerte spécifique."

### Endpoint 3 : Logs d'audit (optionnel)
```
URL: http://localhost:8081/api/analytics/disaster/audit-logs
```

**Montrer :**
- Journal immutable de toutes les actions
- Traçabilité : qui a fait quoi, quand
- Détection des actions générées par ML

**À dire :**
> "Chaque action est tracée pour audit, compliance et debugging. Immutable par design."

---

## Phase 5️⃣ : Démonstration M14 (5 minutes)

### Endpoint 1 : Rapport volontaires
```
URL: http://localhost:8081/api/analytics/volunteer/report?highRiskThreshold=0.70
```

**Montrer et expliquer :**
- 📊 **1250.5 heures** travaillées par les volontaires
- 🎯 **45 opportunités** disponibles
- ⚠️ **8 volontaires à haut risque** (churn > 70%)
- 🏢 **12 organisations** suivies

**À dire :**
> "Cet endpoint donne au manager des volontaires une vue complète de l'impact. On voit immédiatement les volontaires à risque de départ."

### Endpoint 2 : Volontaires à risque
```
URL: http://localhost:8081/api/analytics/volunteer/churn/high-risk?threshold=0.70
```

**Montrer :**
- **2 volontaires** identifiés comme à risque
- **Recommandations d'intervention** :
  - "SCHEDULE_INTERVIEW" : Rencontrer rapidement
  - "PERSONALIZED_OPPORTUNITY" : Proposer une opportunité adaptée
  - etc.

**À dire :**
> "Le système ML prédit automatiquement qui risque de partir et recommande une action concrète. Les managers peuvent intervenir avant le départ."

### Endpoint 3 : Stats par organisation (optionnel)
```
URL: http://localhost:8081/api/analytics/volunteer/organisations/1
```

**Montrer :**
- Taux de rétention par NGO
- Heures livrées
- Postes ouverts

---

## Phase 6️⃣ : Tests unitaires (2 minutes)

```powershell
# Dans un nouveau terminal PowerShell

# Exécuter les tests
.\mvnw.cmd test -q
```

**Output attendu :**
```
[INFO] Tests run: 2, Failures: 0, Errors: 0
[INFO] BUILD SUCCESS
```

**À dire :**
> "2 tests d'intégration completes qui valident :
> - Agrégation des données M13
> - Agrégation des données M14 + détection volontaires à risque
> - Tous les chemins critiques sont testés"

---

## Phase 7️⃣ : Points techniques importants (3 minutes)

### 1️⃣ **Agrégation optimisée en base**
```java
// Requête JPA exécutée en MySQL, pas en Java
@Query("SELECT SUM(...), AVG(...), ... FROM DisasterStatistics")
List<Object[]> aggregateGlobalKpis();
```
**Avantage** : Performance, réduction mémoire, scalabilité

### 2️⃣ **Constructeurs explicites (pas de Lombok builder)**
```java
// ✅ Production-safe
public DisasterAnalyticsReportDto(int total, ...) {
    this.total = total;
}
```
**Avantage** : Stabilité IDE/runtime, pas de dépendances fragiles

### 3️⃣ **Injection par constructeur**
```java
public DisasterAnalyticsService(DisasterStatisticsRepository repo) {
    this.repo = repo;  // Facilite les tests
}
```
**Avantage** : Testabilité, pas de side effects

### 4️⃣ **Réponses standardisées**
```java
ApiResponse<DisasterAnalyticsReportDto> {
  success: true,
  message: "...",
  data: {...},
  timestamp: "2026-04-16T10:30:00Z"
}
```
**Avantage** : Cohérence API, gestion d'erreurs uniforme

---

## 🎯 Résumé des livrables

| Composant | M13 | M14 | Total |
|-----------|-----|-----|-------|
| Entités JPA | 3 | 3 | 6 |
| Repositories | 3 | 3 | 6 |
| Services | 1 | 1 | 2 |
| Controllers | 1 | 1 | 2 |
| **Endpoints REST** | **5** | **5** | **10** |
| Tests d'intégration | 1 | 1 | 2 |

---

## 📋 Checklist avant la présentation

- [ ] MySQL lancé (XAMPP)
- [ ] Application compilée et testée
- [ ] Navigateur ouvert sur http://localhost:8081
- [ ] Postman/Insomnia prêt pour tester les endpoints (optionnel)
- [ ] Document DEMO_PRESENTATION.md disponible
- [ ] Terminal PowerShell disponible pour les tests

---

## 🎤 Réponses aux questions probables du professeur

### Q1: "Comment gérez-vous la performance avec les agrégations ?"
**Réponse :**
> "Les agrégations sont effectuées directement en MySQL via les requêtes JPA, pas en Java. Cela réduit drastiquement la quantité de données à transférer et traiter en mémoire. Pour les gros volumes, on peut ajouter des indexes."

### Q2: "Pourquoi pas utiliser Lombok partout ?"
**Réponse :**
> "J'ai préféré les constructeurs explicites dans les services et DTOs pour éviter les instabilités IDE. Lombok crée du bytecode au compile-time qui n'est pas toujours visible dans l'IDE. C'est une trade-off: flexibilité vs stabilité."

### Q3: "Comment testez-vous ?"
**Réponse :**
> "J'utilise Mockito pour mocker les repositories et tester la logique métier isolée. Tests d'intégration avec H2 en-mémoire pour éviter les dépendances externes."

### Q4: "Comment les modèles ML sont intégrés ?"
**Réponse :**
> "M13 maintient un registre des modèles ML en production (PredictiveModel). M14 utilise des prédictions stockées (ChurnPrediction). Dans une phase future, on peut ajouter une pipeline d'entraînement/déploiement."

### Q5: "Et si la base de données est off ?"
**Réponse :**
> "En test, on utilise H2 en-mémoire. En production, on peut ajouter un circuit breaker et cache pour la résilience."

---

## 💡 Tips pour une présentation réussie

1. **Parlez lentement** - Donnez le temps au professeur de comprendre l'architecture
2. **Montrez du code** - Ouvrez l'IDE et naviguer rapidement dans les fichiers
3. **Exécutez les requêtes** - Utilisez curl/Postman pour montrer les réponses réelles
4. **Montrez les tests** - Prouvez que tout fonctionne
5. **Préparez l'internet** - Assurez-vous que vous pouvez accéder à localhost

---

## 📞 Fichiers importants à consulter

```
disaster-aid-backend/
├── DEMO_PRESENTATION.md          ← Document détaillé avec tous les endpoints
├── demo-quick.ps1               ← Script pour démo rapide
├── src/main/java/tn/esprit/
│   └── disasteraidbackend/
│       ├── analytics/
│       │   ├── m13/              ← DisasterAnalyticsService + endpoints
│       │   └── m14/              ← VolunteerAnalyticsService + endpoints
│       └── common/
│           └── ApiResponse.java  ← Format de réponse standard
└── pom.xml                       ← Dépendances du projet
```

---

## ✨ Bonne chance pour votre présentation ! 🚀

Vous avez tous les éléments pour impressionner votre professeur avec ce projet professionnel ! 

**N'hésitez pas à poser des questions pendant la présentation - c'est bon signe !**


