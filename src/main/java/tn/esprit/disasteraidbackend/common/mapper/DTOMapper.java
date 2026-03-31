package tn.esprit.disasteraidbackend.common.mapper;

import org.locationtech.jts.geom.*;
import org.locationtech.jts.io.WKTReader;
import tn.esprit.disasteraidbackend.modules.evacuation.dto.*;
import tn.esprit.disasteraidbackend.modules.evacuation.entities.*;
import tn.esprit.disasteraidbackend.modules.sensor.dto.*;
import tn.esprit.disasteraidbackend.modules.sensor.entities.*;

/**
 * Utility class for converting between JPA entities and DTOs.
 * Geometry fields are represented as WKT strings in DTOs.
 * Relationship fields are represented by IDs (Integer) in DTOs.
 */
public final class DTOMapper {

    private DTOMapper() {}

    // Geometry helpers
    public static String toWKT(Geometry geometry) {
        return geometry != null ? geometry.toText() : null;
    }

    public static Geometry fromWKT(String wkt, GeometryFactory geometryFactory) {
        if (wkt == null) return null;
        try {
            return new WKTReader(geometryFactory).read(wkt);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid WKT: " + wkt, e);
        }
    }

    // ==================== Alert ====================
    public static AlertDTO toDTO(Alert entity) {
        if (entity == null) return null;
        AlertDTO dto = new AlertDTO();
        dto.setId(entity.getId());
        dto.setDisasterType(entity.getDisasterType());
        dto.setSeverity(entity.getSeverity());
        dto.setStatus(entity.getStatus());
        dto.setAffectedArea(toWKT(entity.getAffectedArea()));
        dto.setPredictedArea2h(toWKT(entity.getPredictedArea2h()));
        dto.setPredictedArea6h(toWKT(entity.getPredictedArea6h()));
        dto.setConfidenceScore(entity.getConfidenceScore());
        dto.setTriggerSensorId(entity.getTriggerSensor() != null ? entity.getTriggerSensor().getId() : null);
        dto.setIssuedBy(entity.getIssuedBy());
        dto.setIssuedAt(entity.getIssuedAt());
        dto.setResolvedAt(entity.getResolvedAt());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public static Alert toEntity(AlertDTO dto, GeometryFactory geometryFactory) {
        if (dto == null) return null;
        Alert entity = new Alert();
        entity.setId(dto.getId());
        entity.setDisasterType(dto.getDisasterType());
        entity.setSeverity(dto.getSeverity());
        entity.setStatus(dto.getStatus());
        entity.setAffectedArea((Polygon) fromWKT(dto.getAffectedArea(), geometryFactory));
        entity.setPredictedArea2h((Polygon) fromWKT(dto.getPredictedArea2h(), geometryFactory));
        entity.setPredictedArea6h((Polygon) fromWKT(dto.getPredictedArea6h(), geometryFactory));
        entity.setConfidenceScore(dto.getConfidenceScore());
        // triggerSensor must be set separately
        entity.setIssuedBy(dto.getIssuedBy());
        entity.setIssuedAt(dto.getIssuedAt());
        entity.setResolvedAt(dto.getResolvedAt());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    // ==================== AlertThreshold ====================
    public static AlertThresholdDTO toDTO(AlertThreshold entity) {
        if (entity == null) return null;
        AlertThresholdDTO dto = new AlertThresholdDTO();
        dto.setId(entity.getId());
        dto.setSensorType(entity.getSensorType());
        dto.setParameter(entity.getParameter());
        dto.setWarnLevel(entity.getWarnLevel());
        dto.setCriticalLevel(entity.getCriticalLevel());
        dto.setUnit(entity.getUnit());
        dto.setEvaluationWindowSec(entity.getEvaluationWindowSec());
        return dto;
    }

    public static AlertThreshold toEntity(AlertThresholdDTO dto) {
        if (dto == null) return null;
        AlertThreshold entity = new AlertThreshold();
        entity.setId(dto.getId());
        entity.setSensorType(dto.getSensorType());
        entity.setParameter(dto.getParameter());
        entity.setWarnLevel(dto.getWarnLevel());
        entity.setCriticalLevel(dto.getCriticalLevel());
        entity.setUnit(dto.getUnit());
        entity.setEvaluationWindowSec(dto.getEvaluationWindowSec());
        return entity;
    }

    // ==================== GeofenceZone ====================
    public static GeofenceZoneDTO toDTO(GeofenceZone entity) {
        if (entity == null) return null;
        GeofenceZoneDTO dto = new GeofenceZoneDTO();
        dto.setId(entity.getId());
        dto.setAlertId(entity.getAlert() != null ? entity.getAlert().getId() : null);
        dto.setPolygon(toWKT(entity.getPolygon()));
        dto.setZoneType(entity.getZoneType());
        dto.setRadius(entity.getRadius());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setExpiresAt(entity.getExpiresAt());
        return dto;
    }

    public static GeofenceZone toEntity(GeofenceZoneDTO dto, GeometryFactory geometryFactory) {
        if (dto == null) return null;
        GeofenceZone entity = new GeofenceZone();
        entity.setId(dto.getId());
        // alert must be set separately
        entity.setPolygon((Polygon) fromWKT(dto.getPolygon(), geometryFactory));
        entity.setZoneType(dto.getZoneType());
        entity.setRadius(dto.getRadius());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setExpiresAt(dto.getExpiresAt());
        return entity;
    }

    // ==================== Sensor ====================
    public static SensorDTO toDTO(Sensor entity) {
        if (entity == null) return null;
        SensorDTO dto = new SensorDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setLocation(toWKT(entity.getLocation()));
        dto.setOwnerId(entity.getOwnerId());
        dto.setManufacturer(entity.getManufacturer());
        dto.setStatus(entity.getStatus());
        dto.setLastPingAt(entity.getLastPingAt());
        dto.setCalibrationDate(entity.getCalibrationDate());
        return dto;
    }

    public static Sensor toEntity(SensorDTO dto, GeometryFactory geometryFactory) {
        if (dto == null) return null;
        Sensor entity = new Sensor();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setType(dto.getType());
        entity.setLocation((Point) fromWKT(dto.getLocation(), geometryFactory));
        entity.setOwnerId(dto.getOwnerId());
        entity.setManufacturer(dto.getManufacturer());
        entity.setStatus(dto.getStatus());
        entity.setLastPingAt(dto.getLastPingAt());
        entity.setCalibrationDate(dto.getCalibrationDate());
        return entity;
    }

    // ==================== SensorData ====================
    public static SensorDataDTO toDTO(SensorData entity) {
        if (entity == null) return null;
        SensorDataDTO dto = new SensorDataDTO();
        dto.setId(entity.getId());
        dto.setSensorId(entity.getSensor() != null ? entity.getSensor().getId() : null);
        dto.setValue(entity.getValue());
        dto.setUnit(entity.getUnit());
        dto.setRecordedAt(entity.getRecordedAt());
        dto.setRawPayload(entity.getRawPayload());
        dto.setIsAnomaly(entity.getIsAnomaly());
        return dto;
    }

    public static SensorData toEntity(SensorDataDTO dto) {
        if (dto == null) return null;
        SensorData entity = new SensorData();
        entity.setId(dto.getId());
        // sensor must be set separately
        entity.setValue(dto.getValue());
        entity.setUnit(dto.getUnit());
        entity.setRecordedAt(dto.getRecordedAt());
        entity.setRawPayload(dto.getRawPayload());
        entity.setIsAnomaly(dto.getIsAnomaly());
        return entity;
    }

    // ==================== EvacuationRoute ====================
    public static EvacuationRouteDTO toDTO(EvacuationRoute entity) {
        if (entity == null) return null;
        EvacuationRouteDTO dto = new EvacuationRouteDTO();
        dto.setId(entity.getId());
        dto.setAlertId(entity.getAlert() != null ? entity.getAlert().getId() : null);
        dto.setToShelterId(entity.getToShelterId());
        dto.setPath(toWKT(entity.getPath()));
        dto.setEstimatedDurationMin(entity.getEstimatedDurationMin());
        dto.setCapacity(entity.getCapacity());
        dto.setStatus(entity.getStatus());
        dto.setActivatedAt(entity.getActivatedAt());
        dto.setClosedAt(entity.getClosedAt());
        return dto;
    }

    public static EvacuationRoute toEntity(EvacuationRouteDTO dto, GeometryFactory geometryFactory) {
        if (dto == null) return null;
        EvacuationRoute entity = new EvacuationRoute();
        entity.setId(dto.getId());
        // alert must be set separately
        entity.setToShelterId(dto.getToShelterId());
        entity.setPath((LineString) fromWKT(dto.getPath(), geometryFactory));
        entity.setEstimatedDurationMin(dto.getEstimatedDurationMin());
        entity.setCapacity(dto.getCapacity());
        entity.setStatus(dto.getStatus());
        entity.setActivatedAt(dto.getActivatedAt());
        entity.setClosedAt(dto.getClosedAt());
        return entity;
    }

    // ==================== RouteCheckpoint ====================
    public static RouteCheckpointDTO toDTO(RouteCheckpoint entity) {
        if (entity == null) return null;
        RouteCheckpointDTO dto = new RouteCheckpointDTO();
        dto.setId(entity.getId());
        dto.setRouteId(entity.getRoute() != null ? entity.getRoute().getId() : null);
        dto.setLocation(toWKT(entity.getLocation()));
        dto.setLabel(entity.getLabel());
        dto.setType(entity.getType());
        dto.setResponderIds(entity.getResponderIds());
        dto.setIsActive(entity.getIsActive());
        return dto;
    }

    public static RouteCheckpoint toEntity(RouteCheckpointDTO dto, GeometryFactory geometryFactory) {
        if (dto == null) return null;
        RouteCheckpoint entity = new RouteCheckpoint();
        entity.setId(dto.getId());
        // route must be set separately
        entity.setLocation((Point) fromWKT(dto.getLocation(), geometryFactory));
        entity.setLabel(dto.getLabel());
        entity.setType(dto.getType());
        entity.setResponderIds(dto.getResponderIds());
        entity.setIsActive(dto.getIsActive());
        return entity;
    }

    // ==================== ZoneShelterAssignment ====================
    public static ZoneShelterAssignmentDTO toDTO(ZoneShelterAssignment entity) {
        if (entity == null) return null;
        ZoneShelterAssignmentDTO dto = new ZoneShelterAssignmentDTO();
        dto.setId(entity.getId());
        dto.setGeofenceZoneId(entity.getGeofenceZone() != null ? entity.getGeofenceZone().getId() : null);
        dto.setShelterId(entity.getShelterId());
        dto.setPriority(entity.getPriority());
        dto.setEstimatedUsersCount(entity.getEstimatedUsersCount());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public static ZoneShelterAssignment toEntity(ZoneShelterAssignmentDTO dto) {
        if (dto == null) return null;
        ZoneShelterAssignment entity = new ZoneShelterAssignment();
        entity.setId(dto.getId());
        // geofenceZone must be set separately
        entity.setShelterId(dto.getShelterId());
        entity.setPriority(dto.getPriority());
        entity.setEstimatedUsersCount(dto.getEstimatedUsersCount());
        entity.setCreatedAt(dto.getCreatedAt());
        return entity;
    }
}