package com.clinic.vetpet.modules.admin.service;

import com.clinic.vetpet.modules.admin.models.RoleTypes;
import com.clinic.vetpet.modules.admin.models.Role;
import com.clinic.vetpet.modules.admin.models.UserDto;
import com.clinic.vetpet.modules.admin.repository.RolesRepository;
import com.clinic.vetpet.modules.admin.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.clinic.vetpet.modules.admin.models.User;

import java.util.Optional;
import java.util.Set;


/**
 * Service layer to handle all the City operation logics
 *
 * @author Keshani
 * @since 2021/11/13
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Page<User> getListOfUsers(UserDto userDto) {
        Pageable pageable = PageRequest.of(userDto.getPageNumber(), userDto.getPageSize());
        return userInfoRepository.findAll(pageable);
    }

    @Override
    public User getUserInfo(String userId) {
        return userInfoRepository.findById(userId).get();
    }


    @Override
    public void addUser(UserDto userDto) {

        Optional<User> existingUser = this.userInfoRepository.findById(userDto.getUserId());
        if(!existingUser.isEmpty()) {
            throw new RuntimeException("UserName is alredy exisist");
        }
        Role userRole = rolesRepository.findByName(RoleTypes.ROLE_USER);
        Set<Role> roleSet = Set.of(userRole);
        User userInfo = new User();
        userInfo.setUserId(userDto.getUserId());
        userInfo.setRoles(roleSet);
        userInfo.setPassword(this.bCryptPasswordEncoder.encode(userDto.getPassword()));
        userInfo.setUserFullName(userDto.getUserFullName());
        userInfo.setEnabled(true);
        userInfoRepository.save(userInfo);
    }

    @Override
    public void updateUser(UserDto userDto) {
        User userInfo = userInfoRepository.findById(userDto.getUserId())
                  .orElseThrow(() -> new RuntimeException("Animal detail is not found for this id:" + userDto.getUserId()));
        userInfo.setUserFullName(userDto.getUserFullName());
          userInfoRepository.save(userInfo);
    }

    @Override
    public void deleteUser(String userInfoId) {
     userInfoRepository.deleteById(userInfoId);
}   }
