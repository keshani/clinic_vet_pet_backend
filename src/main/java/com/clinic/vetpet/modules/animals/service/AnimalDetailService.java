package com.clinic.vetpet.modules.animals.service;

import com.clinic.vetpet.modules.animals.models.AnimalDetail;
import com.clinic.vetpet.modules.animals.models.AnimalDetailDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnimalDetailService {
    Page<AnimalDetail> getListOfAnimals(AnimalDetailDto animalDetailDto);

    List<AnimalDetail> getListOfAnimalsByUser(String userId);

    void addAnimalDetail(AnimalDetailDto animalDetailDto);

    void updateAnimalDetail(AnimalDetailDto animalDetailDto);

    void deleteAnimalDetail(Long animalDetailId);
}
