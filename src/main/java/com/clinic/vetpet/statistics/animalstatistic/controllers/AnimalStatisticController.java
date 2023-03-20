package com.clinic.vetpet.statistics.animalstatistic.controllers;

import com.clinic.vetpet.modules.animals.controller.AnimalDetailController;
import com.clinic.vetpet.modules.animals.models.AnimalDetail;
import com.clinic.vetpet.modules.animals.models.AnimalDetailDto;
import com.clinic.vetpet.modules.animals.service.AnimalDetailService;
import com.clinic.vetpet.statistics.animalstatistic.models.AnimalStatisticsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller class to handle all the Animal Statistic operations
 *
 * @author Keshani
 * @since 2023/03/15
 */

@CrossOrigin
@RestController
@RequestMapping(value = "/clinicvetpet/v1/animalStatisticHandler")
public class AnimalStatisticController {

    Logger LOGGER = LoggerFactory.getLogger(AnimalStatisticController.class);

    @Autowired
    private AnimalDetailService animalDetailService;

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping("/animalsStatistic")
    public ResponseEntity getAnimalStatisticData() {
        try {
            AnimalStatisticsDto statData = animalDetailService.getAnimalStatistics();
            return ResponseEntity.ok().body(statData);
        } catch (Exception ex) {
            LOGGER.error("AnimalStatisticController::getAnimalStatisticData Error", ex);
            throw ex;
        }
    }
}
