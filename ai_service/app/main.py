import os

from dotenv import load_dotenv

from .analysis import build_report_data
from .data_loader import fetch_analytics_dashboard, fetch_core_counts
from .report import generate_pdf


def main() -> int:
    load_dotenv()

    output_dir = os.getenv("OUTPUT_DIR", "ai_reports")
    high_risk_threshold = float(os.getenv("CHURN_THRESHOLD", "0.7"))

    counts = {}
    dashboard = {}

    try:
        counts = fetch_core_counts()
    except Exception as exc:
        print(f"[ai_service] Could not load core counts: {exc}")

    try:
        dashboard = fetch_analytics_dashboard(high_risk_threshold=high_risk_threshold)
    except Exception as exc:
        print(f"[ai_service] Could not load analytics dashboard data: {exc}")

    report_data = build_report_data(counts=counts, dashboard=dashboard)
    pdf_path = generate_pdf(report_data, output_dir=output_dir)

    print(f"[ai_service] Report generated: {pdf_path}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
