package com.clinic.vetpet.modules.animals.repository;

import com.clinic.vetpet.modules.animals.models.AnimalDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
}

