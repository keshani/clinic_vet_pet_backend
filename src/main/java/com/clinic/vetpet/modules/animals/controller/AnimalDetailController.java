package com.clinic.vetpet.modules.animals.controller;

import com.clinic.vetpet.controller.BaseConroller;
import com.clinic.vetpet.modules.animals.models.AnimalDetail;
import com.clinic.vetpet.modules.animals.models.AnimalDetailDto;
import com.clinic.vetpet.modules.animals.service.AnimalDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


/**
 * Controller class to handle all the City view operations
 *
 * @author Keshani
 * @since 2021/11/13
 */

@CrossOrigin
@RestController
public class AnimalDetailController extends BaseConroller {

    Logger LOGGER = LoggerFactory.getLogger(AnimalDetailController.class);

    @Autowired
    private AnimalDetailService animalDetailService;

    @GetMapping("/fetchAllAnimals")
    public ResponseEntity fetchAnimalForUser(@Valid AnimalDetailDto animalDetailDto) {
        try {
          Page<AnimalDetail> animalList = animalDetailService.getListOfAnimals(animalDetailDto);
          return ResponseEntity.ok().body(animalList);
        } catch (Exception ex) {
            LOGGER.error("AnimalDetailController::fetchAllAnimals Error", ex);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }
    @GetMapping("/animalDetailHandler/getAnimalByUserId/{ownerId}")
    public ResponseEntity getAnimalByUserId(@PathVariable String ownerId) {
        try {
            List<AnimalDetail> animalList = animalDetailService.getListOfAnimalsByUser(ownerId);
            return ResponseEntity.ok().body(animalList);
        } catch (Exception ex) {
            LOGGER.error("AnimalDetailController::getAnimalByUserId Error", ex);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }
    @PostMapping("/animalDetailHandler/addAnimalDetails/{ownerId}")
    public ResponseEntity addAnimalDetails(@Valid @PathVariable String ownerId, @RequestBody AnimalDetailDto animalDetailDto) {
        try {
            animalDetailService.addAnimalDetail(animalDetailDto);
            LOGGER.info("AnimalDetailController::addAnimalDetail Sucess - ", animalDetailDto.getId());
            return ResponseEntity.ok().body(null);
        } catch (Exception ex) {
            LOGGER.info("AnimalDetailController::addAnimalDetail Error - ", animalDetailDto.getId());
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/animalDetailHandler/updateAnimalDetails/{ownerId}")
    public ResponseEntity updateAnimalDetails(@Valid @RequestBody AnimalDetailDto animalDetailDto) {
        try {
            animalDetailService.updateAnimalDetail(animalDetailDto);
            LOGGER.info("AnimalDetailController::updateAnimalDetails Sucess - ", animalDetailDto.getId());
            return ResponseEntity.ok().body(null);
        } catch (Exception ex) {
            LOGGER.info("AnimalDetailController::updateAnimalDetails Error - ", animalDetailDto.getId());
            return ResponseEntity.status(500).body(null);
        }
    }

    @DeleteMapping("/animalDetailHandler/deleteAnimalDetails/{animalId}")
    public ResponseEntity deleteAnimalDetails( @PathVariable Long animalId) {
        try {
            animalDetailService.deleteAnimalDetail(animalId);
            LOGGER.info("AnimalDetailController::deleteAnimalDetails Sucess - ", animalId);
            return ResponseEntity.ok().body(null);
        } catch (Exception ex) {
            LOGGER.info("AnimalDetailController::deleteAnimalDetails Error - ", animalId);
            return ResponseEntity.status(500).body(null);
        }
    }

}
