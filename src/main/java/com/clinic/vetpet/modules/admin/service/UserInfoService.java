package com.clinic.vetpet.modules.admin.service;

import com.clinic.vetpet.modules.admin.models.User;
import com.clinic.vetpet.modules.admin.models.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserInfoService {
    Page<User> getListOfUsers(UserDto userDto);

    User getUserInfo(String userId);

    void addUser(UserDto userDto);

    void updateUser(UserDto userDto);

    void deleteUser(String userDto);
}
