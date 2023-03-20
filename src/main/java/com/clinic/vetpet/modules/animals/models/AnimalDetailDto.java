package com.clinic.vetpet.modules.animals.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Animal Dto model to transfer data between client end and server
 *
 * @author Keshani
 * @since 2023/03/15
 */
@Setter
@Getter
@NoArgsConstructor
public class AnimalDetailDto {
    private Long id;
    private String animalName;
    private AnimalTypes animalType;
    private String ownerId;
    private int pageSize;
    private int pageNumber;

    public AnimalDetailDto(Long id, String animalName, AnimalTypes animalType, String ownerId) {
        this.id = id;
        this.animalName = animalName;
        this.animalType = animalType;
        this.ownerId = ownerId;
    }

    public AnimalDetailDto(Long id, String animalName, AnimalTypes animalType, String ownerId, int pageSize, int pageNumber) {
        this.id = id;
        this.animalName = animalName;
        this.animalType = animalType;
        this.ownerId = ownerId;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }
}
