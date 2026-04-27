package tn.esprit.disasteraidbackend.modules.resourceShelter.service;

import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ShelterOccupantCreateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ShelterOccupantUpdateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.ShelterOccupant;

import java.util.List;

public interface IShelterOccupantService {

    // check-in / check-out (recommandé)
    ShelterOccupant checkIn(ShelterOccupantCreateDto dto);
    ShelterOccupant checkOut(Integer recordId);

    // CRUD simple (si tu en as besoin)
    ShelterOccupant getShelterOccupant(Integer id);
    ShelterOccupant updateShelterOccupant(Integer id, ShelterOccupantUpdateDto dto);
    void deleteShelterOccupant(Integer id);
    List<ShelterOccupant> getAllShelterOccupants();

    List<ShelterOccupant> getOccupantsByShelterId(Integer shelterId);
}