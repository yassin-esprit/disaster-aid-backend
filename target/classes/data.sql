-- Seed alerts (only insert if table is empty)
INSERT INTO alert (title, description, type, severity, status, latitude, longitude, radius_km, region, country, reported_at)
SELECT * FROM (
    SELECT 'Tunis Flash Floods' AS title,
           'Severe flooding in Tunis metropolitan area after 48h of heavy rainfall' AS description,
           'FLOOD' AS type, 'CRITICAL' AS severity, 'ACTIVE' AS status,
           36.8065 AS latitude, 10.1815 AS longitude, 15.0 AS radius_km,
           'Tunis' AS region, 'Tunisia' AS country,
           TIMESTAMP '2026-04-10 08:30:00' AS reported_at
) AS tmp WHERE NOT EXISTS (SELECT 1 FROM alert LIMIT 1);

INSERT INTO alert (title, description, type, severity, status, latitude, longitude, radius_km, region, country, reported_at)
SELECT * FROM (
    SELECT 'Sfax Earthquake 4.8M', 'Magnitude 4.8 earthquake detected 12km NE of Sfax',
           'EARTHQUAKE', 'HIGH', 'ACTIVE',
           34.7406, 10.7603, 25.0,
           'Sfax', 'Tunisia',
           TIMESTAMP '2026-04-12 03:15:00'
) AS tmp WHERE NOT EXISTS (SELECT 1 FROM alert WHERE title = 'Sfax Earthquake 4.8M');

INSERT INTO alert (title, description, type, severity, status, latitude, longitude, radius_km, region, country, reported_at)
SELECT * FROM (
    SELECT 'Cap Bon Wildfire', 'Wildfire spreading across agricultural land near Nabeul',
           'WILDFIRE', 'HIGH', 'MONITORING',
           36.4513, 10.7357, 8.0,
           'Nabeul', 'Tunisia',
           TIMESTAMP '2026-04-08 14:00:00'
) AS tmp WHERE NOT EXISTS (SELECT 1 FROM alert WHERE title = 'Cap Bon Wildfire');

INSERT INTO alert (title, description, type, severity, status, latitude, longitude, radius_km, region, country, reported_at)
SELECT * FROM (
    SELECT 'Sousse Coastal Storm', 'Tropical storm warning with 90km/h winds expected',
           'STORM', 'MEDIUM', 'ACTIVE',
           35.8288, 10.6405, 30.0,
           'Sousse', 'Tunisia',
           TIMESTAMP '2026-04-11 19:45:00'
) AS tmp WHERE NOT EXISTS (SELECT 1 FROM alert WHERE title = 'Sousse Coastal Storm');

INSERT INTO alert (title, description, type, severity, status, latitude, longitude, radius_km, region, country, reported_at)
SELECT * FROM (
    SELECT 'Bizerte Landslide Risk', 'Heavy rain triggering landslide warnings in Bizerte hills',
           'LANDSLIDE', 'MEDIUM', 'MONITORING',
           37.2744, 9.8739, 5.0,
           'Bizerte', 'Tunisia',
           TIMESTAMP '2026-04-09 11:20:00'
) AS tmp WHERE NOT EXISTS (SELECT 1 FROM alert WHERE title = 'Bizerte Landslide Risk');

INSERT INTO alert (title, description, type, severity, status, latitude, longitude, radius_km, region, country, reported_at)
SELECT * FROM (
    SELECT 'Kairouan Drought Emergency', 'Severe water shortage affecting 50,000+ residents',
           'OTHER', 'LOW', 'RESOLVED',
           35.6781, 10.0963, 40.0,
           'Kairouan', 'Tunisia',
           TIMESTAMP '2026-03-15 09:00:00'
) AS tmp WHERE NOT EXISTS (SELECT 1 FROM alert WHERE title = 'Kairouan Drought Emergency');

-- Seed SLA metrics
INSERT INTO sla_metric (alert_id, incident_at, acknowledged_at, resolved_at, time_to_acknowledge_min, time_to_resolve_min, total_resolution_min, sla_status)
SELECT 1, TIMESTAMP '2026-04-10 08:30:00', TIMESTAMP '2026-04-10 08:45:00', NULL, 15, NULL, NULL, 'AT_RISK'
WHERE NOT EXISTS (SELECT 1 FROM sla_metric WHERE alert_id = 1 AND incident_at = TIMESTAMP '2026-04-10 08:30:00');

INSERT INTO sla_metric (alert_id, incident_at, acknowledged_at, resolved_at, time_to_acknowledge_min, time_to_resolve_min, total_resolution_min, sla_status)
SELECT 2, TIMESTAMP '2026-04-12 03:15:00', TIMESTAMP '2026-04-12 03:22:00', NULL, 7, NULL, NULL, 'WITHIN_SLA'
WHERE NOT EXISTS (SELECT 1 FROM sla_metric WHERE alert_id = 2 AND incident_at = TIMESTAMP '2026-04-12 03:15:00');

INSERT INTO sla_metric (alert_id, incident_at, acknowledged_at, resolved_at, time_to_acknowledge_min, time_to_resolve_min, total_resolution_min, sla_status)
SELECT 3, TIMESTAMP '2026-04-08 14:00:00', TIMESTAMP '2026-04-08 14:45:00', NULL, 45, NULL, NULL, 'BREACHED'
WHERE NOT EXISTS (SELECT 1 FROM sla_metric WHERE alert_id = 3 AND incident_at = TIMESTAMP '2026-04-08 14:00:00');

INSERT INTO sla_metric (alert_id, incident_at, acknowledged_at, resolved_at, time_to_acknowledge_min, time_to_resolve_min, total_resolution_min, sla_status)
SELECT 6, TIMESTAMP '2026-03-15 09:00:00', TIMESTAMP '2026-03-15 09:10:00', TIMESTAMP '2026-04-01 12:00:00', 10, 24170, 24180, 'WITHIN_SLA'
WHERE NOT EXISTS (SELECT 1 FROM sla_metric WHERE alert_id = 6 AND incident_at = TIMESTAMP '2026-03-15 09:00:00');

-- Seed alert rules
INSERT INTO alert_rule (name, description, field, operator, threshold, action, target_severity, active, trigger_count, created_at)
SELECT 'Critical if > 100 reports', 'Escalate to CRITICAL when total reports exceed 100',
       'TOTAL_REPORTS', 'GREATER_THAN', 100, 'CHANGE_SEVERITY', 'CRITICAL', true, 0, TIMESTAMP '2026-04-01 00:00:00'
WHERE NOT EXISTS (SELECT 1 FROM alert_rule WHERE name = 'Critical if > 100 reports');

INSERT INTO alert_rule (name, description, field, operator, threshold, action, target_severity, active, trigger_count, created_at)
SELECT 'Auto-resolve if 0 requests', 'Auto-resolve alerts with zero help requests',
       'HELP_REQUESTS', 'EQUALS', 0, 'AUTO_RESOLVE', NULL, true, 0, TIMESTAMP '2026-04-01 00:00:00'
WHERE NOT EXISTS (SELECT 1 FROM alert_rule WHERE name = 'Auto-resolve if 0 requests');

INSERT INTO alert_rule (name, description, field, operator, threshold, action, target_severity, active, trigger_count, created_at)
SELECT 'Low responders warning', 'Notify when responders drop below 5',
       'ACTIVE_RESPONDERS', 'LESS_THAN', 5, 'NOTIFY', NULL, true, 0, TIMESTAMP '2026-04-01 00:00:00'
WHERE NOT EXISTS (SELECT 1 FROM alert_rule WHERE name = 'Low responders warning');

-- Seed alert snapshots for temporal replay (Tunis Floods - alert_id=1)
INSERT INTO alert_snapshot (alert_id, status, severity, radius_km, total_reports, total_help_requests, active_responders, snapshot_at)
SELECT 1, 'ACTIVE', 'MEDIUM', 5.0, 12, 8, 3, TIMESTAMP '2026-04-10 08:30:00'
WHERE NOT EXISTS (SELECT 1 FROM alert_snapshot WHERE alert_id = 1 AND snapshot_at = TIMESTAMP '2026-04-10 08:30:00');

INSERT INTO alert_snapshot (alert_id, status, severity, radius_km, total_reports, total_help_requests, active_responders, snapshot_at)
SELECT 1, 'ACTIVE', 'HIGH', 8.0, 45, 30, 12, TIMESTAMP '2026-04-10 12:00:00'
WHERE NOT EXISTS (SELECT 1 FROM alert_snapshot WHERE alert_id = 1 AND snapshot_at = TIMESTAMP '2026-04-10 12:00:00');

INSERT INTO alert_snapshot (alert_id, status, severity, radius_km, total_reports, total_help_requests, active_responders, snapshot_at)
SELECT 1, 'ACTIVE', 'HIGH', 12.0, 120, 75, 25, TIMESTAMP '2026-04-10 18:00:00'
WHERE NOT EXISTS (SELECT 1 FROM alert_snapshot WHERE alert_id = 1 AND snapshot_at = TIMESTAMP '2026-04-10 18:00:00');

INSERT INTO alert_snapshot (alert_id, status, severity, radius_km, total_reports, total_help_requests, active_responders, snapshot_at)
SELECT 1, 'ACTIVE', 'CRITICAL', 15.0, 245, 120, 35, TIMESTAMP '2026-04-11 06:00:00'
WHERE NOT EXISTS (SELECT 1 FROM alert_snapshot WHERE alert_id = 1 AND snapshot_at = TIMESTAMP '2026-04-11 06:00:00');

INSERT INTO alert_snapshot (alert_id, status, severity, radius_km, total_reports, total_help_requests, active_responders, snapshot_at)
SELECT 1, 'ACTIVE', 'CRITICAL', 15.0, 280, 130, 42, TIMESTAMP '2026-04-11 18:00:00'
WHERE NOT EXISTS (SELECT 1 FROM alert_snapshot WHERE alert_id = 1 AND snapshot_at = TIMESTAMP '2026-04-11 18:00:00');

-- Snapshots for Sfax Earthquake - alert_id=2
INSERT INTO alert_snapshot (alert_id, status, severity, radius_km, total_reports, total_help_requests, active_responders, snapshot_at)
SELECT 2, 'ACTIVE', 'HIGH', 15.0, 30, 20, 8, TIMESTAMP '2026-04-12 03:15:00'
WHERE NOT EXISTS (SELECT 1 FROM alert_snapshot WHERE alert_id = 2 AND snapshot_at = TIMESTAMP '2026-04-12 03:15:00');

INSERT INTO alert_snapshot (alert_id, status, severity, radius_km, total_reports, total_help_requests, active_responders, snapshot_at)
SELECT 2, 'ACTIVE', 'HIGH', 22.0, 85, 55, 18, TIMESTAMP '2026-04-12 12:00:00'
WHERE NOT EXISTS (SELECT 1 FROM alert_snapshot WHERE alert_id = 2 AND snapshot_at = TIMESTAMP '2026-04-12 12:00:00');

INSERT INTO alert_snapshot (alert_id, status, severity, radius_km, total_reports, total_help_requests, active_responders, snapshot_at)
SELECT 2, 'ACTIVE', 'HIGH', 25.0, 150, 90, 30, TIMESTAMP '2026-04-13 00:00:00'
WHERE NOT EXISTS (SELECT 1 FROM alert_snapshot WHERE alert_id = 2 AND snapshot_at = TIMESTAMP '2026-04-13 00:00:00');
