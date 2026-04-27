package tn.esprit.disasteraidbackend.alert.service;

import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.alert.dto.RiskZoneDto;
import tn.esprit.disasteraidbackend.alert.entity.Alert;
import tn.esprit.disasteraidbackend.alert.entity.AlertStatus;
import tn.esprit.disasteraidbackend.alert.repository.AlertRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RiskZoneService {

    private final AlertRepository alertRepo;

    public RiskZoneService(AlertRepository alertRepo) {
        this.alertRepo = alertRepo;
    }

    public List<RiskZoneDto> getRiskZones() {
        List<Alert> allAlerts = alertRepo.findAll();

        Map<String, List<Alert>> byRegion = allAlerts.stream()
                .filter(a -> a.getRegion() != null && !a.getRegion().isBlank())
                .collect(Collectors.groupingBy(Alert::getRegion));

        List<RiskZoneDto> zones = new ArrayList<>();

        for (Map.Entry<String, List<Alert>> entry : byRegion.entrySet()) {
            String region = entry.getKey();
            List<Alert> alerts = entry.getValue();

            int total = alerts.size();
            int active = (int) alerts.stream().filter(a -> a.getStatus() == AlertStatus.ACTIVE).count();
            int resolved = (int) alerts.stream().filter(a -> a.getStatus() == AlertStatus.RESOLVED).count();

            double avgLat = alerts.stream().mapToDouble(Alert::getLatitude).average().orElse(0);
            double avgLng = alerts.stream().mapToDouble(Alert::getLongitude).average().orElse(0);

            String country = alerts.get(0).getCountry();

            List<String> types = alerts.stream()
                    .map(a -> a.getType().name())
                    .distinct()
                    .sorted()
                    .toList();

            String risk;
            if (active >= 3 || total >= 5) risk = "CRITICAL";
            else if (active >= 2 || total >= 3) risk = "HIGH";
            else if (active >= 1 || total >= 2) risk = "MEDIUM";
            else risk = "LOW";

            zones.add(new RiskZoneDto(region, country, total, active, resolved,
                    avgLat, avgLng, risk, types));
        }

        zones.sort(Comparator.comparingInt(RiskZoneDto::getTotalAlerts).reversed());
        return zones;
    }
}
