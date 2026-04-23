package tn.esprit.disasteraidbackend.modules.incident.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "incident_status_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IncidentStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Foreign key column (for queries and direct access)
    @Column(name = "incident_report_id", nullable = false)
    private Integer incidentReportId;

    // JPA relationship (for entity navigation - LAZY loading to avoid performance issues)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_report_id", nullable = false,
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_history_incident_report"))
    private IncidentReport incidentReport;

    @Column(name = "from_status")
    private String fromStatus;

    @Column(name = "to_status", nullable = false)
    private String toStatus;

    @Column(name = "changed_by")
    private Integer changedBy;

    @Column
    private String note;

    @CreationTimestamp
    @Column(name = "changed_at", updatable = false)
    private LocalDateTime changedAt;

    // Convenience method to set both ID and relationship
    public void setIncidentReport(IncidentReport report) {
        if (report != null) {
            this.incidentReportId = report.getId();
            this.incidentReport = report;
        }
    }
}