package tn.esprit.disasteraidbackend.modules.hours.service;

import tn.esprit.disasteraidbackend.modules.hours.dto.ImpactRecordRequest;
import tn.esprit.disasteraidbackend.modules.hours.dto.ImpactRecordResponse;

import java.util.List;

public interface ImpactRecordService {

    ImpactRecordResponse createImpactRecord(ImpactRecordRequest request);

    ImpactRecordResponse getImpactRecordById(Long id);

    ImpactRecordResponse getImpactRecordByVolunteer(Long volunteerId);

    List<ImpactRecordResponse> getAllImpactRecords();

    ImpactRecordResponse updateImpactRecord(Long id, ImpactRecordRequest request);

    void deleteImpactRecord(Long id);
}
