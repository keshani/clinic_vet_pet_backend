package com.clinic.vetpet.animal.service;

import com.clinic.vetpet.modules.admin.models.User;
import com.clinic.vetpet.modules.animals.models.AnimalDetail;
import com.clinic.vetpet.modules.animals.models.AnimalDetailDto;
import com.clinic.vetpet.modules.animals.models.AnimalTypes;
import com.clinic.vetpet.modules.animals.repository.AnimalDetailRepository;
import com.clinic.vetpet.modules.animals.service.AnimalDetailServiceImpl;
import com.clinic.vetpet.statistics.animalstatistic.models.AnimalStatistic;
import com.clinic.vetpet.statistics.animalstatistic.models.AnimalStatisticsDto;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AnimalDetailServiceTest {

    @Mock
    private AnimalDetailRepository animalDetailRepository;

    @InjectMocks
    private AnimalDetailServiceImpl animalDetailServiceImpl;

    private User owner;
    private String ownerId;
    private AnimalDetail animalDetail;
    private AnimalDetailDto animalDetailDto;
    private Pageable pageable;

    @BeforeEach
    void init() {
        ownerId = "test-owner";
        owner = new User(ownerId);
        animalDetailDto = new AnimalDetailDto(1l, "TestAnimal", AnimalTypes.CAT, ownerId , 2, 0 );
        animalDetail = new AnimalDetail(1l,"TestAnimal",AnimalTypes.CAT, owner );
        pageable =  PageRequest.of(0, 2);
    }

    @Test
    void testGetListOfAnimals() {
        Mockito.when(animalDetailRepository.findAll(pageable)).thenReturn(new PageImpl<AnimalDetail>(Arrays.asList(animalDetail), pageable,  1L));
        Page<AnimalDetail> animalDetailPage = animalDetailServiceImpl.getListOfAnimals(animalDetailDto);

        Assertions.assertNotNull(animalDetailPage);
        List<AnimalDetail> animalDetails = animalDetailPage.stream().toList();
        Assertions.assertEquals(1, animalDetails.size(), "check array size");
        assertEquals(true, animalDetails.stream().anyMatch((s) -> "TestAnimal".equalsIgnoreCase(s.getAnimalName())), "check weather object are in find all");

    }

    @Test
    void testGetListOfAnimalsForGivenUser() {
        Mockito.when(animalDetailRepository.findByOwnerUserId(ownerId)).thenReturn(Arrays.asList(animalDetail));
        List<AnimalDetail> animalDetails = animalDetailServiceImpl.getListOfAnimalsByUser(ownerId);

        Assertions.assertNotNull(animalDetails);
        Assertions.assertEquals(1, animalDetails.size(), "check array size");
        assertEquals(true, animalDetails.stream().anyMatch((s) -> "TestAnimal".equalsIgnoreCase(s.getAnimalName())), "check weather animals are return for given user");

    }

    @Test
    void testAddAnimal() {

        Mockito.when(animalDetailRepository.save(Mockito.any())).thenReturn(animalDetail);
        AnimalDetail animalDetailResponse  = animalDetailServiceImpl.addAnimalDetail(animalDetailDto);

        Assertions.assertAll(
                () -> assertNotNull(animalDetailResponse),
                () -> assertEquals(animalDetail.getAnimalName(), animalDetailResponse.getAnimalName()),
                () -> assertEquals(animalDetail.getId(), animalDetailResponse.getId())
        );
    }

    @Test
    void testUpdateAnimal() {
        String updatedAnimalName = "TestAnimal-CAT";
        AnimalDetailDto animalDetailDto = new AnimalDetailDto(1l, updatedAnimalName, AnimalTypes.CAT, ownerId  );
        AnimalDetail updatedAnimalDetail = new AnimalDetail(1l,updatedAnimalName,AnimalTypes.CAT, owner);

        Mockito.when(animalDetailRepository.findById(1L)).thenReturn(Optional.of(animalDetail));
        Mockito.when(animalDetailRepository.save(Mockito.any())).thenReturn(updatedAnimalDetail);

        AnimalDetail animalDetailResponse = animalDetailServiceImpl.updateAnimalDetail(animalDetailDto);
        Assertions.assertAll(
                () -> assertNotNull(animalDetailResponse),
                () -> assertEquals(updatedAnimalDetail.getAnimalName(), animalDetailResponse.getAnimalName()),
                () -> assertEquals(updatedAnimalDetail.getId(), animalDetailResponse.getId())
        );
    }



    @Nested
    class testAnimalStatisticDataTotalCount{

        List<AnimalStatistic> statList;
        AnimalStatistic stat1;
        AnimalStatistic stat2;
        AnimalStatistic stat3;
        @BeforeEach
        void init() {
            stat1 =  new AnimalStatistic(5L, AnimalTypes.CAT);
            stat2 =  new AnimalStatistic(1L, AnimalTypes.CAT);
            stat3 =  new AnimalStatistic(2L, AnimalTypes.CAT);

            statList = List.of(stat1,stat2, stat3);
        }
        @Test
        void testAnimalStatTotalCount() {
            Mockito.when(animalDetailRepository.getAnimalStatistics()).thenReturn(List.of(stat1,stat2, stat3));
            AnimalStatisticsDto animalStatisticsDto  = animalDetailServiceImpl.getAnimalStatistics();

            Assertions.assertAll(
                    () -> assertNotNull(animalStatisticsDto),
                    () -> assertEquals(8,animalStatisticsDto.getTotalAnimals()));
        }
        @Test
        void testAnimalStatTotalCountWithZero() {
            stat1.setAnimalCount(0L);
            stat2.setAnimalCount(0L);
            stat3.setAnimalCount(0L);

            Mockito.when(animalDetailRepository.getAnimalStatistics()).thenReturn(List.of(stat1,stat2, stat3));
            AnimalStatisticsDto animalStatisticsDto  = animalDetailServiceImpl.getAnimalStatistics();

            Assertions.assertAll(
                    () -> assertNotNull(animalStatisticsDto),
                    () -> assertEquals(0,animalStatisticsDto.getTotalAnimals()));
        }
    }

}