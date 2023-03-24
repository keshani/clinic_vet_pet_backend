package com.clinic.vetpet.userInfo.service;

import com.clinic.vetpet.configure.model.AppUserDetail;
import com.clinic.vetpet.modules.admin.models.Role;
import com.clinic.vetpet.modules.admin.models.RoleTypes;
import com.clinic.vetpet.modules.admin.models.User;
import com.clinic.vetpet.modules.admin.models.UserDto;
import com.clinic.vetpet.modules.admin.repository.RolesRepository;
import com.clinic.vetpet.modules.admin.repository.UserInfoRepository;
import com.clinic.vetpet.modules.admin.service.UserInfoService;
import com.clinic.vetpet.modules.admin.service.UserInfoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Sql(scripts = "classpath:init/testdata.sql", config = @SqlConfig(separator = ";", commentPrefix = "--"))
public class UserInfoServiceTest {

    @InjectMocks
    private UserInfoServiceImpl userInfoService;

    @Mock
    private UserInfoRepository userInfoRepository;

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void init() {
        user = new User("TestUser", "Test User", "TestPassword",true);
        userDto = new UserDto("TestUser", "Test User", "TestPassword",true );
    }

    @Test
    void testGetUserById() {
        String userId = "TestUser";
        Mockito.when(userInfoRepository.findById(userId)).thenReturn(Optional.of(user));

        User user = userInfoService.getUserInfo(userId);
        Assertions.assertEquals(userId, user.getUserId());
    }

    @Test
    void testAddUser() {
        String userId = "TestUser";
        Role userRole = new Role(1, RoleTypes.ROLE_USER);
        user.setRoles(Set.of(userRole));
        Mockito.when(userInfoRepository.findById(userId)).thenReturn(Optional.empty());
        Mockito.when(rolesRepository.findByName(RoleTypes.ROLE_USER)).thenReturn(userRole);
        Mockito.when(userInfoRepository.save(Mockito.any())).thenReturn(user);

        User userResponse = userInfoService.addUser(userDto);
        Assertions.assertAll(
                () -> assertNotNull(userResponse),
                () -> assertEquals(user.getUserId(), userResponse.getUserId()),
                () -> assertEquals(userResponse.getRoles(),Set.of(userRole))
        );
    }

    @Test
    void testUpdateUser() {
        String userId = "TestUser";
        String updatedUserFullName = "Test User Full Name";
        userDto.setUserFullName(updatedUserFullName);
        User updatedUser = new User("TestUser", updatedUserFullName, "TestPassword",true);

        Mockito.when(userInfoRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(userInfoRepository.save(Mockito.any())).thenReturn(updatedUser);

        User userResponse = userInfoService.updateUser(userDto);
        Assertions.assertAll(
                () -> assertNotNull(userResponse),
                () -> assertEquals(user.getUserId(), userResponse.getUserId()),
                () -> assertEquals(updatedUserFullName, userResponse.getUserFullName())
        );
    }

}
