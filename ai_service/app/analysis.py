"""Build a formal, narrative analytics report from raw DB payloads.

The output mirrors the structure of UN Secretariat reports: Roman-numeral
chapters, numbered paragraphs, lettered sub-items for recommendations, and a
short Executive Summary (Résumé) block before the body.

Every number that appears in the prose is computed from the database — there
is no canned text that ignores the inputs.
"""

from datetime import datetime
from typing import Any, Dict, List, Optional

from .models import (
    Chapter,
    LetteredItem,
    MetricItem,
    NumberedParagraph,
    ReportData,
    ReportSection,
    SubSection,
)


# ---------------------------------------------------------------------------
# Formatters
# ---------------------------------------------------------------------------

def _i(v: Any) -> int:
    try:
        return int(v or 0)
    except (TypeError, ValueError):
        return 0


def _f(v: Any) -> float:
    try:
        return float(v or 0)
    except (TypeError, ValueError):
        return 0.0


def _fmt_int(v: Any) -> str:
    if v is None:
        return "N/A"
    try:
        return f"{int(v):,}"
    except (TypeError, ValueError):
        return str(v)


def _fmt_float(v: Any, digits: int = 2) -> str:
    if v is None:
        return "N/A"
    try:
        return f"{float(v):,.{digits}f}"
    except (TypeError, ValueError):
        return str(v)


def _fmt_pct(num: Any, den: Any) -> str:
    n, d = _f(num), _f(den)
    if d == 0:
        return "0%"
    return f"{round((n / d) * 100)}%"


def _fmt_prob(v: Any) -> str:
    return f"{round(_f(v) * 100)}%"


def _qual(rate: float) -> str:
    """Return a qualitative descriptor for a percentage 0-100."""
    if rate >= 90:
        return "excellent"
    if rate >= 75:
        return "satisfactory"
    if rate >= 60:
        return "moderate"
    if rate >= 40:
        return "concerning"
    return "critical"


def _resp_qual(minutes: float) -> str:
    if minutes <= 0:
        return "not yet measurable"
    if minutes <= 10:
        return "well within target"
    if minutes <= 30:
        return "broadly aligned with the 30-minute service-level objective"
    if minutes <= 60:
        return "above the 30-minute objective and trending towards delay"
    return "significantly above target and indicative of dispatch friction"


# ---------------------------------------------------------------------------
# Public API
# ---------------------------------------------------------------------------

def build_report_data(
    counts: Optional[Dict[str, Optional[int]]] = None,
    dashboard: Optional[Dict[str, Any]] = None,
    period_covered: Optional[str] = None,
) -> ReportData:
    counts = counts or {}
    dashboard = dashboard or {}

    disaster = dashboard.get("disaster_report") or {}
    volunteer = dashboard.get("volunteer_report") or {}
    models = dashboard.get("active_predictive_models") or []
    audit = dashboard.get("recent_audit_logs") or []
    churn = dashboard.get("high_risk_churn") or []

    generated_at = datetime.utcnow()
    period = period_covered or _default_period(generated_at)

    return ReportData(
        generated_at=generated_at,
        title="Strengthening the Coordination of Disaster Aid Operations",
        subtitle="Quarterly platform analytics report",
        source_note="Source: PostgreSQL · disaster_aid · ai_service",
        doc_codes=[
            f"A/DA/{generated_at.year}/{generated_at.month:02d}",
            f"I/AID/{generated_at.year}/Q{((generated_at.month - 1) // 3) + 1}",
        ],
        distribution="General",
        period_covered=period,
        original_language="English",
        summary_paragraphs=_summary(disaster, volunteer, models, churn, period),
        kpi_cards=_kpi_cards(disaster, volunteer),
        chapters=_chapters(disaster, volunteer, models, audit, churn, counts, period),
        disaster_kpis=_disaster_kpis(disaster),
        volunteer_kpis=_volunteer_kpis(volunteer),
        predictive_models=models,
        audit_logs=audit,
        high_risk_churn=churn,
        # Legacy fields
        metrics=_legacy_metrics(counts),
        sections=[],
        executive_summary=_summary(disaster, volunteer, models, churn, period),
        observations=[],
        recommendations=[],
    )


# ---------------------------------------------------------------------------
# Period
# ---------------------------------------------------------------------------

def _default_period(now: datetime) -> str:
    quarter = ((now.month - 1) // 3) + 1
    return f"Q{quarter} {now.year} (snapshot at {now.strftime('%Y-%m-%d')} UTC)"


# ---------------------------------------------------------------------------
# KPI cards
# ---------------------------------------------------------------------------

def _kpi_cards(
    disaster: Dict[str, Any], volunteer: Dict[str, Any]
) -> List[MetricItem]:
    return [
        MetricItem("Total Reports", _fmt_int(disaster.get("total_reports"))),
        MetricItem(
            "Verification Rate",
            _fmt_pct(disaster.get("verified_reports"), disaster.get("total_reports")),
        ),
        MetricItem(
            "Help Requests", _fmt_int(disaster.get("total_help_requests"))
        ),
        MetricItem(
            "Resolution Rate",
            _fmt_pct(
                disaster.get("resolved_requests"), disaster.get("total_help_requests")
            ),
        ),
        MetricItem(
            "Avg Response",
            f"{_fmt_float(disaster.get('avg_response_time_min'), 1)} min",
        ),
        MetricItem(
            "Active ML Models", _fmt_int(disaster.get("active_models"))
        ),
        MetricItem(
            "Volunteer Hours",
            _fmt_float(volunteer.get("total_approved_hours"), 0),
        ),
        MetricItem(
            "Avg Churn", _fmt_prob(volunteer.get("avg_churn_probability"))
        ),
        MetricItem(
            "High-Risk Volunteers", _fmt_int(volunteer.get("high_risk_volunteers"))
        ),
    ]


# ---------------------------------------------------------------------------
# Executive summary
# ---------------------------------------------------------------------------

def _summary(
    disaster: Dict[str, Any],
    volunteer: Dict[str, Any],
    models: List[Dict[str, Any]],
    churn: List[Dict[str, Any]],
    period: str,
) -> List[str]:
    total_reports = _i(disaster.get("total_reports"))
    verified = _i(disaster.get("verified_reports"))
    help_reqs = _i(disaster.get("total_help_requests"))
    resolved = _i(disaster.get("resolved_requests"))
    avg_resp = _f(disaster.get("avg_response_time_min"))
    active_models = _i(disaster.get("active_models"))

    hours = _f(volunteer.get("total_approved_hours"))
    avg_churn = _f(volunteer.get("avg_churn_probability"))
    high_risk = _i(volunteer.get("high_risk_volunteers"))
    orgs = _i(volunteer.get("organisations_tracked"))

    verif_rate = (verified / total_reports * 100) if total_reports else 0
    res_rate = (resolved / help_reqs * 100) if help_reqs else 0

    return [
        (
            f"The present report has been prepared in support of operational "
            f"oversight of the Disaster Aid platform and covers the period {period}. "
            f"It provides a quantitative overview of the activity recorded in the "
            f"production database, examines volunteer engagement and retention "
            f"trends, presents the inventory of active predictive models, and "
            f"formulates a series of recommendations."
        ),
        (
            f"During the reporting window the platform processed "
            f"{total_reports:,} citizen reports, of which {verified:,} were verified "
            f"by moderators or automated credibility scoring "
            f"({verif_rate:.0f} per cent verification rate, {_qual(verif_rate)}). "
            f"The system routed {help_reqs:,} help requests, {resolved:,} of which "
            f"reached resolution ({res_rate:.0f} per cent resolution rate). The "
            f"observed mean response time stood at {avg_resp:.1f} minutes, "
            f"{_resp_qual(avg_resp)}."
        ),
        (
            f"Volunteer engagement remains a structural priority. "
            f"{hours:,.0f} approved volunteer hours have been logged across "
            f"{orgs} tracked organisations, while the churn predictor identifies "
            f"{high_risk} volunteer{'s' if high_risk != 1 else ''} above the "
            f"70 per cent risk threshold. The average churn probability "
            f"({avg_churn * 100:.0f} per cent) calls for sustained retention work "
            f"and informs the recommendations set out in chapter VI."
        ),
        (
            f"Finally, the platform currently runs {active_models} active "
            f"predictive model{'s' if active_models != 1 else ''}, covering "
            f"disaster spread forecasting, volunteer churn prediction and "
            f"credibility scoring. A complete inventory and recent audit-log "
            f"activity are provided in chapters IV and V."
        ),
    ]


# ---------------------------------------------------------------------------
# Chapters
# ---------------------------------------------------------------------------

def _chapters(
    disaster: Dict[str, Any],
    volunteer: Dict[str, Any],
    models: List[Dict[str, Any]],
    audit: List[Dict[str, Any]],
    churn: List[Dict[str, Any]],
    counts: Dict[str, Optional[int]],
    period: str,
) -> List[Chapter]:
    return [
        _chapter_introduction(period, counts),
        _chapter_operational(disaster, counts),
        _chapter_volunteers(volunteer, churn),
        _chapter_models(models),
        _chapter_audit(audit),
        _chapter_findings(disaster, volunteer, models, churn),
    ]


# ----- I. Introduction -----------------------------------------------------

def _chapter_introduction(period: str, counts: Dict[str, Optional[int]]) -> Chapter:
    alerts = _i(counts.get("alerts"))
    incidents = _i(counts.get("incidents"))
    sensors = _i(counts.get("sensors"))
    ds_rows = _i(counts.get("disaster_stats"))
    vs_rows = _i(counts.get("volunteer_stats"))

    paragraphs = [
        NumberedParagraph(
            1,
            (
                "The Disaster Aid platform is an integrated emergency-response "
                "system that aggregates citizen reports, sensor telemetry and "
                "responder coordination into a single operational picture. The "
                "present report consolidates the analytics surfaced by the "
                f"platform's <b>/analytics</b> dashboard and covers {period}. "
                "It is intended for programme leads, operations managers and "
                "platform engineers."
            ),
        ),
        NumberedParagraph(
            2,
            (
                "The data underlying the report are queried directly from the "
                "production PostgreSQL instance through the read-only "
                "<i>data_loader</i> module of the AI service. No external sources "
                "are used; consequently the figures presented hereafter reflect "
                "the exact state of the database at the time of generation."
            ),
        ),
        NumberedParagraph(
            3,
            (
                "At the snapshot date the database held "
                f"{alerts:,} alert record{'s' if alerts != 1 else ''}, "
                f"{incidents:,} citizen incident report{'s' if incidents != 1 else ''}, "
                f"{sensors:,} sensor entr{'ies' if sensors != 1 else 'y'}, "
                f"{ds_rows:,} disaster-statistics row{'s' if ds_rows != 1 else ''} "
                f"and {vs_rows:,} volunteer-statistics row"
                f"{'s' if vs_rows != 1 else ''}. These tables are the foundation "
                "of every aggregation that follows."
            ),
        ),
        NumberedParagraph(
            4,
            (
                "The report is organised into six chapters. Chapter II reviews "
                "operational throughput and quality of moderation. Chapter III "
                "covers volunteer engagement and retention. Chapter IV inventories "
                "the active machine-learning stack. Chapter V summarises recent "
                "audit-log activity. Chapter VI consolidates findings and "
                "recommendations."
            ),
        ),
    ]
    return Chapter(roman="I.", title="Introduction", paragraphs=paragraphs)


# ----- II. Operational landscape ------------------------------------------

def _chapter_operational(
    disaster: Dict[str, Any], counts: Dict[str, Optional[int]]
) -> Chapter:
    total_reports = _i(disaster.get("total_reports"))
    verified = _i(disaster.get("verified_reports"))
    help_reqs = _i(disaster.get("total_help_requests"))
    resolved = _i(disaster.get("resolved_requests"))
    avg_resp = _f(disaster.get("avg_response_time_min"))

    verif_rate = (verified / total_reports * 100) if total_reports else 0
    res_rate = (resolved / help_reqs * 100) if help_reqs else 0
    unresolved = max(help_reqs - resolved, 0)

    reports_subsection = SubSection(
        title="Citizen reports and verification",
        paragraphs=[
            NumberedParagraph(
                5,
                (
                    f"Throughout the reporting window the platform ingested "
                    f"{total_reports:,} citizen report{'s' if total_reports != 1 else ''}. "
                    f"Of those, {verified:,} ({verif_rate:.0f} per cent) successfully "
                    f"cleared the moderation pipeline, which combines manual review "
                    f"by partner organisations with automated credibility scoring. "
                    f"The remaining "
                    f"{max(total_reports - verified, 0):,} report"
                    f"{'s' if (total_reports - verified) != 1 else ''} were either "
                    f"flagged as unverified, rejected or are still awaiting review."
                ),
            ),
            NumberedParagraph(
                6,
                (
                    f"A verification rate of {verif_rate:.0f} per cent is considered "
                    f"<b>{_qual(verif_rate)}</b>. Sustained verification quality is "
                    "essential because the credibility weight assigned to each "
                    "report directly conditions downstream dispatch decisions and "
                    "the training signal of the credibility scorer. Any prolonged "
                    "drop in this indicator should trigger a review of moderator "
                    "staffing levels and of the credibility scorer's threshold."
                ),
            ),
        ],
    )

    requests_subsection = SubSection(
        title="Help requests and resolution outcomes",
        paragraphs=[
            NumberedParagraph(
                7,
                (
                    f"Citizens and field volunteers submitted {help_reqs:,} help "
                    f"request{'s' if help_reqs != 1 else ''} during the period. "
                    f"By snapshot time, {resolved:,} of them "
                    f"({res_rate:.0f} per cent) had been formally resolved, while "
                    f"{unresolved:,} remained open or were closed without "
                    f"resolution. This indicator is closely correlated with "
                    "responder capacity in each geofenced zone and with the "
                    "matching optimiser used by the coordination module."
                ),
            ),
            NumberedParagraph(
                8,
                (
                    f"The mean response time across all incidents in the period "
                    f"was {avg_resp:.1f} minutes, a level "
                    f"{_resp_qual(avg_resp)}. Response time is computed as the "
                    "elapsed period between the moment an alert is acknowledged "
                    "by an operator and the moment a responder is formally "
                    "dispatched. Spikes in this indicator typically reflect "
                    "either an under-supply of responders or notification "
                    "fan-out delays affecting the websocket broker."
                ),
            ),
        ],
    )

    alerts = _i(counts.get("alerts"))
    sensors = _i(counts.get("sensors"))
    incidents = _i(counts.get("incidents"))

    coverage_subsection = SubSection(
        title="Coverage and underlying data volumes",
        paragraphs=[
            NumberedParagraph(
                9,
                (
                    f"The operational picture is built on top of {alerts:,} alert "
                    f"record{'s' if alerts != 1 else ''}, "
                    f"{sensors:,} sensor entr{'ies' if sensors != 1 else 'y'} and "
                    f"{incidents:,} citizen incident report"
                    f"{'s' if incidents != 1 else ''}. Where any of these volumes "
                    "drops sharply between consecutive reports, the cause should "
                    "be investigated as a matter of priority — sustained "
                    "throughput is the precondition for every metric described "
                    "in this chapter."
                ),
            ),
        ],
    )

    return Chapter(
        roman="II.",
        title="Operational Landscape",
        intro=[
            (
                "This chapter examines the volume and quality of the operational "
                "throughput recorded by the platform during the reporting period, "
                "starting from inbound citizen reports and ending with the "
                "resolution of help requests dispatched to responders."
            )
        ],
        subsections=[reports_subsection, requests_subsection, coverage_subsection],
    )


# ----- III. Volunteer engagement ------------------------------------------

def _chapter_volunteers(
    volunteer: Dict[str, Any], churn: List[Dict[str, Any]]
) -> Chapter:
    hours = _f(volunteer.get("total_approved_hours"))
    opps = _i(volunteer.get("total_opportunities"))
    avg_churn = _f(volunteer.get("avg_churn_probability"))
    high_risk = _i(volunteer.get("high_risk_volunteers"))
    orgs = _i(volunteer.get("organisations_tracked"))

    top_prob = (
        _f(churn[0].get("predicted_churn_prob")) if churn else 0.0
    )

    engagement = SubSection(
        title="Engagement and approved hours",
        paragraphs=[
            NumberedParagraph(
                10,
                (
                    f"During the reporting window, {hours:,.0f} hours of volunteer "
                    f"time were approved across {opps:,} opportunit"
                    f"{'ies' if opps != 1 else 'y'} and {orgs} tracked "
                    "organisation"
                    f"{'s' if orgs != 1 else ''}. Approved hours represent the "
                    "subset of logged hours that have passed organisational "
                    "validation; they are the canonical input to the volunteer "
                    "credibility scorer and to the gamification engine."
                ),
            ),
            NumberedParagraph(
                11,
                (
                    "Hours are aggregated per volunteer in the "
                    "<i>volunteer_statistics</i> table, which is recomputed at "
                    "regular intervals by the analytics scheduler. Reviewing the "
                    "distribution of these hours is a useful proxy for "
                    "identifying both top contributors deserving recognition and "
                    "low-engagement cohorts at risk of disengagement."
                ),
            ),
        ],
    )

    churn_section = SubSection(
        title="Churn risk and retention outlook",
        paragraphs=[
            NumberedParagraph(
                12,
                (
                    f"The churn predictor currently estimates an average churn "
                    f"probability of {avg_churn * 100:.0f} per cent across the "
                    f"active volunteer base. A total of "
                    f"{high_risk} volunteer{'s' if high_risk != 1 else ''} "
                    "exceeds the 70 per cent threshold and qualifies for a "
                    "high-risk intervention. Such interventions can take the "
                    "form of an opportunity recommendation, an inclusion in a "
                    "team challenge, or an achievement nudge, depending on the "
                    "volunteer's recent activity profile."
                ),
            ),
            NumberedParagraph(
                13,
                (
                    f"The most exposed individual case in the current snapshot "
                    f"presents a predicted churn probability of "
                    f"{top_prob * 100:.0f} per cent. Concentrated cases of this "
                    "magnitude warrant proactive contact by the responsible "
                    "organisation; the historical record indicates that early "
                    "outreach within forty-eight hours of the prediction "
                    "materially improves the reactivation rate."
                )
                if churn
                else (
                    "No volunteers presently exceed the 70 per cent churn "
                    "threshold. The platform's retention posture is healthy, "
                    "and routine monitoring is sufficient at this stage."
                ),
            ),
        ],
    )

    return Chapter(
        roman="III.",
        title="Volunteer Engagement and Retention",
        intro=[
            (
                "Volunteer activity is the structural input on which the platform "
                "depends. This chapter reviews the volume of validated "
                "contributions, situates the current churn-risk profile and "
                "highlights the cases that require organisational follow-up."
            )
        ],
        subsections=[engagement, churn_section],
    )


# ----- IV. Predictive analytics -------------------------------------------

def _chapter_models(models: List[Dict[str, Any]]) -> Chapter:
    paragraphs = [
        NumberedParagraph(
            14,
            (
                "Predictive scoring is delivered by the models registered in the "
                "<i>predictive_model</i> table. Five model families are currently "
                "supported: spread predictor, matching optimiser, credibility "
                "scorer, churn predictor and impact estimator. A model is only "
                "considered authoritative for production traffic when its "
                "<b>is_active</b> flag is set."
            ),
        ),
    ]

    if models:
        types = sorted({(m.get("type") or "UNKNOWN") for m in models})
        latest = max(
            (m for m in models if m.get("trained_at")),
            key=lambda m: m.get("trained_at"),
            default=None,
        )
        latest_phrase = (
            f"The most recently trained active model is <b>{latest.get('name')}</b> "
            f"version {latest.get('version')}, retrained on "
            f"{latest.get('trained_at')}. "
            if latest
            else ""
        )
        paragraphs.append(
            NumberedParagraph(
                15,
                (
                    f"At the time of writing, {len(models)} model"
                    f"{'s are' if len(models) != 1 else ' is'} flagged as active, "
                    f"covering the following families: "
                    f"<i>{', '.join(types)}</i>. {latest_phrase}"
                    "An exhaustive list, including version identifiers and "
                    "training timestamps, is provided in the appendix tables."
                ),
            )
        )
        paragraphs.append(
            NumberedParagraph(
                16,
                (
                    "Each model is registered with an artefact reference, an "
                    "optional metrics blob and an activation flag. Whenever a "
                    "model is retrained, the previous version should be "
                    "deactivated rather than deleted, in order to preserve a "
                    "complete production lineage and to enable rollback in case "
                    "of regression."
                ),
            )
        )
    else:
        paragraphs.append(
            NumberedParagraph(
                15,
                (
                    "No active models are currently registered. The scoring layer "
                    "is therefore inactive and downstream features depending on "
                    "model output — credibility weighting of reports, "
                    "volunteer-opportunity matching and churn prediction — are "
                    "running in degraded mode. Restoring at least the "
                    "credibility scorer and the churn predictor is the highest "
                    "priority."
                ),
            )
        )

    return Chapter(
        roman="IV.",
        title="Predictive Analytics and Machine-Learning Stack",
        paragraphs=paragraphs,
    )


# ----- V. Audit activity ---------------------------------------------------

def _chapter_audit(audit: List[Dict[str, Any]]) -> Chapter:
    if not audit:
        paragraphs = [
            NumberedParagraph(
                17,
                (
                    "The audit log is currently empty. Although this is consistent "
                    "with a freshly seeded environment, in production this state "
                    "would warrant an immediate investigation: every "
                    "configuration change, alert lifecycle transition and "
                    "machine-generated decision is expected to leave a trace in "
                    "the <i>audit_log</i> table."
                ),
            )
        ]
        return Chapter(roman="V.", title="Recent Operational Activity", paragraphs=paragraphs)

    actions = sorted({(a.get("action") or "UNKNOWN") for a in audit})
    entities = sorted({(a.get("entity_type") or "UNKNOWN") for a in audit})
    ml_count = sum(1 for a in audit if a.get("is_ml_generated"))
    human_count = len(audit) - ml_count

    return Chapter(
        roman="V.",
        title="Recent Operational Activity",
        paragraphs=[
            NumberedParagraph(
                17,
                (
                    f"The {len(audit)} most recent audit-log entr"
                    f"{'ies' if len(audit) != 1 else 'y'} touch a total of "
                    f"{len(entities)} entity type{'s' if len(entities) != 1 else ''} "
                    f"(<i>{', '.join(entities)}</i>) and exercise the following "
                    f"actions: <i>{', '.join(actions)}</i>."
                ),
            ),
            NumberedParagraph(
                18,
                (
                    f"Of those entries, {ml_count} "
                    f"({_fmt_pct(ml_count, len(audit))}) were generated by an "
                    f"automated pipeline, while {human_count} "
                    f"({_fmt_pct(human_count, len(audit))}) were attributed to a "
                    "human actor. A persistent imbalance in either direction "
                    "should be investigated: an over-reliance on automation may "
                    "indicate insufficient human supervision, whereas a complete "
                    "absence of machine-generated entries may signal an outage "
                    "of the scoring pipeline."
                ),
            ),
            NumberedParagraph(
                19,
                (
                    "The full ordered list of recent entries — including "
                    "timestamps, actors and target entities — is reproduced in "
                    "the appendix tables for reference."
                ),
            ),
        ],
    )


# ----- VI. Findings and recommendations ------------------------------------

def _chapter_findings(
    disaster: Dict[str, Any],
    volunteer: Dict[str, Any],
    models: List[Dict[str, Any]],
    churn: List[Dict[str, Any]],
) -> Chapter:
    total_reports = _i(disaster.get("total_reports"))
    verified = _i(disaster.get("verified_reports"))
    help_reqs = _i(disaster.get("total_help_requests"))
    resolved = _i(disaster.get("resolved_requests"))
    avg_resp = _f(disaster.get("avg_response_time_min"))
    avg_churn = _f(volunteer.get("avg_churn_probability"))

    verif_rate = (verified / total_reports * 100) if total_reports else 0
    res_rate = (resolved / help_reqs * 100) if help_reqs else 0

    findings: List[NumberedParagraph] = []
    n = 20

    if total_reports == 0:
        findings.append(
            NumberedParagraph(
                n,
                (
                    "No citizen reports have been recorded for the period. The "
                    "operational pipeline is, in effect, idle, and any indicator "
                    "downstream of report ingestion is structurally meaningless. "
                    "Restoring inflow is the prerequisite for every other action."
                ),
            )
        )
        n += 1
    else:
        findings.append(
            NumberedParagraph(
                n,
                (
                    f"Verification quality stands at {verif_rate:.0f} per cent, a "
                    f"level qualified as <b>{_qual(verif_rate)}</b>. "
                    + (
                        "Continuous monitoring is sufficient at this stage."
                        if verif_rate >= 75
                        else (
                            "Action is required to bring this indicator above "
                            "75 per cent in the next reporting cycle."
                        )
                    )
                ),
            )
        )
        n += 1

    if help_reqs > 0:
        findings.append(
            NumberedParagraph(
                n,
                (
                    f"Resolution rate stands at {res_rate:.0f} per cent. "
                    + (
                        "Resolution throughput is on par with operational "
                        "expectations."
                        if res_rate >= 70
                        else (
                            "The platform is therefore failing to close a "
                            "material share of incoming requests; this is "
                            "consistent either with insufficient responder "
                            "supply or with bottlenecks in the matching "
                            "optimiser."
                        )
                    )
                ),
            )
        )
        n += 1

    if avg_resp > 0:
        findings.append(
            NumberedParagraph(
                n,
                (
                    f"The mean response time of {avg_resp:.1f} minutes is "
                    f"{_resp_qual(avg_resp)}. "
                    + (
                        "No corrective action is needed beyond ongoing "
                        "monitoring."
                        if avg_resp <= 30
                        else (
                            "Engineering should review the dispatch path: "
                            "notification fan-out, geofencing rules and the "
                            "websocket broker are the most common contributors "
                            "to elevated latency."
                        )
                    )
                ),
            )
        )
        n += 1

    findings.append(
        NumberedParagraph(
            n,
            (
                f"Volunteer churn risk averages {avg_churn * 100:.0f} per cent. "
                + (
                    "The retention posture is acceptable; routine engagement "
                    "campaigns remain sufficient."
                    if avg_churn < 0.4
                    else (
                        "The platform is exposed to material attrition risk and "
                        "should activate targeted retention interventions before "
                        "the next reporting cycle."
                    )
                )
            ),
        )
    )
    n += 1

    if not models:
        findings.append(
            NumberedParagraph(
                n,
                (
                    "No active predictive models are registered. The intelligent "
                    "layer of the platform is, in practice, dormant; restoring "
                    "the credibility scorer and churn predictor is the highest "
                    "technical priority."
                ),
            )
        )
        n += 1

    # Recommendations as lettered items
    recommendations: List[LetteredItem] = []

    if verif_rate < 75 and total_reports > 0:
        recommendations.append(
            LetteredItem(
                "a)",
                (
                    "<b>Operational.</b> Increase moderator capacity or recalibrate "
                    "the credibility-scorer threshold so as to bring the "
                    "verification rate above 75 per cent in the next reporting "
                    "cycle."
                ),
            )
        )

    if avg_resp > 30:
        recommendations.append(
            LetteredItem(
                "b)" if recommendations else "a)",
                (
                    "<b>Engineering.</b> Carry out a full review of the dispatch "
                    "pipeline — notification fan-out, geofence evaluation and "
                    "websocket throughput — with a view to reducing average "
                    "response time below thirty minutes."
                ),
            )
        )

    if avg_churn >= 0.4 or churn:
        recommendations.append(
            LetteredItem(
                _next_letter(recommendations),
                (
                    "<b>Volunteer engagement.</b> Trigger targeted retention "
                    "interventions — opportunity recommendations, team "
                    "challenges, achievement nudges — for every volunteer "
                    "whose predicted churn probability exceeds forty per cent, "
                    "with a particular focus on the high-risk cohort."
                ),
            )
        )

    if not models:
        recommendations.append(
            LetteredItem(
                _next_letter(recommendations),
                (
                    "<b>Machine-learning stack.</b> Re-publish at least the "
                    "credibility scorer and the churn predictor, and document "
                    "the training pipeline so that subsequent retrainings can "
                    "be performed without engineering involvement."
                ),
            )
        )

    recommendations.append(
        LetteredItem(
            _next_letter(recommendations),
            (
                "<b>Data quality.</b> Validate, ahead of every external "
                "publication of these analytics, that the disaster and "
                "volunteer statistics tables have been re-computed within "
                "the last twenty-four hours and that no aggregation job has "
                "silently failed."
            ),
        )
    )

    recommendations.append(
        LetteredItem(
            _next_letter(recommendations),
            (
                "<b>Reporting cadence.</b> Generate the present report at least "
                "once per quarter and circulate it to programme leads and "
                "platform engineers, so that operational drift can be detected "
                "and addressed in a timely manner."
            ),
        )
    )

    return Chapter(
        roman="VI.",
        title="Findings and Recommendations",
        intro=[
            (
                "This concluding chapter consolidates the principal findings of "
                "the analysis and formulates a series of recommendations. Each "
                "recommendation is anchored in the figures presented in the "
                "preceding chapters; their adoption is intended to maintain the "
                "platform's operational quality and to anticipate emerging "
                "risks."
            )
        ],
        paragraphs=findings,
        lettered_items=recommendations,
    )


def _next_letter(existing: List[LetteredItem]) -> str:
    return f"{chr(ord('a') + len(existing))})"


# ---------------------------------------------------------------------------
# Detailed KPI tables (rendered as appendices)
# ---------------------------------------------------------------------------

def _disaster_kpis(disaster: Dict[str, Any]) -> List[MetricItem]:
    return [
        MetricItem("Total citizen reports", _fmt_int(disaster.get("total_reports"))),
        MetricItem("Verified reports", _fmt_int(disaster.get("verified_reports"))),
        MetricItem(
            "Verification rate",
            _fmt_pct(disaster.get("verified_reports"), disaster.get("total_reports")),
        ),
        MetricItem("Total help requests", _fmt_int(disaster.get("total_help_requests"))),
        MetricItem("Resolved requests", _fmt_int(disaster.get("resolved_requests"))),
        MetricItem(
            "Resolution rate",
            _fmt_pct(
                disaster.get("resolved_requests"), disaster.get("total_help_requests")
            ),
        ),
        MetricItem(
            "Avg response time (min)",
            _fmt_float(disaster.get("avg_response_time_min"), 2),
        ),
        MetricItem("Audit log entries", _fmt_int(disaster.get("audit_entries"))),
        MetricItem("Active predictive models", _fmt_int(disaster.get("active_models"))),
    ]


def _volunteer_kpis(volunteer: Dict[str, Any]) -> List[MetricItem]:
    return [
        MetricItem(
            "Total approved hours", _fmt_float(volunteer.get("total_approved_hours"), 1)
        ),
        MetricItem("Total opportunities", _fmt_int(volunteer.get("total_opportunities"))),
        MetricItem(
            "Average churn probability",
            _fmt_prob(volunteer.get("avg_churn_probability")),
        ),
        MetricItem(
            "High-risk volunteers (> 70%)",
            _fmt_int(volunteer.get("high_risk_volunteers")),
        ),
        MetricItem(
            "Organisations tracked", _fmt_int(volunteer.get("organisations_tracked"))
        ),
    ]


def _legacy_metrics(counts: Dict[str, Optional[int]]) -> List[MetricItem]:
    return [
        MetricItem("Total alerts", _fmt_int(counts.get("alerts"))),
        MetricItem("Total incidents", _fmt_int(counts.get("incidents"))),
        MetricItem("Total sensors", _fmt_int(counts.get("sensors"))),
        MetricItem("Disaster statistics rows", _fmt_int(counts.get("disaster_stats"))),
        MetricItem("Volunteer statistics rows", _fmt_int(counts.get("volunteer_stats"))),
    ]
