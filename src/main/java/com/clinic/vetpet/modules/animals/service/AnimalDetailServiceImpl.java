package com.clinic.vetpet.modules.animals.service;

import com.clinic.vetpet.modules.admin.models.User;
import com.clinic.vetpet.modules.animals.models.AnimalDetail;
import com.clinic.vetpet.modules.animals.models.AnimalDetailDto;
import com.clinic.vetpet.modules.animals.repository.AnimalDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer to handle all the City operation logics
 *
 * @author Keshani
 * @since 2021/11/13
 */
@Service
public class AnimalDetailServiceImpl implements AnimalDetailService {

    @Autowired
    private AnimalDetailRepository animalDetailRepository;

    @Override
    public Page<AnimalDetail> getListOfAnimals(AnimalDetailDto animalDetailDto) {
        Pageable pageable = PageRequest.of(animalDetailDto.getPageNumber(), animalDetailDto.getPageSize());
        return animalDetailRepository.findByOwnerUserId(animalDetailDto.getOwnerId(),  pageable);
    }

    @Override
    public List<AnimalDetail> getListOfAnimalsByUser(String userId) {
        List<AnimalDetail> animalList = animalDetailRepository.findByOwnerUserId(userId);
        return animalList;
    }

    @Override
    public void addAnimalDetail(AnimalDetailDto animalDetailDto) {
        AnimalDetail animalDetail = new AnimalDetail();
        User owner = new User();
        owner.setUserId(animalDetailDto.getOwnerId());
        animalDetail.setOwner(owner);
        animalDetail.setAnimalName(animalDetailDto.getAnimalName());
        animalDetail.setAnimalType(animalDetailDto.getAnimalType());
        animalDetailRepository.save(animalDetail);
    }

    @Override
    public void updateAnimalDetail(AnimalDetailDto animalDetailDto) {
          AnimalDetail animalDetail = animalDetailRepository.findById(animalDetailDto.getId())
                  .orElseThrow(() -> new RuntimeException("Animal detail is not found for this id:" + animalDetailDto.getId()));
          animalDetail.setAnimalName(animalDetailDto.getAnimalName());
          animalDetail.setAnimalType(animalDetailDto.getAnimalType());
          animalDetailRepository.save(animalDetail);
    }

    @Override
    public void deleteAnimalDetail(Long animalDetailId) {
     animalDetailRepository.deleteById(animalDetailId);
}   }
