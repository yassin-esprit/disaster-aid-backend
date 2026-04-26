package tn.esprit.disasteraidbackend.modules.hours.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.disasteraidbackend.modules.hours.dto.ImpactRecordRequest;
import tn.esprit.disasteraidbackend.modules.hours.dto.ImpactRecordResponse;
import tn.esprit.disasteraidbackend.modules.hours.entity.ImpactRecord;
import tn.esprit.disasteraidbackend.modules.hours.repository.ImpactRecordRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImpactRecordServiceImpl implements ImpactRecordService {

    private final ImpactRecordRepository impactRecordRepository;
    private final ImpactScoreService impactScoreService;   // ← injected

    // ── Mapping helpers ──────────────────────────────────────────────────────

    private ImpactRecord toEntity(ImpactRecordRequest request) {
        return ImpactRecord.builder()
                .volunteerId(request.getVolunteerId())
                .totalApprovedHours(request.getTotalApprovedHours())
                .co2OffsetKg(request.getCo2OffsetKg())
                .mealsServed(request.getMealsServed())
                .peopleHelped(request.getPeopleHelped())
                .wasteRemovedKg(request.getWasteRemovedKg())
                .hoursOfTutoring(request.getHoursOfTutoring())
                .confidenceLevel(request.getConfidenceLevel())
                .impactScore(0.0)
                .lastCalculatedAt(LocalDateTime.now())
                .build();
    }

    private ImpactRecordResponse toResponse(ImpactRecord r) {
        ImpactRecordResponse response = new ImpactRecordResponse();
        response.setImpactId(r.getImpactId());
        response.setVolunteerId(r.getVolunteerId());
        response.setTotalApprovedHours(r.getTotalApprovedHours());
        response.setCo2OffsetKg(r.getCo2OffsetKg());
        response.setMealsServed(r.getMealsServed());
        response.setPeopleHelped(r.getPeopleHelped());
        response.setWasteRemovedKg(r.getWasteRemovedKg());
        response.setHoursOfTutoring(r.getHoursOfTutoring());
        response.setConfidenceLevel(r.getConfidenceLevel());
        response.setImpactScore(r.getImpactScore());          // ← new field
        response.setLastCalculatedAt(r.getLastCalculatedAt());
        return response;
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────

    @Override
    public ImpactRecordResponse createImpactRecord(ImpactRecordRequest request) {
        ImpactRecord saved = impactRecordRepository.save(toEntity(request));
        // Calculate score immediately after creation
        saved = impactScoreService.calculateForRecord(saved);
        return toResponse(saved);
    }

    @Override
    public ImpactRecordResponse getImpactRecordById(Long id) {
        ImpactRecord r = impactRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ImpactRecord not found with id: " + id));
        return toResponse(r);
    }

    @Override
    public ImpactRecordResponse getImpactRecordByVolunteer(Long volunteerId) {
        ImpactRecord r = impactRecordRepository.findByVolunteerId(volunteerId)
                .orElseThrow(() -> new RuntimeException("ImpactRecord not found for volunteerId: " + volunteerId));
        return toResponse(r);
    }

    @Override
    public List<ImpactRecordResponse> getAllImpactRecords() {
        return impactRecordRepository.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public ImpactRecordResponse updateImpactRecord(Long id, ImpactRecordRequest request) {
        ImpactRecord r = impactRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ImpactRecord not found with id: " + id));
        r.setVolunteerId(request.getVolunteerId());
        r.setTotalApprovedHours(request.getTotalApprovedHours());
        r.setCo2OffsetKg(request.getCo2OffsetKg());
        r.setMealsServed(request.getMealsServed());
        r.setPeopleHelped(request.getPeopleHelped());
        r.setWasteRemovedKg(request.getWasteRemovedKg());
        r.setHoursOfTutoring(request.getHoursOfTutoring());
        r.setConfidenceLevel(request.getConfidenceLevel());
        // Recalculate score immediately after update
        r = impactScoreService.calculateForRecord(r);
        return toResponse(r);
    }

    @Override
    public void deleteImpactRecord(Long id) {
        if (!impactRecordRepository.existsById(id)) {
            throw new RuntimeException("ImpactRecord not found with id: " + id);
        }
        impactRecordRepository.deleteById(id);
    }
}
