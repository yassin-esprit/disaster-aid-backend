import os
import tempfile
import unittest
from datetime import datetime

from app.models import MetricItem, ReportData, ReportSection
from app.report import generate_pdf


class ReportGenerationTests(unittest.TestCase):
    def test_generate_pdf_creates_file(self):
        report = ReportData(
            generated_at=datetime.utcnow(),
            metrics=[MetricItem("Total alerts", "10")],
            sections=[ReportSection("Observations", ["Test line"])],
            source_note="Test source",
        )

        with tempfile.TemporaryDirectory() as tmpdir:
            path = generate_pdf(report, output_dir=tmpdir, filename="test.pdf")
            self.assertTrue(os.path.exists(path))
            self.assertGreater(os.path.getsize(path), 0)


if __name__ == "__main__":
    unittest.main()

