# Disaster Aid Backend - Architecture, annotations, et endpoints

## 1) Vue d'ensemble

Ce backend est une API Spring Boot structuree par domaines metier. La logique est repartie en couches classiques :

- Controllers (REST) : exposent les endpoints HTTP.
- Services : logique metier et regles.
- Repositories : acces base via Spring Data JPA / requetes JPQL ou SQL natif.
- Entities / DTOs : modeles de persistance et contrats d'API.

Packages racine (exemples) :
- `alert/` : gestion des alertes (alertes, regles, SLA, quality, replay).
- `analytics/` : analytics M13/M14 (reporting, audit, prediction).
- `modules/` : sous-domaines (sensor, incident, communication, coordination, gamification, hours, etc.).
- `config/` : securite, OpenAPI, WebSocket, CORS, cache.
- `statistics/` et `spatial/` : stats et requetes spatiales PostGIS.

## 2) Annotations cles (avec exemples)

### Spring Boot / configuration
- `@SpringBootApplication`, `@EnableScheduling`, `@EnableCaching` dans `src/main/java/tn/esprit/disasteraidbackend/DisasterAidBackendApplication.java`.
- `@Configuration`, `@Bean` dans `src/main/java/tn/esprit/disasteraidbackend/config/SecurityConfig.java` et `src/main/java/tn/esprit/disasteraidbackend/config/OpenApiConfig.java`.
- `@EnableWebSocketMessageBroker` dans `src/main/java/tn/esprit/disasteraidbackend/config/WebSocketConfig.java`.

### Web / REST
- `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@PatchMapping`.
- Exemple : `src/main/java/tn/esprit/disasteraidbackend/modules/sensor/controllers/SensorController.java`.

### Validation
- `@Valid` pour valider les DTO en entree.
- Gestion centrale des erreurs dans `src/main/java/tn/esprit/disasteraidbackend/config/GlobalExceptionHandler.java` avec `@RestControllerAdvice` et `@ExceptionHandler`.

### Securite
- `@EnableMethodSecurity` pour activer `@PreAuthorize`.
- Exemple : `@PreAuthorize` sur endpoints admin dans `src/main/java/tn/esprit/disasteraidbackend/modules/sensor/controllers/SensorController.java`.

### JPA / Hibernate
- `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Enumerated` pour les entites.
- `@MappedSuperclass` dans `src/main/java/tn/esprit/disasteraidbackend/common/BaseEntity.java` pour `id`, `createdAt`, `updatedAt`.
- `@Repository`, `@Query`, `@Param` dans les repositories.

### Cache
- `@Cacheable`, `@CacheEvict` (ex: `src/main/java/tn/esprit/disasteraidbackend/modules/sensor/services/SensorService.java`).

## 3) Flux typique d'une requete

Exemple "capteurs" :
1. Controller : `SensorController` recoit `/api/sensors`.
2. Service : `SensorService` applique la logique metier, conversions DTO/Entity.
3. Repository : `SensorRepository` execute les requetes JPA / SQL.
4. Entite : `Sensor` est mappee sur la table `sensors` (avec champ geometry PostGIS).

## 4) Securite et acces

- JWT + OAuth2 (Google/Facebook) : `src/main/java/tn/esprit/disasteraidbackend/config/SecurityConfig.java`.
- Endpoints publics autorises : `/api/auth/**`, `/oauth2/**`, `/login/**`, `/swagger-ui/**`, etc.
- CORS : autorise `http://localhost:4200`.

## 5) WebSocket

Config STOMP : `src/main/java/tn/esprit/disasteraidbackend/config/WebSocketConfig.java`
- Endpoints WS :
  - `/ws-chat` (SockJS)
  - `/ws` (native WS)
  - `/ws-sockjs` (SockJS fallback)
- Prefixes STOMP :
  - Inbound: `/app`
  - Broker: `/topic`

MessageMapping :
- `TypingController`: `@MessageMapping("/typing")` => destination client `/topic/typing/{conversationId}`.

---

# 6) Endpoints HTTP (tous les controllers)

## Alerts (alert)
Base: `/api/alerts`
- GET `/api/alerts`
- GET `/api/alerts/active`
- GET `/api/alerts/{alertId}`
- GET `/api/alerts/with-stats`
- GET `/api/alerts/{alertId}/with-stats`
- GET `/api/alerts/map-summary`
- POST `/api/alerts`
- PUT `/api/alerts/{alertId}`
- DELETE `/api/alerts/{alertId}`

Base: `/api/alerts/compare`
- GET `/api/alerts/compare?alertIdA=&alertIdB=`

Base: `/api/alerts/replay`
- GET `/api/alerts/replay/{alertId}`

Base: `/api/rules`
- GET `/api/rules`
- POST `/api/rules`
- PUT `/api/rules/{id}`
- DELETE `/api/rules/{id}`
- POST `/api/rules/evaluate`

Base: `/api/risk-zones`
- GET `/api/risk-zones`

Base: `/api/sla`
- GET `/api/sla`
- GET `/api/sla/alert/{alertId}`
- GET `/api/sla/summary`

Base: `/api/data-quality`
- GET `/api/data-quality/analyze`

## Analytics M13 (disaster)
Base: `/api/analytics/disaster`
- GET `/api/analytics/disaster/report`
- GET `/api/analytics/disaster/stats`
- GET `/api/analytics/disaster/stats/alert/{alertId}`
- GET `/api/analytics/disaster/stats/alert/{alertId}/latest`
- GET `/api/analytics/disaster/audit-logs`
- GET `/api/analytics/disaster/predictive-models?activeOnly=`
- POST `/api/analytics/disaster/add_model`
- PUT `/api/analytics/disaster/modify_model?modelId=`
- DELETE `/api/analytics/disaster/delete?modelId=`

## Analytics M14 (volunteer)
Base: `/api/analytics/volunteer`
- GET `/api/analytics/volunteer/report?highRiskThreshold=`
- GET `/api/analytics/volunteer/stats/{volunteerId}`
- GET `/api/analytics/volunteer/organisations/{orgId}`
- GET `/api/analytics/volunteer/churn/high-risk?threshold=`
- GET `/api/analytics/volunteer/churn/volunteer/{volunteerId}`

## Statistics
Base: `/api/stats`
- GET `/api/stats/dashboard`
- GET `/api/stats/alerts/by-type?from=&to=`
- GET `/api/stats/alerts/by-status`
- GET `/api/stats/alerts/by-severity`
- GET `/api/stats/sensors/avg?hours=`
- GET `/api/stats/sensors/anomaly-rate`
- GET `/api/stats/routes/by-status`
- GET `/api/stats/sensors/by-status`

## Spatial
Base: `/api/spatial`
- GET `/api/spatial/sensors/inside-zone?polygon=`
- GET `/api/spatial/sensors/nearest?point=&limit=`

## Auth / Identity
Base: `/api/auth`
- POST `/api/auth/register`
- POST `/api/auth/login`
- POST `/api/auth/logout`
- POST `/api/auth/forgot-password`
- POST `/api/auth/verify-reset-code`
- POST `/api/auth/reset-password`

Base: `/api/identity-access/users`
- GET `/api/identity-access/users`
- GET `/api/identity-access/users/{id}`
- POST `/api/identity-access/users`
- PUT `/api/identity-access/users/{id}`
- DELETE `/api/identity-access/users/{id}`
- GET `/api/identity-access/users/me`
- PUT `/api/identity-access/users/me` (multipart/form-data)

## Incidents
Base: `/api/incidents`
- GET `/api/incidents`
- GET `/api/incidents/{id}`
- POST `/api/incidents`
- PUT `/api/incidents/{id}`
- DELETE `/api/incidents/{id}`
- GET `/api/incidents/status/{status}`
- GET `/api/incidents/severity/{severity}`
- PATCH `/api/incidents/{id}/upvote`
- PATCH `/api/incidents/{id}/downvote`
- PATCH `/api/incidents/{id}/status?status=`
- POST `/api/incidents/check-duplicate`
- GET `/api/incidents/{id}/history`

Base: `/api/evidences`
- GET `/api/evidences`
- GET `/api/evidences/{id}`
- GET `/api/evidences/report/{reportId}`
- POST `/api/evidences/report/{reportId}`
- DELETE `/api/evidences/{id}`

Base: `/api/evidences/uploads`
- POST `/api/evidences/uploads/report/{reportId}/upload`
- GET `/api/evidences/uploads/files/{filename}`
- GET `/api/evidences/uploads/report/{reportId}`
- DELETE `/api/evidences/uploads/api/v1/uploads/{id}`

## Sensors
Base: `/api/sensors`
- POST `/api/sensors`
- GET `/api/sensors/{id}`
- GET `/api/sensors`
- GET `/api/sensors/status/{status}`
- GET `/api/sensors/type/{type}`
- GET `/api/sensors/owner/{ownerId}`
- PUT `/api/sensors/{id}`
- DELETE `/api/sensors/{id}`
- PATCH `/api/sensors/{id}/ping`

Base: `/api/sensor-data`
- POST `/api/sensor-data`
- GET `/api/sensor-data/sensor/{sensorId}`
- GET `/api/sensor-data/sensor/{sensorId}/between?start=&end=`
- GET `/api/sensor-data/anomalies`

Base: `/api/sensor/alerts`
- POST `/api/sensor/alerts`
- GET `/api/sensor/alerts/{id}`
- GET `/api/sensor/alerts`
- GET `/api/sensor/alerts/status/{status}`
- GET `/api/sensor/alerts/type/{type}`
- GET `/api/sensor/alerts/severity/{severity}`
- GET `/api/sensor/alerts/since?date=`
- PUT `/api/sensor/alerts/{id}`
- PATCH `/api/sensor/alerts/{id}/resolve`
- DELETE `/api/sensor/alerts/{id}`

Base: `/api/alert-thresholds`
- POST `/api/alert-thresholds`
- GET `/api/alert-thresholds/{id}`
- GET `/api/alert-thresholds`
- GET `/api/alert-thresholds/sensor-type/{sensorType}/parameter/{parameter}`
- PUT `/api/alert-thresholds/{id}`
- DELETE `/api/alert-thresholds/{id}`

Base: `/api/geofence-zones`
- POST `/api/geofence-zones`
- GET `/api/geofence-zones/{id}`
- GET `/api/geofence-zones`
- GET `/api/geofence-zones/alert/{alertId}`
- GET `/api/geofence-zones/type/{type}`
- PUT `/api/geofence-zones/{id}`
- DELETE `/api/geofence-zones/{id}`

## Shelter agent
Base: `/api/shelters`
- GET `/api/shelters`
- GET `/api/shelters/nearby?lat=&lng=&maxDistanceKm=&city=`
- POST `/api/shelters/recommend`

## Resource/Shelter
Base: `/resource-shelter/shelters`
- POST `/resource-shelter/shelters`
- GET `/resource-shelter/shelters/{id}`
- GET `/resource-shelter/shelters`
- PUT `/resource-shelter/shelters/{id}`
- DELETE `/resource-shelter/shelters/{id}`

Base: `/resource-shelter/shelter-occupants`
- POST `/resource-shelter/shelter-occupants/check-in`
- POST `/resource-shelter/shelter-occupants/{recordId}/check-out`
- GET `/resource-shelter/shelter-occupants/{id}`
- GET `/resource-shelter/shelter-occupants`
- GET `/resource-shelter/shelter-occupants/by-shelter/{shelterId}`
- PUT `/resource-shelter/shelter-occupants/{id}`
- DELETE `/resource-shelter/shelter-occupants/{id}`

Base: `/resource-shelter/resources`
- POST `/resource-shelter/resources`
- GET `/resource-shelter/resources/{id}`
- GET `/resource-shelter/resources`
- PUT `/resource-shelter/resources/{id}`
- DELETE `/resource-shelter/resources/{id}`

## Reviews
Base: `/reviews`
- POST `/reviews`
- GET `/reviews/{id}`
- GET `/reviews`
- PUT `/reviews/{id}`
- DELETE `/reviews/{id}`
- GET `/reviews/by-target/{targetId}?targetType=`
- GET `/reviews/by-author/{authorId}`
- GET `/reviews/by-status?status=`
- POST `/reviews/{id}/helpful`
- PATCH `/reviews/{id}/moderate?status=`
- PATCH `/reviews/{id}/verify-participant`

Base: `/reviews/trust-scores`
- POST `/reviews/trust-scores`
- GET `/reviews/trust-scores/{id}`
- GET `/reviews/trust-scores`
- GET `/reviews/trust-scores/by-entity?entityId=&entityType=`
- GET `/reviews/trust-scores/by-type?entityType=`
- PUT `/reviews/trust-scores/{id}`
- DELETE `/reviews/trust-scores/{id}`
- POST `/reviews/trust-scores/recalculate?entityId=&entityType=`

Base: `/reviews/moderation`
- POST `/reviews/moderation`
- GET `/reviews/moderation/{id}`
- GET `/reviews/moderation`
- PUT `/reviews/moderation/{id}`
- DELETE `/reviews/moderation/{id}`
- GET `/reviews/moderation/by-target/{targetId}?targetType=`
- GET `/reviews/moderation/by-admin/{adminId}`

## Communication
Base: `/api/communication/conversations`
- GET `/api/communication/conversations/retrieve-all`
- GET `/api/communication/conversations/me`
- GET `/api/communication/conversations/{id}`
- POST `/api/communication/conversations/add`
- PUT `/api/communication/conversations/update/{id}`
- DELETE `/api/communication/conversations/delete/{id}`

Base: `/api/communication/messages`
- GET `/api/communication/messages/retrieve-all`
- GET `/api/communication/messages/me`
- GET `/api/communication/messages/conversation/{conversationId}`
- GET `/api/communication/messages/{id}`
- POST `/api/communication/messages/add`
- PUT `/api/communication/messages/update/{id}`
- DELETE `/api/communication/messages/delete/{id}`

Base: `/api/communication/notifications`
- GET `/api/communication/notifications/retrieve-all-notifications`
- GET `/api/communication/notifications/my-notifications`
- GET `/api/communication/notifications/retrieve-notification/{id}`
- POST `/api/communication/notifications/add-notification`
- PUT `/api/communication/notifications/update-notification/{id}`
- DELETE `/api/communication/notifications/remove-notification/{id}`

Base: `/api/communication/preferences`
- GET `/api/communication/preferences/retrieve-all`
- GET `/api/communication/preferences/me`
- GET `/api/communication/preferences/{id}`
- POST `/api/communication/preferences/add`
- PUT `/api/communication/preferences/update/{id}`
- DELETE `/api/communication/preferences/delete/{id}`

Base: `/api/conversation-requests`
- POST `/api/conversation-requests/citizen/{citizenId}`
- GET `/api/conversation-requests/pending`
- GET `/api/conversation-requests/citizen/{citizenId}`
- GET `/api/conversation-requests/admin/{adminId}`
- PUT `/api/conversation-requests/{requestId}/accept/admin/{adminId}`
- PUT `/api/conversation-requests/{requestId}/reject/admin/{adminId}`
- POST `/api/conversation-requests/predict`

## Coordination
Base: `/api/help-requests`
- GET `/api/help-requests`
- GET `/api/help-requests/{id}`
- POST `/api/help-requests`
- PUT `/api/help-requests/{id}`
- DELETE `/api/help-requests/{id}`
- GET `/api/help-requests/status/{status}`

Base: `/api/assignments`
- GET `/api/assignments`
- GET `/api/assignments/{id}`
- GET `/api/assignments/help-request/{helpRequestId}`
- POST `/api/assignments/help-request/{helpRequestId}`
- PATCH `/api/assignments/{id}/status?status=`
- PATCH `/api/assignments/{id}/complete`
- DELETE `/api/assignments/{id}`

Base: `/api/volunteer-offers`
- GET `/api/volunteer-offers`
- GET `/api/volunteer-offers/{id}`
- POST `/api/volunteer-offers`
- PUT `/api/volunteer-offers/{id}`
- DELETE `/api/volunteer-offers/{id}`
- GET `/api/volunteer-offers/status/{status}`
- GET `/api/volunteer-offers/matches?helpRequestId=`

Base: `/api/coordination`
- GET `/api/coordination/stats`

## Evacuation
Base: `/api/evacuation-routes`
- POST `/api/evacuation-routes`
- GET `/api/evacuation-routes/{id}`
- GET `/api/evacuation-routes`
- GET `/api/evacuation-routes/alert/{alertId}`
- GET `/api/evacuation-routes/status/{status}`
- GET `/api/evacuation-routes/shelter/{shelterId}`
- PUT `/api/evacuation-routes/{id}`
- PATCH `/api/evacuation-routes/{id}/recompute`
- PATCH `/api/evacuation-routes/{id}/activate`
- PATCH `/api/evacuation-routes/{id}/close`
- DELETE `/api/evacuation-routes/{id}`

Base: `/api/route-checkpoints`
- POST `/api/route-checkpoints`
- GET `/api/route-checkpoints/{id}`
- GET `/api/route-checkpoints`
- GET `/api/route-checkpoints/route/{routeId}`
- GET `/api/route-checkpoints/type/{type}`
- PUT `/api/route-checkpoints/{id}`
- DELETE `/api/route-checkpoints/{id}`

Base: `/api/zone-shelter-assignments`
- POST `/api/zone-shelter-assignments`
- GET `/api/zone-shelter-assignments/{id}`
- GET `/api/zone-shelter-assignments`
- GET `/api/zone-shelter-assignments/zone/{zoneId}`
- GET `/api/zone-shelter-assignments/shelter/{shelterId}`
- PUT `/api/zone-shelter-assignments/{id}`
- DELETE `/api/zone-shelter-assignments/{id}`

## Gamification
Base: `/api/v1/achievements`
- POST `/api/v1/achievements`
- GET `/api/v1/achievements`
- GET `/api/v1/achievements/active`
- GET `/api/v1/achievements/{id}`
- GET `/api/v1/achievements/category/{category}`
- GET `/api/v1/achievements/rarity/{rarity}`
- PUT `/api/v1/achievements/{id}`
- DELETE `/api/v1/achievements/{id}`

Base: `/api/v1/challenges`
- POST `/api/v1/challenges`
- GET `/api/v1/challenges`
- GET `/api/v1/challenges/{id}`
- GET `/api/v1/challenges/status/{status}`
- GET `/api/v1/challenges/volunteer/{volunteerId}`
- PUT `/api/v1/challenges/{id}`
- POST `/api/v1/challenges/{id}/join?volunteerId=`
- POST `/api/v1/challenges/{id}/leave?volunteerId=`
- DELETE `/api/v1/challenges/{id}`

Base: `/api/v1/leaderboards`
- POST `/api/v1/leaderboards`
- GET `/api/v1/leaderboards`
- GET `/api/v1/leaderboards/{id}`
- GET `/api/v1/leaderboards/scope/{scope}`
- GET `/api/v1/leaderboards/metric/{metric}`
- PUT `/api/v1/leaderboards/{id}`
- PUT `/api/v1/leaderboards/{id}/refresh`
- DELETE `/api/v1/leaderboards/{id}`

Base: `/api/v1/volunteer-achievements`
- POST `/api/v1/volunteer-achievements`
- GET `/api/v1/volunteer-achievements`
- GET `/api/v1/volunteer-achievements/{id}`
- GET `/api/v1/volunteer-achievements/volunteer/{volunteerId}`
- GET `/api/v1/volunteer-achievements/volunteer/{volunteerId}/displayed`
- PUT `/api/v1/volunteer-achievements/{id}/display?value=`
- DELETE `/api/v1/volunteer-achievements/{id}`

## Hours / Impact
Base: `/api/v1/impact-records`
- POST `/api/v1/impact-records`
- GET `/api/v1/impact-records`
- GET `/api/v1/impact-records/{id}`
- GET `/api/v1/impact-records/volunteer/{volunteerId}`
- PUT `/api/v1/impact-records/{id}`
- DELETE `/api/v1/impact-records/{id}`

Base: `/api/v1/timesheets`
- POST `/api/v1/timesheets`
- GET `/api/v1/timesheets`
- GET `/api/v1/timesheets/{id}`
- GET `/api/v1/timesheets/volunteer/{volunteerId}`
- GET `/api/v1/timesheets/org/{orgId}`
- GET `/api/v1/timesheets/supervisor/{supervisorId}`
- PUT `/api/v1/timesheets/{id}`
- PUT `/api/v1/timesheets/{id}/approve?supervisorId=`
- PUT `/api/v1/timesheets/{id}/reject?supervisorId=&reason=`
- DELETE `/api/v1/timesheets/{id}`

Base: `/api/v1/certificates`
- POST `/api/v1/certificates`
- GET `/api/v1/certificates`
- GET `/api/v1/certificates/{id}`
- GET `/api/v1/certificates/volunteer/{volunteerId}`
- GET `/api/v1/certificates/org/{orgId}`
- PUT `/api/v1/certificates/{id}`
- DELETE `/api/v1/certificates/{id}`

---

# 7) Explication detaillee du code (par modules)

## 7.1) Noyau et infrastructure
- Point d'entree: `src/main/java/tn/esprit/disasteraidbackend/DisasterAidBackendApplication.java`
  - Active le scan des beans et l'auto-config Spring.
  - Active les taches planifiees (scheduler) et le cache.
  - Lance `DatabaseBootstrap.ensureDatabaseExists()` avant le demarrage.
- Bootstrap base: `src/main/java/tn/esprit/disasteraidbackend/DatabaseBootstrap.java`
  - Cree la base PostgreSQL si elle n'existe pas (utile en local, sans intervention manuelle).

## 7.2) Modules alerts (gestion des alertes et qualite)
- Controllers:
  - `AlertController`: CRUD alertes + stats associees + map summary.
  - `ComparisonController`: compare deux alertes via stats.
  - `RuleEngineController`: CRUD regles + evaluation.
  - `RiskZoneController`: lecture des zones de risque.
  - `SlaController`: metriques SLA et resume.
  - `ReplayController`: timeline (snapshots) d'une alerte.
  - `DataQualityController`: analyse qualite des donnees.
- Points techniques:
  - Reponses uniformes `ApiResponse<T>` pour une structure standard.
  - Requetes par ID et aggregats (stats, summary) utiles pour dashboards.

## 7.3) Analytics M13/M14
- `analytics/m13`: indicateurs catastrophe, audit logs, modeles predictifs.
- `analytics/m14`: churn, stats volontaires, stats organisations.
- Pattern: un controller expose des endpoints de reporting, le service calcule/agrège, les entities stockent les stats.

## 7.4) Sensors + Spatial
- `modules/sensor`:
  - `SensorController`: CRUD capteurs + filtre par status/type/owner + ping.
  - `SensorDataController`: enregistrement lectures + requetes par capteur/dates + anomalies.
  - `SensorAlertController`: CRUD alertes des capteurs + resolve.
  - `AlertThresholdController`: seuils d'alerte par type de capteur.
  - `GeofenceZoneController`: zones geofence par alerte/type.
- `spatial/SpatialQueryController`:
  - Requetes PostGIS: capteurs dans polygone, capteurs les plus proches.
- Mapping geo:
  - `DTOMapper` convertit WKT <-> Geometry (Point/Polygon).

## 7.5) Incidents + evidences
- `IncidentReportController`:
  - CRUD incidents, status/severity, votes, historique, detection de doublons.
- `ReportEvidenceController`:
  - CRUD evidences liees aux incidents.
- `EvidenceUploadController`:
  - Upload fichiers, recuperation, mapping fichier -> evidence, types media.

## 7.6) Resource/Shelter + Shelter agent
- `ResourceController`, `ShelterController`, `ShelterOccupantController`:
  - CRUD ressources et shelters + check-in/out occupants.
- `ShelterRecommendationController`:
  - Reco shelters a proximite + recommandation classee.

## 7.7) Communication (chat, notifications)
- Conversations + messages + preferences + notifications.
- Endpoints orientes actions: retrieve-all, me, add, update, delete.
- WebSocket:
  - `TypingController` envoie un evenement typing via `/topic/typing/{conversationId}`.

## 7.8) Coordination (help requests, assignments, offers)
- `HelpRequestController`, `TaskAssignmentController`, `VolunteerOfferController`.
- `CoordinationStatsController` pour stats agreges.
- Flux typique: HelpRequest -> match offers -> create assignment -> update status.

## 7.9) Evacuation (routes, checkpoints, assignments)
- `EvacuationRouteController`: CRUD route + recompute/activate/close.
- `RouteCheckpointController`: CRUD checkpoints.
- `ZoneShelterAssignmentController`: CRUD affectations zones -> shelters.

## 7.10) Gamification
- `AchievementController`, `ChallengeController`, `LeaderboardController`, `VolunteerAchievementController`.
- CRUD + actions metier (join/leave challenge, refresh leaderboard, toggle display).

## 7.11) Hours / Impact
- `ImpactRecordController`, `TimesheetController`, `CertificateController`.
- Workflow: timesheet soumis -> approve/reject -> certificates et impact.

---

# 8) Inventaire des annotations (reference rapide)

## 8.1) Spring Boot / config
- `@SpringBootApplication`, `@EnableScheduling`, `@EnableCaching`
- `@Configuration`, `@Bean`
- `@EnableWebSocketMessageBroker`

## 8.2) Web / REST
- `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`, `@PatchMapping`
- `@RequestParam`, `@PathVariable`, `@RequestBody`, `@RequestPart`
- `@CrossOrigin`

## 8.3) Validation / erreurs
- `@Valid`
- `@RestControllerAdvice`, `@ExceptionHandler`

## 8.4) Securite
- `@EnableMethodSecurity`, `@PreAuthorize`
- Filtres JWT ajoutés via `SecurityFilterChain`

## 8.5) JPA / Hibernate
- `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Enumerated`
- `@MappedSuperclass`
- `@Repository`, `@Query`, `@Param`
- `@CreationTimestamp`, `@UpdateTimestamp`

## 8.6) Cache / Transaction
- `@Cacheable`, `@CacheEvict`
- `@Transactional`

## 8.7) Lombok
- `@RequiredArgsConstructor`, `@Getter`, `@Setter`

## 8.8) OpenAPI
- `@Operation`, `@Tag`, `@Parameter`

---

# 9) Workflow de presentation (demo)

## 9.1) Version courte (5-7 min)
1. Intro rapide (objectif, problemes resolus).
2. Architecture (packages + couches).
3. Securite (JWT + roles + endpoints publics).
4. Demo: 2 endpoints cles (ex: alerts + analytics).
5. Conclusion (impact + prochaines etapes).

## 9.2) Version technique (8-10 min)
1. Intro et contexte.
2. Structure du projet (packages + modules).
3. Deep-dive "capteurs": Controller -> Service -> Repository -> DB.
4. Spatial + PostGIS (inside-zone / nearest).
5. Analytics M13/M14 (report + churn).
6. WebSocket (typing event) + securite (JWT).
7. Demo Swagger (3-4 endpoints).
8. Q&A.

## 9.3) Script de demo (exemple)
- Ouvrir Swagger: `http://localhost:8089/swagger-ui.html`
- Verifier un endpoint public (ex: `/api/analytics/disaster/report`).
- Recuperer alerts: `/api/alerts/active`.
- Test spatial: `/api/spatial/sensors/nearest?point=POINT(... )&limit=3`.

---

# 10) Notes d'alignement front
- Base API: `http://localhost:8089/api`
- WebSocket: `ws://localhost:8089/ws` (native) ou `/ws-chat` (SockJS)
