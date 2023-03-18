package com.clinic.vetpet.modules.admin.repository;

import com.clinic.vetpet.common.util.RoleTypes;
import com.clinic.vetpet.modules.admin.models.Role;
import com.clinic.vetpet.modules.admin.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Role, Long> {

    Role findByName(RoleTypes roleType);
}
