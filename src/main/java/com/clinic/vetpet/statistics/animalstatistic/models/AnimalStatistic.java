package com.clinic.vetpet.statistics.animalstatistic.models;

import com.clinic.vetpet.modules.animals.models.AnimalTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalStatistic {
    private Long animalCount;
    private AnimalTypes animalType;
}
