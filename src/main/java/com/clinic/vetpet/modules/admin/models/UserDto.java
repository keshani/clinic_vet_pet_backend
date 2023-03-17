package com.clinic.vetpet.modules.admin.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * User entity for store user information
 *
 * @author Keshani
 * @since 2021/11/13
 */
@Getter
@Setter
public class UserDto {
    private String userId;
    private String userFullName;
    private String password;
    private boolean enabled;
    private int pageSize;
    private int pageNumber;

}
