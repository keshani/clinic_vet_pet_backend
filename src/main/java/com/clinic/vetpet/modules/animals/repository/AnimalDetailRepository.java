package com.clinic.vetpet.modules.animals.repository;

import com.clinic.vetpet.modules.animals.models.AnimalDetail;
import com.clinic.vetpet.statistics.animalstatistic.models.AnimalStatistic;
import com.clinic.vetpet.statistics.animalstatistic.models.AnimalStatisticsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to handle all the Animal Entity related CRUD operations
 *
 * @author Keshani
 * @since 2023/03/15
 */
@Repository
public interface AnimalDetailRepository extends JpaRepository<AnimalDetail, Long> {

        Page<AnimalDetail> findByOwnerUserId(String userId, Pageable pageable);

        List<AnimalDetail> findByOwnerUserId(String userId);

        // get animal count against animal type
        @Query("SELECT new com.clinic.vetpet.statistics.animalstatistic.models.AnimalStatistic(count(a.id) as animalCount, a.animalType) " +
                "FROM AnimalDetail a " +
                "GROUP BY a.animalType ")
        List<AnimalStatistic> getAnimalStatistics();
}

