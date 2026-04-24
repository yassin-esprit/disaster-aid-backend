package tn.esprit.disasteraidbackend.modules.evacuation.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class OsrmRouteService {

    private final GeometryFactory geometryFactory;
    private final ObjectMapper objectMapper;

    private static final String OSRM_URL =
            "https://router.project-osrm.org/route/v1/driving/{lng1},{lat1};{lng2},{lat2}?overview=full&geometries=geojson";

    public RouteResult computeRoute(double fromLat, double fromLng, double toLat, double toLng) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = OSRM_URL
                    .replace("{lat1}", String.valueOf(fromLat))
                    .replace("{lng1}", String.valueOf(fromLng))
                    .replace("{lat2}", String.valueOf(toLat))
                    .replace("{lng2}", String.valueOf(toLng));

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);

            if (!"Ok".equals(root.path("code").asText())) {
                throw new RuntimeException("OSRM routing failed: " + root.path("code").asText());
            }

            JsonNode route = root.path("routes").get(0);
            double distanceMeters = route.path("distance").asDouble();
            double durationSeconds = route.path("duration").asDouble();
            int durationMin = (int) Math.ceil(durationSeconds / 60.0);

            JsonNode coordinates = route.path("geometry").path("coordinates");
            Coordinate[] coords = new Coordinate[coordinates.size()];
            for (int i = 0; i < coordinates.size(); i++) {
                JsonNode point = coordinates.get(i);
                double lng = point.get(0).asDouble();
                double lat = point.get(1).asDouble();
                coords[i] = new Coordinate(lng, lat);
            }

            LineString path = geometryFactory.createLineString(coords);

            return new RouteResult(path, durationMin, distanceMeters / 1000.0);

        } catch (Exception e) {
            log.warn("OSRM route computation failed: {}. Falling back to straight line.", e.getMessage());
            return fallbackStraightLine(fromLat, fromLng, toLat, toLng);
        }
    }

    private RouteResult fallbackStraightLine(double fromLat, double fromLng, double toLat, double toLng) {
        Coordinate[] coords = {
                new Coordinate(fromLng, fromLat),
                new Coordinate(toLng, toLat)
        };
        LineString path = geometryFactory.createLineString(coords);
        double distKm = haversineKm(fromLat, fromLng, toLat, toLng);
        int durationMin = (int) Math.ceil(distKm / 0.5);
        return new RouteResult(path, durationMin, distKm);
    }

    private double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public record RouteResult(LineString path, int estimatedDurationMin, double distanceKm) {}
}
