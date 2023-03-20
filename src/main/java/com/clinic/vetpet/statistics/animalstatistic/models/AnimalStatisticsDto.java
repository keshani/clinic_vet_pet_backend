package com.clinic.vetpet.statistics.animalstatistic.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalStatisticsDto {

    private Long totalAnimals;
    private List<AnimalStatistic> animalStatisticList;
}
