-- =========================================================
--  Fake seed data for m13 + m14 analytics
--  Run AFTER the app has started once so Hibernate creates
--  the tables (spring.jpa.hibernate.ddl-auto=update).
--
--  Usage:
--    mysql -u root disaster_aid < docs/seed-analytics.sql
--  or paste into MySQL Workbench against the disaster_aid schema.
-- =========================================================

USE disaster_aid;

-- ---------- clean existing seed rows (optional) ----------
-- DELETE FROM disaster_statistics;
-- DELETE FROM audit_log;
-- DELETE FROM predictive_model;
-- DELETE FROM volunteer_statistics;
-- DELETE FROM organisation_stats;
-- DELETE FROM churn_prediction;

-- =========================================================
--  M13 — DISASTER ANALYTICS
-- =========================================================

-- ----- predictive_model -----
INSERT INTO predictive_model
    (name, version, type, artifact_url, metrics, is_active, trained_at)
VALUES
    ('Wildfire Spread Net',   '1.2.0', 'SPREAD_PREDICTOR',   'https://models.local/wildfire-spread-1.2.0.onnx', '{"accuracy":0.91,"f1":0.88}', TRUE,  '2026-03-10 09:15:00'),
    ('Aid Matcher',           '0.9.1', 'MATCHING_OPTIMIZER', 'https://models.local/aid-matcher-0.9.1.pt',       '{"precision":0.84}',          TRUE,  '2026-03-18 14:40:00'),
    ('Report Credibility',    '2.0.0', 'CREDIBILITY_SCORER', 'https://models.local/credibility-2.0.0.pkl',      '{"auc":0.93}',                TRUE,  '2026-03-22 11:05:00'),
    ('Volunteer Churn',       '1.0.3', 'CHURN_PREDICTOR',    'https://models.local/churn-1.0.3.pkl',            '{"recall":0.79}',             TRUE,  '2026-03-28 08:30:00'),
    ('Impact Estimator',      '0.5.0', 'IMPACT_ESTIMATOR',   'https://models.local/impact-0.5.0.onnx',          '{"rmse":3.2}',                FALSE, '2026-02-05 10:00:00');

-- ----- disaster_statistics -----
INSERT INTO disaster_statistics
    (alert_id, total_reports, verified_reports, total_help_requests, resolved_requests,
     active_responders, shelter_occupancy, avg_response_time_min, affected_area_km2,
     credibility_weighted_count, computed_at)
VALUES
    (101, 245, 198, 180, 142, 34, '{"shelter-A":80,"shelter-B":65}', 12.5,  42.3, 187.4, '2026-04-01 08:00:00'),
    (101, 268, 221, 195, 160, 36, '{"shelter-A":88,"shelter-B":72}', 11.2,  44.1, 205.0, '2026-04-03 08:00:00'),
    (102,  90,  75,  60,  40, 12, '{"shelter-C":30}',                 9.8,  12.0,  70.2, '2026-04-02 09:30:00'),
    (103, 410, 355, 300, 250, 58, '{"shelter-D":140,"shelter-E":98}', 15.7,  88.6, 332.9, '2026-04-04 10:15:00'),
    (104,  55,  40,  35,  28,  8, '{"shelter-F":20}',                 7.3,   5.8,  38.5, '2026-04-05 07:45:00'),
    (105, 700, 612, 540, 470, 92, '{"shelter-G":220,"shelter-H":180}',18.1, 134.0, 580.6, '2026-04-06 12:20:00');

-- ----- audit_log -----
INSERT INTO audit_log
    (actor_id, action, entity_type, entity_id, previous_value, new_value,
     ip_address, user_agent, timestamp, is_m_l_generated, ml_model_version)
VALUES
    (1,  'CREATE', 'DisasterStatistics', 101, NULL,            '{"total_reports":245}', '10.0.0.4',  'curl/8.5.0', '2026-04-01 08:00:10', FALSE, NULL),
    (1,  'UPDATE', 'PredictiveModel',     1,  '{"isActive":false}', '{"isActive":true}', '10.0.0.4',  'Mozilla/5.0', '2026-04-01 09:12:30', FALSE, NULL),
    (2,  'CREATE', 'AuditLog',          NULL, NULL,            '{"action":"LOGIN"}',    '10.0.0.9',  'Mozilla/5.0', '2026-04-02 07:40:55', FALSE, NULL),
    (99, 'PREDICT','ChurnPrediction',    77, NULL,            '{"prob":0.82}',         NULL,        NULL,          '2026-04-02 10:05:12', TRUE,  '1.0.3'),
    (1,  'DELETE', 'PredictiveModel',     9,  '{"name":"old"}', NULL,                   '10.0.0.4',  'Mozilla/5.0', '2026-04-03 11:30:00', FALSE, NULL),
    (3,  'CREATE', 'DisasterStatistics', 103, NULL,            '{"total_reports":410}', '10.0.0.12', 'curl/8.5.0', '2026-04-04 10:15:20', FALSE, NULL),
    (99, 'PREDICT','ChurnPrediction',    42, NULL,            '{"prob":0.74}',         NULL,        NULL,          '2026-04-05 09:22:00', TRUE,  '1.0.3'),
    (2,  'UPDATE', 'DisasterStatistics', 105, '{"resolved_requests":420}', '{"resolved_requests":470}', '10.0.0.9', 'Mozilla/5.0', '2026-04-06 12:20:45', FALSE, NULL);

-- =========================================================
--  M14 — VOLUNTEER ANALYTICS
-- =========================================================

-- ----- volunteer_statistics -----
INSERT INTO volunteer_statistics
    (volunteer_id, total_approved_hours, total_opportunities, cause_distribution, skills_utilised,
     avg_rating, current_streak, longest_streak, achievement_count, last_activity_date,
     churn_probability, computed_at)
VALUES
    (1,  124.5, 18, '{"disaster":10,"food":5,"medical":3}', '["first-aid","driving"]',         4.8, 6, 14, 7,  '2026-04-02', 0.12, '2026-04-06 06:00:00'),
    (2,   58.0,  9, '{"disaster":4,"shelter":5}',            '["logistics"]',                   4.2, 2,  8, 3,  '2026-03-20', 0.55, '2026-04-06 06:00:00'),
    (3,  210.0, 27, '{"disaster":15,"medical":12}',          '["nursing","triage"]',            4.9, 9, 20, 12, '2026-04-05', 0.08, '2026-04-06 06:00:00'),
    (4,    6.0,  2, '{"food":2}',                            '[]',                              3.5, 0,  1, 0,  '2026-01-12', 0.89, '2026-04-06 06:00:00'),
    (5,   92.5, 14, '{"shelter":8,"logistics":6}',           '["driving","warehousing"]',       4.6, 4, 10, 5,  '2026-03-30', 0.22, '2026-04-06 06:00:00'),
    (6,   33.0,  5, '{"disaster":5}',                        '["comms"]',                       4.0, 1,  5, 2,  '2026-02-18', 0.78, '2026-04-06 06:00:00'),
    (7,  155.0, 21, '{"medical":13,"disaster":8}',           '["paramedic","first-aid"]',       4.7, 7, 15, 9,  '2026-04-04', 0.14, '2026-04-06 06:00:00');

-- ----- organisation_stats -----
INSERT INTO organisation_stats
    (org_id, total_volunteers, active_volunteers, total_hours_delivered,
     retention_rate, avg_rating, skill_coverage, open_positions, computed_at)
VALUES
    (10, 120,  85, 4820.5, 0.71, 4.6, '{"first-aid":40,"driving":25}', 12, '2026-04-06 06:00:00'),
    (11,  45,  30, 1120.0, 0.67, 4.3, '{"logistics":12}',               5, '2026-04-06 06:00:00'),
    (12, 260, 210, 9875.0, 0.81, 4.8, '{"medical":70,"nursing":30}',   18, '2026-04-06 06:00:00'),
    (13,  18,  10,  312.0, 0.55, 4.0, '{"comms":4}',                    2, '2026-04-06 06:00:00');

-- ----- churn_prediction -----
INSERT INTO churn_prediction
    (volunteer_id, predicted_churn_prob, intervention_type, predicted_at, reactivated, reactivated_at, model_version)
VALUES
    (2, 0.55, 'OPPORTUNITY_REC',  '2026-04-05 07:00:00', FALSE, NULL,                  '1.0.3'),
    (4, 0.89, 'ACHIEVEMENT_NUDGE','2026-04-05 07:00:00', FALSE, NULL,                  '1.0.3'),
    (6, 0.78, 'TEAM_CHALLENGE',   '2026-04-05 07:00:00', TRUE,  '2026-04-06 12:00:00', '1.0.3'),
    (1, 0.12, 'OPPORTUNITY_REC',  '2026-04-05 07:00:00', FALSE, NULL,                  '1.0.3'),
    (3, 0.08, 'OPPORTUNITY_REC',  '2026-04-05 07:00:00', FALSE, NULL,                  '1.0.3'),
    (5, 0.22, 'TEAM_CHALLENGE',   '2026-04-05 07:00:00', FALSE, NULL,                  '1.0.3'),
    (7, 0.14, 'ACHIEVEMENT_NUDGE','2026-04-05 07:00:00', FALSE, NULL,                  '1.0.3');

-- =========================================================
--  Quick sanity check
-- =========================================================
SELECT 'predictive_model'      AS table_name, COUNT(*) AS rows_ FROM predictive_model
UNION ALL SELECT 'disaster_statistics',  COUNT(*) FROM disaster_statistics
UNION ALL SELECT 'audit_log',            COUNT(*) FROM audit_log
UNION ALL SELECT 'volunteer_statistics', COUNT(*) FROM volunteer_statistics
UNION ALL SELECT 'organisation_stats',   COUNT(*) FROM organisation_stats
UNION ALL SELECT 'churn_prediction',     COUNT(*) FROM churn_prediction;
