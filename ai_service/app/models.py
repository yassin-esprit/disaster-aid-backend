from dataclasses import dataclass, field
from datetime import datetime
from typing import Any, Dict, List, Optional


@dataclass
class MetricItem:
    label: str
    value: str


@dataclass
class ReportSection:
    title: str
    lines: List[str]


@dataclass
class NumberedParagraph:
    """A single numbered paragraph in the formal UN-style narrative."""

    number: int
    text: str


@dataclass
class LetteredItem:
    """A lettered sub-item, e.g. recommendations a), b), c)."""

    letter: str
    text: str


@dataclass
class Chapter:
    """A Roman-numeral chapter of the report.

    Each chapter has a title, optional sub-headings interleaved with
    numbered paragraphs, and optional lettered sub-items at the end
    (used for recommendations).
    """

    roman: str  # "I.", "II.", ...
    title: str
    intro: List[str] = field(default_factory=list)  # free narrative paragraphs (no number)
    subsections: List["SubSection"] = field(default_factory=list)
    paragraphs: List[NumberedParagraph] = field(default_factory=list)
    lettered_items: List[LetteredItem] = field(default_factory=list)


@dataclass
class SubSection:
    """A non-numbered sub-heading inside a chapter, optionally followed by
    its own block of numbered paragraphs.
    """

    title: str
    paragraphs: List[NumberedParagraph] = field(default_factory=list)


@dataclass
class ReportData:
    """Rich payload consumed by report.py.

    Built by analysis.py from the data_loader output.
    """

    generated_at: datetime

    # Cover/title
    title: str = "Disaster Aid — Analytics Report"
    subtitle: str = ""
    source_note: str = ""

    # Document-code header (UN-style)
    doc_codes: List[str] = field(default_factory=list)
    distribution: str = "General"
    period_covered: str = ""
    original_language: str = "English"

    # Executive summary (résumé)
    summary_paragraphs: List[str] = field(default_factory=list)

    # KPI cards displayed in the executive summary grid
    kpi_cards: List[MetricItem] = field(default_factory=list)

    # Chapters with numbered paragraphs (the bulk of the report)
    chapters: List[Chapter] = field(default_factory=list)

    # Optional KPI tables still rendered as appendices
    disaster_kpis: List[MetricItem] = field(default_factory=list)
    volunteer_kpis: List[MetricItem] = field(default_factory=list)

    # Tabular data shown as appendices
    predictive_models: List[Dict[str, Any]] = field(default_factory=list)
    audit_logs: List[Dict[str, Any]] = field(default_factory=list)
    high_risk_churn: List[Dict[str, Any]] = field(default_factory=list)

    # Legacy fields kept so older callers / tests still work.
    metrics: List[MetricItem] = field(default_factory=list)
    sections: List[ReportSection] = field(default_factory=list)
    executive_summary: List[str] = field(default_factory=list)
    observations: List[str] = field(default_factory=list)
    recommendations: List[str] = field(default_factory=list)
