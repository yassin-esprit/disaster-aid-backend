"""Render the analytics report as a formal, multi-page PDF.

Layout follows the visual conventions of UN Secretariat reports:
    - Cover page with document codes, distribution and language
    - "Summary" block in italics
    - Roman-numeral chapters
    - Numbered paragraphs with hanging indent
    - Lettered recommendations (a, b, c)
    - Header / footer with document codes and page numbers on every page
"""

import os
from datetime import datetime
from typing import Any, Dict, List, Optional

from reportlab.lib import colors
from reportlab.lib.enums import TA_CENTER, TA_JUSTIFY, TA_LEFT, TA_RIGHT
from reportlab.lib.pagesizes import A4
from reportlab.lib.styles import ParagraphStyle, getSampleStyleSheet
from reportlab.lib.units import cm, mm
from reportlab.platypus import (
    BaseDocTemplate,
    Frame,
    HRFlowable,
    KeepTogether,
    NextPageTemplate,
    PageBreak,
    PageTemplate,
    Paragraph,
    Spacer,
    Table,
    TableStyle,
)

from .models import Chapter, LetteredItem, MetricItem, NumberedParagraph, ReportData


# ---------------------------------------------------------------------------
# Theme
# ---------------------------------------------------------------------------

PRIMARY = colors.HexColor("#0f172a")        # slate-900
ACCENT = colors.HexColor("#1d4ed8")         # blue-700
SOFT_BG = colors.HexColor("#f1f5f9")        # slate-100
HEADER_BG = colors.HexColor("#1e293b")      # slate-800
HEADER_TEXT = colors.HexColor("#f8fafc")    # slate-50
GRID = colors.HexColor("#cbd5e1")           # slate-300
MUTED = colors.HexColor("#64748b")          # slate-500
RULE = colors.HexColor("#0f172a")           # slate-900


def _styles():
    base = getSampleStyleSheet()
    return {
        "doc_header": ParagraphStyle(
            "DocHeader",
            parent=base["Normal"],
            fontName="Helvetica",
            fontSize=10,
            leading=14,
            textColor=PRIMARY,
        ),
        "doc_codes": ParagraphStyle(
            "DocCodes",
            parent=base["Normal"],
            fontName="Helvetica-Bold",
            fontSize=11,
            leading=14,
            textColor=PRIMARY,
            alignment=TA_RIGHT,
        ),
        "title": ParagraphStyle(
            "Title",
            parent=base["Title"],
            fontName="Times-Bold",
            fontSize=20,
            leading=24,
            textColor=PRIMARY,
            spaceBefore=16,
            spaceAfter=4,
            alignment=TA_LEFT,
        ),
        "subtitle": ParagraphStyle(
            "Subtitle",
            parent=base["Normal"],
            fontName="Times-Italic",
            fontSize=12,
            leading=15,
            textColor=MUTED,
            spaceAfter=10,
        ),
        "author": ParagraphStyle(
            "Author",
            parent=base["Normal"],
            fontName="Times-Italic",
            fontSize=10,
            leading=13,
            textColor=PRIMARY,
            spaceAfter=10,
        ),
        "summary_label": ParagraphStyle(
            "SummaryLabel",
            parent=base["Normal"],
            fontName="Times-BoldItalic",
            fontSize=12,
            leading=15,
            textColor=PRIMARY,
            spaceBefore=14,
            spaceAfter=8,
        ),
        "summary_body": ParagraphStyle(
            "SummaryBody",
            parent=base["BodyText"],
            fontName="Times-Roman",
            fontSize=10.5,
            leading=15,
            textColor=PRIMARY,
            alignment=TA_JUSTIFY,
            spaceAfter=8,
            firstLineIndent=14,
        ),
        "chapter_roman": ParagraphStyle(
            "ChapterRoman",
            fontName="Times-Bold",
            fontSize=14,
            leading=18,
            textColor=PRIMARY,
            spaceBefore=18,
            spaceAfter=2,
            alignment=TA_LEFT,
        ),
        "chapter_title": ParagraphStyle(
            "ChapterTitle",
            fontName="Times-Bold",
            fontSize=14,
            leading=18,
            textColor=PRIMARY,
            spaceAfter=10,
            alignment=TA_LEFT,
        ),
        "subsection": ParagraphStyle(
            "Subsection",
            fontName="Times-BoldItalic",
            fontSize=11.5,
            leading=14,
            textColor=PRIMARY,
            spaceBefore=10,
            spaceAfter=6,
            alignment=TA_LEFT,
        ),
        "intro": ParagraphStyle(
            "Intro",
            fontName="Times-Italic",
            fontSize=10.5,
            leading=15,
            textColor=PRIMARY,
            alignment=TA_JUSTIFY,
            spaceAfter=8,
        ),
        "para": ParagraphStyle(
            "Para",
            fontName="Times-Roman",
            fontSize=10.5,
            leading=15,
            textColor=PRIMARY,
            alignment=TA_JUSTIFY,
            leftIndent=18,
            firstLineIndent=-18,
            spaceAfter=6,
        ),
        "lettered": ParagraphStyle(
            "Lettered",
            fontName="Times-Roman",
            fontSize=10.5,
            leading=15,
            textColor=PRIMARY,
            alignment=TA_JUSTIFY,
            leftIndent=36,
            firstLineIndent=-18,
            spaceAfter=6,
        ),
        "section": ParagraphStyle(
            "Section",
            fontName="Times-Bold",
            fontSize=12,
            leading=15,
            textColor=PRIMARY,
            spaceBefore=12,
            spaceAfter=8,
        ),
        "kpi_label": ParagraphStyle(
            "KPILabel",
            fontName="Helvetica",
            fontSize=8,
            leading=10,
            textColor=MUTED,
            alignment=TA_CENTER,
        ),
        "kpi_value": ParagraphStyle(
            "KPIValue",
            fontName="Helvetica-Bold",
            fontSize=13,
            leading=16,
            textColor=PRIMARY,
            alignment=TA_CENTER,
        ),
    }


# ---------------------------------------------------------------------------
# Page decoration
# ---------------------------------------------------------------------------

def _make_decoration(report: ReportData):
    """Return (cover_decoration, body_decoration) callbacks."""

    codes = " · ".join(report.doc_codes) if report.doc_codes else ""

    def _cover(canvas, doc):
        canvas.saveState()
        width, height = A4
        # Slim accent bar at the top of the cover
        canvas.setFillColor(ACCENT)
        canvas.rect(0, height - 4 * mm, width, 4 * mm, stroke=0, fill=1)
        # Footer with the codes
        canvas.setStrokeColor(GRID)
        canvas.setLineWidth(0.4)
        canvas.line(2 * cm, 1.6 * cm, width - 2 * cm, 1.6 * cm)
        canvas.setFillColor(MUTED)
        canvas.setFont("Helvetica", 8)
        canvas.drawString(2 * cm, 1.0 * cm, codes)
        canvas.drawRightString(width - 2 * cm, 1.0 * cm, "Cover")
        canvas.restoreState()

    def _body(canvas, doc):
        canvas.saveState()
        width, height = A4
        # Top: document codes left, project name right
        canvas.setFont("Helvetica-Bold", 9)
        canvas.setFillColor(PRIMARY)
        if codes:
            canvas.drawString(2 * cm, height - 1.4 * cm, codes)
        canvas.setFont("Helvetica", 9)
        canvas.setFillColor(MUTED)
        canvas.drawRightString(
            width - 2 * cm, height - 1.4 * cm, "Disaster Aid · Analytics Report"
        )
        # Thin rule under the top header strip
        canvas.setStrokeColor(GRID)
        canvas.setLineWidth(0.4)
        canvas.line(2 * cm, height - 1.7 * cm, width - 2 * cm, height - 1.7 * cm)

        # Footer rule
        canvas.line(2 * cm, 1.6 * cm, width - 2 * cm, 1.6 * cm)
        canvas.setFillColor(MUTED)
        canvas.setFont("Helvetica", 8)
        canvas.drawString(2 * cm, 1.0 * cm, "ai_service · disaster-aid")
        canvas.drawRightString(width - 2 * cm, 1.0 * cm, f"Page {doc.page}")
        canvas.restoreState()

    return _cover, _body


# ---------------------------------------------------------------------------
# Cover-page builder
# ---------------------------------------------------------------------------

def _cover_page(report: ReportData, styles) -> List[Any]:
    flow: List[Any] = []

    # Left column: "Disaster Aid Platform" / "Analytics Service"
    # Right column: document codes
    left = Paragraph(
        "<b>Disaster Aid Platform</b><br/>"
        "Analytics Service<br/>"
        "<i>Operational reporting unit</i>",
        styles["doc_header"],
    )
    right = Paragraph(
        "<br/>".join(report.doc_codes) if report.doc_codes else "",
        styles["doc_codes"],
    )

    header = Table(
        [[left, right]],
        colWidths=[10 * cm, 7 * cm],
        style=TableStyle(
            [
                ("VALIGN", (0, 0), (-1, -1), "TOP"),
                ("LEFTPADDING", (0, 0), (-1, -1), 0),
                ("RIGHTPADDING", (0, 0), (-1, -1), 0),
            ]
        ),
    )
    flow.append(header)
    flow.append(Spacer(1, 4))

    flow.append(
        HRFlowable(
            width="100%",
            thickness=0.6,
            color=RULE,
            spaceBefore=4,
            spaceAfter=4,
        )
    )

    meta = Paragraph(
        f"Distr.: {report.distribution} · {report.generated_at.strftime('%d %B %Y')} "
        f"· Original: {report.original_language}",
        styles["doc_header"],
    )
    flow.append(meta)
    flow.append(
        Paragraph(
            f"Reporting period: {report.period_covered}",
            styles["doc_header"],
        )
    )

    flow.append(Spacer(1, 28))
    flow.append(Paragraph(report.title, styles["title"]))
    if report.subtitle:
        flow.append(Paragraph(report.subtitle, styles["subtitle"]))
    flow.append(
        Paragraph(
            "<i>Report of the Analytics Service of the Disaster Aid Platform</i>",
            styles["author"],
        )
    )

    flow.append(
        HRFlowable(
            width="40%",
            thickness=0.6,
            color=RULE,
            spaceBefore=12,
            spaceAfter=8,
            hAlign="LEFT",
        )
    )
    flow.append(Paragraph("Summary", styles["summary_label"]))
    for paragraph in report.summary_paragraphs:
        flow.append(Paragraph(paragraph, styles["summary_body"]))

    flow.append(Spacer(1, 16))
    grid = _kpi_grid(report.kpi_cards, styles)
    if grid is not None:
        flow.append(grid)

    flow.append(Spacer(1, 14))
    flow.append(
        Paragraph(
            f"<i>{report.source_note}</i>",
            styles["doc_header"],
        )
    )
    return flow


# ---------------------------------------------------------------------------
# KPI grid
# ---------------------------------------------------------------------------

def _kpi_grid(cards: List[MetricItem], styles) -> Optional[Table]:
    if not cards:
        return None

    cols = 3
    cells: List[List[Any]] = []
    row: List[Any] = []
    for card in cards:
        cell = [
            Paragraph(card.label.upper(), styles["kpi_label"]),
            Spacer(1, 4),
            Paragraph(str(card.value), styles["kpi_value"]),
        ]
        row.append(cell)
        if len(row) == cols:
            cells.append(row)
            row = []
    if row:
        while len(row) < cols:
            row.append("")
        cells.append(row)

    col_width = (A4[0] - 4 * cm) / cols
    table = Table(cells, colWidths=[col_width] * cols)
    table.setStyle(
        TableStyle(
            [
                ("BACKGROUND", (0, 0), (-1, -1), SOFT_BG),
                ("BOX", (0, 0), (-1, -1), 0.4, GRID),
                ("INNERGRID", (0, 0), (-1, -1), 0.4, GRID),
                ("VALIGN", (0, 0), (-1, -1), "MIDDLE"),
                ("LEFTPADDING", (0, 0), (-1, -1), 8),
                ("RIGHTPADDING", (0, 0), (-1, -1), 8),
                ("TOPPADDING", (0, 0), (-1, -1), 12),
                ("BOTTOMPADDING", (0, 0), (-1, -1), 12),
            ]
        )
    )
    return table


# ---------------------------------------------------------------------------
# Chapter rendering
# ---------------------------------------------------------------------------

def _chapter_block(chapter: Chapter, styles) -> List[Any]:
    flow: List[Any] = []

    # Roman-numeral header line
    flow.append(
        Paragraph(
            f"<font size='14'><b>{chapter.roman}</b></font>&nbsp;&nbsp;"
            f"<b>{chapter.title}</b>",
            styles["chapter_roman"],
        )
    )
    flow.append(
        HRFlowable(
            width="100%",
            thickness=0.5,
            color=RULE,
            spaceBefore=2,
            spaceAfter=10,
        )
    )

    # Free-form intro
    for line in chapter.intro:
        flow.append(Paragraph(line, styles["intro"]))

    # Direct numbered paragraphs (no subsection)
    for p in chapter.paragraphs:
        flow.append(_numbered(p, styles))

    # Subsections
    for sub in chapter.subsections:
        flow.append(Paragraph(sub.title, styles["subsection"]))
        for p in sub.paragraphs:
            flow.append(_numbered(p, styles))

    # Lettered items (used for recommendations)
    for item in chapter.lettered_items:
        flow.append(_lettered(item, styles))

    return flow


def _numbered(p: NumberedParagraph, styles) -> Paragraph:
    return Paragraph(f"<b>{p.number}.</b>&nbsp;&nbsp;{p.text}", styles["para"])


def _lettered(item: LetteredItem, styles) -> Paragraph:
    return Paragraph(
        f"<b>{item.letter}</b>&nbsp;&nbsp;{item.text}", styles["lettered"]
    )


# ---------------------------------------------------------------------------
# Appendix tables
# ---------------------------------------------------------------------------

def _kv_table(items: List[MetricItem]) -> Optional[Table]:
    if not items:
        return None
    rows = [[item.label, str(item.value)] for item in items]
    table = Table(rows, colWidths=[8 * cm, 8 * cm])
    table.setStyle(
        TableStyle(
            [
                ("FONTNAME", (0, 0), (0, -1), "Helvetica"),
                ("FONTNAME", (1, 0), (1, -1), "Helvetica-Bold"),
                ("FONTSIZE", (0, 0), (-1, -1), 10),
                ("TEXTCOLOR", (0, 0), (0, -1), MUTED),
                ("TEXTCOLOR", (1, 0), (1, -1), PRIMARY),
                ("ROWBACKGROUNDS", (0, 0), (-1, -1), [colors.white, SOFT_BG]),
                ("LINEBELOW", (0, 0), (-1, -1), 0.3, GRID),
                ("LEFTPADDING", (0, 0), (-1, -1), 8),
                ("RIGHTPADDING", (0, 0), (-1, -1), 8),
                ("TOPPADDING", (0, 0), (-1, -1), 6),
                ("BOTTOMPADDING", (0, 0), (-1, -1), 6),
            ]
        )
    )
    return table


def _data_table(
    headers: List[str],
    rows: List[List[Any]],
    col_widths: Optional[List[float]] = None,
):
    if not rows:
        return None
    data = [headers] + rows
    table = Table(data, colWidths=col_widths, repeatRows=1)
    table.setStyle(
        TableStyle(
            [
                ("BACKGROUND", (0, 0), (-1, 0), HEADER_BG),
                ("TEXTCOLOR", (0, 0), (-1, 0), HEADER_TEXT),
                ("FONTNAME", (0, 0), (-1, 0), "Helvetica-Bold"),
                ("FONTSIZE", (0, 0), (-1, 0), 9),
                ("FONTNAME", (0, 1), (-1, -1), "Helvetica"),
                ("FONTSIZE", (0, 1), (-1, -1), 8.5),
                ("TEXTCOLOR", (0, 1), (-1, -1), PRIMARY),
                ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, SOFT_BG]),
                ("GRID", (0, 0), (-1, -1), 0.3, GRID),
                ("VALIGN", (0, 0), (-1, -1), "MIDDLE"),
                ("LEFTPADDING", (0, 0), (-1, -1), 6),
                ("RIGHTPADDING", (0, 0), (-1, -1), 6),
                ("TOPPADDING", (0, 0), (-1, -1), 5),
                ("BOTTOMPADDING", (0, 0), (-1, -1), 5),
            ]
        )
    )
    return table


def _trim(value: Any, max_len: int = 40) -> str:
    if value is None:
        return "—"
    s = str(value)
    return s if len(s) <= max_len else s[: max_len - 1] + "…"


def _fmt_dt(value: Any) -> str:
    if value is None:
        return "—"
    if isinstance(value, datetime):
        return value.strftime("%Y-%m-%d %H:%M")
    return str(value)


def _appendix(report: ReportData, styles) -> List[Any]:
    flow: List[Any] = [
        Paragraph("Annex — Detailed metrics and tabular data", styles["chapter_roman"]),
        HRFlowable(
            width="100%",
            thickness=0.5,
            color=RULE,
            spaceBefore=2,
            spaceAfter=10,
        ),
    ]

    if report.disaster_kpis:
        flow.append(Paragraph("A. Disaster analytics — full KPI list", styles["section"]))
        kv = _kv_table(report.disaster_kpis)
        if kv is not None:
            flow.append(kv)

    if report.volunteer_kpis:
        flow.append(Paragraph("B. Volunteer analytics — full KPI list", styles["section"]))
        kv = _kv_table(report.volunteer_kpis)
        if kv is not None:
            flow.append(kv)

    if report.predictive_models:
        flow.append(Paragraph("C. Active predictive models", styles["section"]))
        rows = [
            [
                _trim(m.get("name"), 24),
                _trim(m.get("version"), 12),
                _trim(m.get("type"), 22),
                _fmt_dt(m.get("trained_at")),
            ]
            for m in report.predictive_models
        ]
        t = _data_table(
            ["Name", "Version", "Type", "Trained at"],
            rows,
            col_widths=[6 * cm, 2.5 * cm, 4.5 * cm, 4 * cm],
        )
        if t is not None:
            flow.append(t)

    if report.audit_logs:
        flow.append(Paragraph("D. Recent audit-log entries", styles["section"]))
        rows = [
            [
                _fmt_dt(a.get("timestamp")),
                _trim(a.get("action"), 18),
                _trim(a.get("entity_type"), 16),
                _trim(a.get("entity_id"), 8),
                _trim(a.get("actor_id"), 8),
                "ML" if a.get("is_ml_generated") else "human",
            ]
            for a in report.audit_logs
        ]
        t = _data_table(
            ["Timestamp", "Action", "Entity", "ID", "Actor", "Source"],
            rows,
            col_widths=[3.4 * cm, 2.6 * cm, 2.8 * cm, 2 * cm, 2 * cm, 2.2 * cm],
        )
        if t is not None:
            flow.append(t)

    if report.high_risk_churn:
        flow.append(Paragraph("E. High-risk volunteer churn", styles["section"]))
        rows = [
            [
                _trim(c.get("volunteer_id"), 8),
                f"{float(c.get('predicted_churn_prob') or 0) * 100:.0f}%",
                _trim(c.get("intervention_type"), 22),
                _fmt_dt(c.get("predicted_at")),
                "yes" if c.get("reactivated") else "no",
            ]
            for c in report.high_risk_churn
        ]
        t = _data_table(
            ["Volunteer", "Churn", "Intervention", "Predicted at", "Reactivated"],
            rows,
            col_widths=[2.5 * cm, 2.5 * cm, 4.5 * cm, 3.5 * cm, 2.5 * cm],
        )
        if t is not None:
            flow.append(t)

    return flow


# ---------------------------------------------------------------------------
# Main entry point
# ---------------------------------------------------------------------------

def generate_pdf(
    report: ReportData, output_dir: str, filename: Optional[str] = None
) -> str:
    os.makedirs(output_dir, exist_ok=True)
    ts = report.generated_at.strftime("%Y%m%d_%H%M%S")
    file_name = filename or f"analytics_report_{ts}.pdf"
    path = os.path.join(output_dir, file_name)

    styles = _styles()
    cover_deco, body_deco = _make_decoration(report)

    doc = BaseDocTemplate(
        path,
        pagesize=A4,
        title=report.title,
        author="ai_service",
        leftMargin=2 * cm,
        rightMargin=2 * cm,
        topMargin=2 * cm,
        bottomMargin=2.2 * cm,
    )

    cover_frame = Frame(
        2 * cm, 2.2 * cm,
        A4[0] - 4 * cm, A4[1] - 4.2 * cm,
        leftPadding=0, rightPadding=0,
        topPadding=0, bottomPadding=0,
        showBoundary=0,
    )
    body_frame = Frame(
        2 * cm, 2.2 * cm,
        A4[0] - 4 * cm, A4[1] - 4.4 * cm,
        leftPadding=0, rightPadding=0,
        topPadding=0, bottomPadding=0,
        showBoundary=0,
    )

    doc.addPageTemplates(
        [
            PageTemplate(id="cover", frames=[cover_frame], onPage=cover_deco),
            PageTemplate(id="body", frames=[body_frame], onPage=body_deco),
        ]
    )

    story: List[Any] = []

    # ----- cover page ----- (rendered with the cover template)
    story.extend(_cover_page(report, styles))

    # Switch to body template for everything after
    story.append(NextPageTemplate("body"))
    story.append(PageBreak())

    # ----- chapters -----
    for chapter in report.chapters:
        story.extend(_chapter_block(chapter, styles))

    # ----- appendix -----
    story.append(PageBreak())
    story.extend(_appendix(report, styles))

    doc.build(story)
    return path
