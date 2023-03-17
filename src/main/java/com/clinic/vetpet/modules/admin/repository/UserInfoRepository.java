package com.clinic.vetpet.modules.admin.repository;

import com.clinic.vetpet.modules.admin.models.User;
import com.clinic.vetpet.modules.animals.models.AnimalDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to handle all the City Entity related CRUD operations
 *
 * @author Keshani
 * @since 2021/11/13
 */
@Repository
public interface UserInfoRepository extends JpaRepository<User, String> {
}

