package com.clinic.vetpet.animal.repository;
import com.clinic.vetpet.modules.animals.models.AnimalTypes;
import com.clinic.vetpet.modules.animals.repository.AnimalDetailRepository;
import com.clinic.vetpet.statistics.animalstatistic.models.AnimalStatistic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;


@DataJpaTest
@Sql(scripts = "classpath:init/testdata.sql", config = @SqlConfig(separator = ";", commentPrefix = "--"))
public class AnimalDetailRepositoryTest {

    @Autowired
    private AnimalDetailRepository animalDetailRepository;

    @BeforeEach
    void init() {
    }

    @Nested
    class testAnimalStatisticData{

        @BeforeEach
        void init() {
        }
        @Test
        void testAnimalStatTypes() {
            List<AnimalStatistic> statList = animalDetailRepository.getAnimalStatistics();

            Assertions.assertAll(
                    () -> assertNotNull(statList),
                    ()-> assertEquals(3, statList.size()));

            assertThat( statList, containsInAnyOrder(
                    hasProperty("animalType", is(AnimalTypes.BIRD)),
                    hasProperty("animalType", is(AnimalTypes.DOG)),
                    hasProperty("animalType", is(AnimalTypes.CAT))
            ));
        }

        @Test
        void testAnimalStatTypesCount() {
            List<AnimalStatistic> statList = animalDetailRepository.getAnimalStatistics();

            Assertions.assertAll(
                    () -> assertNotNull(statList),
                    ()-> assertEquals(3, statList.size()));

            assertThat( statList,
                    hasItems(new AnimalStatistic(2L, AnimalTypes.CAT),
                            new AnimalStatistic(3L, AnimalTypes.BIRD),
                            new AnimalStatistic(1L, AnimalTypes.DOG)));
        }

    }

}
