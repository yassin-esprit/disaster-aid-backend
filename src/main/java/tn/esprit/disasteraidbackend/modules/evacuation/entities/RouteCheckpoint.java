package tn.esprit.disasteraidbackend.modules.evacuation.entities;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tn.esprit.disasteraidbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.util.List;

@Entity
@Table(name = "route_checkpoints")
@Getter
@Setter
public class RouteCheckpoint extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    private EvacuationRoute route;

    @Column(columnDefinition = "GEOMETRY(POINT, 4326)", nullable = false)
    private Point location;

    private String label;

    @Enumerated(EnumType.STRING)
    private CheckpointType type;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "integer[]")
    private List<Integer> responderIds;

    private Boolean isActive = true;

    public enum CheckpointType {
        WAYPOINT, MEDICAL_STOP, SUPPLY_POINT
    }
}