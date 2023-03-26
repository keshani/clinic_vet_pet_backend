package com.clinic.vetpet.modules.admin.service;

import com.clinic.vetpet.modules.admin.models.Role;
import com.clinic.vetpet.modules.admin.models.RoleTypes;
import com.clinic.vetpet.modules.admin.models.User;
import com.clinic.vetpet.modules.admin.models.UserDto;
import com.clinic.vetpet.modules.admin.repository.RolesRepository;
import com.clinic.vetpet.modules.admin.repository.UserInfoRepository;
import com.clinic.vetpet.modules.animals.models.AnimalDetail;
import com.clinic.vetpet.modules.animals.service.AnimalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;


/**
 * Service layer to handle all the User operation logics
 *
 * @author Keshani
 * @since 2023/03/15
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AnimalDetailService animalDetailService;

    @Override
    public Page<UserDto> getListOfUsers(UserDto userDto) {
        Pageable pageable = PageRequest.of(userDto.getPageNumber(), userDto.getPageSize());
        Page<User> userPage = userInfoRepository.findAll(pageable);
        List<User> userList = userPage.getContent();
        Page<UserDto> userDtoPage = userPage.map(new Function<User, UserDto>() {
            @Override
            public UserDto apply(User entity) {
                List<AnimalDetail> animalList = animalDetailService.getListOfAnimalsByUser(entity.getUserId());
                int animalCount = 0;
                if (!animalList.isEmpty()) {
                    animalCount = animalList.size();
                }
                UserDto userDto = new UserDto();
                userDto.setUserFullName(entity.getUserFullName());
                userDto.setUserId(entity.getUserId());
                userDto.setAnimalCount(animalCount);
                return userDto;
            }
        });
        return userDtoPage;
    }

    @Override
    public User getUserInfo(String userId) {
        return userInfoRepository.findById(userId).get();
    }

    @Override
    public User addUser(UserDto userDto) {
        Optional<User> existingUser = this.userInfoRepository.findById(userDto.getUserId());
        // check weather there is existing user for given user id
        if (!existingUser.isEmpty()) {
            throw new RuntimeException("UserName is already exist");
        }
        // get default user role details
        Role userRole = rolesRepository.findByName(RoleTypes.ROLE_USER);
        Set<Role> roleSet = Set.of(userRole);

        User userInfo = new User();
        userInfo.setUserId(userDto.getUserId());
        userInfo.setRoles(roleSet);
        // encode password before saving it to the DB
        userInfo.setPassword(this.bCryptPasswordEncoder.encode(userDto.getPassword()));
        userInfo.setUserFullName(userDto.getUserFullName());
        userInfo.setEnabled(true);
        return userInfoRepository.save(userInfo);
    }

    @Override
    public User updateUser(UserDto userDto) {
        User userInfo = userInfoRepository.findById(userDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User is not found for this id:" + userDto.getUserId()));
        userInfo.setUserFullName(userDto.getUserFullName());
        return userInfoRepository.save(userInfo);
    }

    @Override
    public void deleteUser(String userInfoId) {
        userInfoRepository.deleteById(userInfoId);
    }
}

