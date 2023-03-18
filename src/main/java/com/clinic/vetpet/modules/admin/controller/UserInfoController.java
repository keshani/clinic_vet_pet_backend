package com.clinic.vetpet.modules.admin.controller;

import com.clinic.vetpet.common.controller.BaseConroller;
import com.clinic.vetpet.modules.admin.models.User;
import com.clinic.vetpet.modules.admin.models.UserDto;
import com.clinic.vetpet.modules.admin.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * Controller class to handle user information
 *
 * @author Keshani
 * @since 2023/03/13
 */

@CrossOrigin
@RestController
public class UserInfoController extends BaseConroller {

    Logger LOGGER = LoggerFactory.getLogger(UserInfoController.class);

    @Autowired
    private UserInfoService userInfoService;

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @GetMapping("/userInfoHandler/fetchAllUsers")
    public ResponseEntity fetchAllUsers(@Valid UserDto userDto) {
        try {
            Page<UserDto> userList = userInfoService.getListOfUsers(userDto);
          return ResponseEntity.ok().body(userList);
        } catch (Exception ex) {
            LOGGER.error("userInfoController::fetchAllUsers Error", ex);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }
    }

    @GetMapping("/userInfoHandler/getUserInfo/{userId}")
    public ResponseEntity getUserInfo(@PathVariable String userId ) {
        try {
            User userInfo = userInfoService.getUserInfo(userId);
            return ResponseEntity.ok().body(userInfo);
        } catch (Exception ex) {
            LOGGER.error("userInfoController::getUserInfo Error", ex);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex);
        }
    }
    @PostMapping("/userInfoHandler/registerUser")
    public ResponseEntity registerUser(@Valid @RequestBody UserDto userDto) {
        try {
            userInfoService.addUser(userDto);
            LOGGER.info("userInfoController::registerUser Sucess - ", userDto.getUserId());
            return ResponseEntity.ok().body(null);
        } catch (Exception ex) {
            LOGGER.error("userInfoController::addUser Error - ", ex);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PutMapping("/userInfoHandler/updateUser/{userId}")
    public ResponseEntity updateUser(@Valid @RequestBody UserDto userDto, @PathVariable String userId) {
        try {
            userInfoService.updateUser(userDto);
            LOGGER.info("userInfoController::updateUser Sucess - ", userDto.getUserId());
            return ResponseEntity.ok().body(null);
        } catch (Exception ex) {
            LOGGER.info("userInfoController::updateUser Error - ", userDto.getUserId());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping("/userInfoHandler/deleteUser/{userId}")
    public ResponseEntity deleteUser( @PathVariable String userId) {
        try {
            userInfoService.deleteUser(userId);
            LOGGER.info("userInfoController::deleteUser Sucess - ", userId);
            return ResponseEntity.ok().body(null);
        } catch (Exception ex) {
            LOGGER.info("userInfoController::deleteUser Error - ", userId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

}
