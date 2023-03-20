package com.clinic.vetpet.modules.animals.service;

import com.clinic.vetpet.modules.admin.models.User;
import com.clinic.vetpet.modules.animals.models.AnimalDetail;
import com.clinic.vetpet.modules.animals.models.AnimalDetailDto;
import com.clinic.vetpet.modules.animals.repository.AnimalDetailRepository;
import com.clinic.vetpet.statistics.animalstatistic.models.AnimalStatistic;
import com.clinic.vetpet.statistics.animalstatistic.models.AnimalStatisticsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service layer to handle all the Animal operation logics
 *
 * @author Keshani
 * @since 2023/03/15
 */
@Service
public class AnimalDetailServiceImpl implements AnimalDetailService {

    @Autowired
    private AnimalDetailRepository animalDetailRepository;

    @Override
    public Page<AnimalDetail> getListOfAnimals(AnimalDetailDto animalDetailDto) {
        Pageable pageable = PageRequest.of(animalDetailDto.getPageNumber(), animalDetailDto.getPageSize());
        return animalDetailRepository.findByOwnerUserId(animalDetailDto.getOwnerId(), pageable);
    }

    @Override
    public List<AnimalDetail> getListOfAnimalsByUser(String userId) {
        List<AnimalDetail> animalList = animalDetailRepository.findByOwnerUserId(userId);
        return animalList;
    }

    @Override
    public AnimalDetail addAnimalDetail(AnimalDetailDto animalDetailDto) {
        AnimalDetail animalDetail = new AnimalDetail();
        User owner = new User();
        owner.setUserId(animalDetailDto.getOwnerId());
        animalDetail.setOwner(owner);
        animalDetail.setAnimalName(animalDetailDto.getAnimalName());
        animalDetail.setAnimalType(animalDetailDto.getAnimalType());
        return animalDetailRepository.save(animalDetail);
    }

    @Override
    public AnimalDetail updateAnimalDetail(AnimalDetailDto animalDetailDto) {
        AnimalDetail animalDetail = animalDetailRepository.findById(animalDetailDto.getId())
                .orElseThrow(() -> new RuntimeException("Animal detail is not found for this id:" + animalDetailDto.getId()));
        animalDetail.setAnimalName(animalDetailDto.getAnimalName());
        animalDetail.setAnimalType(animalDetailDto.getAnimalType());
        animalDetailRepository.save(animalDetail);
        return animalDetail;
    }

    @Override
    public void deleteAnimalDetail(Long animalDetailId) {
        animalDetailRepository.deleteById(animalDetailId);
    }

    @Override
    public AnimalStatisticsDto getAnimalStatistics() {
        List<AnimalStatistic> statistics = animalDetailRepository.getAnimalStatistics();
        AnimalStatisticsDto result = new AnimalStatisticsDto();
        Long totalAnimals = statistics.stream().mapToLong(
                (stat) -> stat.getAnimalCount()).sum();

        result.setTotalAnimals(totalAnimals);
        result.setAnimalStatisticList(statistics);
        return result;
    }
}