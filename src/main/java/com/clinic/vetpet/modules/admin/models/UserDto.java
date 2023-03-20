package com.clinic.vetpet.modules.admin.models;

import com.clinic.vetpet.modules.animals.models.AnimalDetailDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * User entity for store user information
 *
 * @author Keshani
 * @since 2023/03/15
 */
@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String userFullName;
    private String password;
    private List<AnimalDetailDto> animals;
    private boolean enabled;
    private int pageSize;
    private int pageNumber;
    private int animalCount;


    public UserDto(String userId, String userFullName, String password, boolean enabled) {
        this.userId = userId;
        this.userFullName = userFullName;
        this.password = password;
        this.enabled = enabled;
    }
}
