package com.clinic.vetpet.modules.animals.models;

import lombok.Getter;
import lombok.Setter;

/**
 * City Dto model to transfer data between client end and server
 *
 * @author Keshani
 * @since 2021/11/13
 */
@Setter
@Getter
public class AnimalDetailDto {
    private Long id;
    private String animalName;
    private AnimalTypes animalType;
    private String ownerId;
    private int pageSize;
    private int pageNumber;

}
