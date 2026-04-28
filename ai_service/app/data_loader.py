import os
from typing import Any, Dict, List, Optional

import psycopg2
from psycopg2.extras import RealDictCursor


# ---------------------------------------------------------------------------
# Connection
# ---------------------------------------------------------------------------

def _connect():
    host = os.getenv("DB_HOST", "localhost")
    port = int(os.getenv("DB_PORT", "5432"))
    name = os.getenv("DB_NAME", "disaster_aid")
    user = os.getenv("DB_USER", "postgres")
    password = os.getenv("DB_PASSWORD", "postgres")
    return psycopg2.connect(
        host=host,
        port=port,
        dbname=name,
        user=user,
        password=password,
    )


# ---------------------------------------------------------------------------
# Helpers
# ---------------------------------------------------------------------------

def _safe_count(cur, table: str) -> Optional[int]:
    try:
        cur.execute(f"SELECT COUNT(*) FROM {table}")
        return int(cur.fetchone()[0])
    except Exception:
        return None


def _safe_one(cur, sql: str, params: tuple = ()) -> Optional[tuple]:
    try:
        cur.execute(sql, params)
        return cur.fetchone()
    except Exception:
        return None


def _safe_all(cur, sql: str, params: tuple = ()) -> List[Dict[str, Any]]:
    try:
        cur.execute(sql, params)
        return [dict(row) for row in cur.fetchall()]
    except Exception:
        return []


# ---------------------------------------------------------------------------
# Existing API (kept for backwards compatibility)
# ---------------------------------------------------------------------------

def fetch_core_counts() -> Dict[str, Optional[int]]:
    """Fetch basic counts used in the PDF report.

    Returns None for any missing table or query failure.
    """
    with _connect() as conn:
        with conn.cursor() as cur:
            return {
                "alerts": _safe_count(cur, "alert"),
                "incidents": _safe_count(cur, "incident_report"),
                "sensors": _safe_count(cur, "sensors"),
                "disaster_stats": _safe_count(cur, "disaster_statistics"),
                "volunteer_stats": _safe_count(cur, "volunteer_statistics"),
            }


# ---------------------------------------------------------------------------
# Analytics dashboard data
# Mirrors the five datasets loaded by AnalyticsDashboardComponent at
# /analytics in the frontend:
#   1. GET /analytics/disaster/report
#   2. GET /analytics/volunteer/report?highRiskThreshold=...
#   3. GET /analytics/disaster/predictive-models?activeOnly=true
#   4. GET /analytics/disaster/audit-logs       (top 100, UI shows 8)
#   5. GET /analytics/volunteer/churn/high-risk?threshold=...
# ---------------------------------------------------------------------------

def fetch_disaster_report() -> Dict[str, Any]:
    """Disaster KPIs aggregated from disaster_statistics + audit_log + predictive_model.

    Matches DisasterAnalyticsService.getGlobalReport().
    """
    with _connect() as conn:
        with conn.cursor() as cur:
            row = _safe_one(
                cur,
                """
                SELECT COALESCE(SUM(total_reports), 0)         AS total_reports,
                       COALESCE(SUM(verified_reports), 0)      AS verified_reports,
                       COALESCE(SUM(total_help_requests), 0)   AS total_help_requests,
                       COALESCE(SUM(resolved_requests), 0)     AS resolved_requests,
                       COALESCE(AVG(avg_response_time_min), 0) AS avg_response_time_min
                FROM disaster_statistics
                """,
            )
            audit_count = _safe_count(cur, "audit_log") or 0
            active_models_row = _safe_one(
                cur,
                "SELECT COUNT(*) FROM predictive_model WHERE is_active = TRUE",
            )
            active_models = int(active_models_row[0]) if active_models_row else 0

            return {
                "total_reports": int(row[0]) if row else 0,
                "verified_reports": int(row[1]) if row else 0,
                "total_help_requests": int(row[2]) if row else 0,
                "resolved_requests": int(row[3]) if row else 0,
                "avg_response_time_min": float(row[4]) if row else 0.0,
                "audit_entries": audit_count,
                "active_models": active_models,
            }


def fetch_volunteer_report(high_risk_threshold: float = 0.7) -> Dict[str, Any]:
    """Volunteer KPIs aggregated from volunteer_statistics + churn_prediction + organisation_stats.

    Matches VolunteerAnalyticsService.getGlobalReport(highRiskThreshold).
    """
    with _connect() as conn:
        with conn.cursor() as cur:
            row = _safe_one(
                cur,
                """
                SELECT COALESCE(SUM(total_approved_hours), 0) AS total_approved_hours,
                       COALESCE(SUM(total_opportunities), 0)  AS total_opportunities,
                       COALESCE(AVG(churn_probability), 0)    AS avg_churn_probability
                FROM volunteer_statistics
                """,
            )
            high_risk_row = _safe_one(
                cur,
                "SELECT COUNT(*) FROM churn_prediction WHERE predicted_churn_prob > %s",
                (high_risk_threshold,),
            )
            high_risk = int(high_risk_row[0]) if high_risk_row else 0
            org_count = _safe_count(cur, "organisation_stats") or 0

            return {
                "total_approved_hours": float(row[0]) if row else 0.0,
                "total_opportunities": int(row[1]) if row else 0,
                "avg_churn_probability": float(row[2]) if row else 0.0,
                "high_risk_volunteers": high_risk,
                "organisations_tracked": org_count,
            }


def fetch_active_predictive_models() -> List[Dict[str, Any]]:
    """Active predictive models, newest first (UI: /analytics 'Active models' card)."""
    with _connect() as conn:
        with conn.cursor(cursor_factory=RealDictCursor) as cur:
            return _safe_all(
                cur,
                """
                SELECT model_id, name, version, type, artifact_url,
                       metrics, is_active, trained_at
                FROM predictive_model
                WHERE is_active = TRUE
                ORDER BY trained_at DESC NULLS LAST
                """,
            )


def fetch_recent_audit_logs(limit: int = 8) -> List[Dict[str, Any]]:
    """Most recent audit log entries (UI: /analytics 'Recent activity' panel)."""
    with _connect() as conn:
        with conn.cursor(cursor_factory=RealDictCursor) as cur:
            return _safe_all(
                cur,
                """
                SELECT log_id, actor_id, action, entity_type, entity_id,
                       previous_value, new_value, ip_address, user_agent,
                       timestamp, is_ml_generated, ml_model_version
                FROM audit_log
                ORDER BY timestamp DESC
                LIMIT %s
                """,
                (limit,),
            )


def fetch_high_risk_churn(
    threshold: float = 0.7, limit: int = 8
) -> List[Dict[str, Any]]:
    """High-risk volunteer churn predictions (UI: /analytics 'High-risk churn' panel)."""
    with _connect() as conn:
        with conn.cursor(cursor_factory=RealDictCursor) as cur:
            return _safe_all(
                cur,
                """
                SELECT prediction_id, volunteer_id, predicted_churn_prob,
                       intervention_type, predicted_at, reactivated,
                       reactivated_at, model_version
                FROM churn_prediction
                WHERE predicted_churn_prob > %s
                ORDER BY predicted_churn_prob DESC
                LIMIT %s
                """,
                (threshold, limit),
            )


def fetch_analytics_dashboard(
    high_risk_threshold: float = 0.7, list_limit: int = 8
) -> Dict[str, Any]:
    """One-shot loader matching everything AnalyticsDashboardComponent shows.

    Useful for the PDF report — gives you the same five datasets in a single
    structure, with the same default thresholds the UI uses.
    """
    return {
        "disaster_report": fetch_disaster_report(),
        "volunteer_report": fetch_volunteer_report(high_risk_threshold),
        "active_predictive_models": fetch_active_predictive_models(),
        "recent_audit_logs": fetch_recent_audit_logs(list_limit),
        "high_risk_churn": fetch_high_risk_churn(high_risk_threshold, list_limit),
    }
