package com.clinic.vetpet.modules.animals.controller;

import com.clinic.vetpet.common.controller.BaseConroller;
import com.clinic.vetpet.modules.animals.models.AnimalDetail;
import com.clinic.vetpet.modules.animals.models.AnimalDetailDto;
import com.clinic.vetpet.modules.animals.service.AnimalDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * Controller class to handle all the Animal Detail operations
 *
 * @author Keshani
 * @since 2023/03/15
 */

@CrossOrigin
@RestController
@RequestMapping(value = "/clinicvetpet/v1/animalDetailHandler")
public class AnimalDetailController {

    Logger LOGGER = LoggerFactory.getLogger(AnimalDetailController.class);

    @Autowired
    private AnimalDetailService animalDetailService;

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping("/animals")
    public ResponseEntity getAllAnimals(@Valid AnimalDetailDto animalDetailDto) {
        try {
          Page<AnimalDetail> animalList = animalDetailService.getListOfAnimals(animalDetailDto);
          return ResponseEntity.ok().body(animalList);
        } catch (Exception ex) {
            LOGGER.error("AnimalDetailController::fetchAllAnimals Error", ex);
            throw ex;
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @GetMapping("/{ownerId}/animals")
    public ResponseEntity getAnimalByUserId(@PathVariable String ownerId) {
        try {
            List<AnimalDetail> animalList = animalDetailService.getListOfAnimalsByUser(ownerId);
            return ResponseEntity.ok().body(animalList);
        } catch (Exception ex) {
            LOGGER.error("AnimalDetailController::getAnimalByUserId Error", ex);
            throw ex;
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @PostMapping("/{ownerId}/animals")
    public ResponseEntity addAnimalDetails(@PathVariable String ownerId,  @RequestBody AnimalDetailDto animalDetailDto) {
        try {
            animalDetailDto.setOwnerId(ownerId);
            AnimalDetail animalDetail = animalDetailService.addAnimalDetail(animalDetailDto);
            LOGGER.info("AnimalDetailController::addAnimalDetail Sucess - ", animalDetail.getId());
            return ResponseEntity.ok().body(animalDetail);
        } catch (Exception ex) {
            LOGGER.info("AnimalDetailController::addAnimalDetail Error - ",ex);
            throw ex;
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @PutMapping("/{ownerId}/animals/{id}")
    public ResponseEntity updateAnimalDetails(@Valid @RequestBody AnimalDetailDto animalDetailDto, @PathVariable Long id) {
        try {
            animalDetailDto.setId(id);
            animalDetailService.updateAnimalDetail(animalDetailDto);
            LOGGER.info("AnimalDetailController::updateAnimalDetails Sucess - ", animalDetailDto.getId());
            return ResponseEntity.ok().body(null);
        } catch (Exception ex) {
            LOGGER.info("AnimalDetailController::updateAnimalDetails Error - ", animalDetailDto.getId());
            throw ex;
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @DeleteMapping("/animals/{animalId}")
    public ResponseEntity deleteAnimalDetails( @PathVariable Long animalId) {
        try {
            animalDetailService.deleteAnimalDetail(animalId);
            LOGGER.info("AnimalDetailController::deleteAnimalDetails Sucess - ", animalId);
            return ResponseEntity.ok().body(null);
        } catch (Exception ex) {
            LOGGER.info("AnimalDetailController::deleteAnimalDetails Error - ", animalId);
            throw ex;
        }
    }

}
