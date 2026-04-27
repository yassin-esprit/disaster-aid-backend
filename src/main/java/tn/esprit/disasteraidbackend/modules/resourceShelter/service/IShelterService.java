package tn.esprit.disasteraidbackend.modules.resourceShelter.service;

import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ShelterCreateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.dto.ShelterUpdateDto;
import tn.esprit.disasteraidbackend.modules.resourceShelter.entity.Shelter;

import java.util.List;

public interface IShelterService {
    Shelter addShelter(ShelterCreateDto dto);
    Shelter getShelter(Integer id);
    Shelter updateShelter(Integer id, ShelterUpdateDto dto);
    void deleteShelter(Integer id);
    List<Shelter> getAllShelters();
}