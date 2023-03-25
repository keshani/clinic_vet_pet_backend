package com.clinic.vetpet.security.controller;

import com.clinic.vetpet.CommonUtilFunction;
import com.clinic.vetpet.configure.filter.JwtFilter;
import com.clinic.vetpet.configure.model.AppUserDetail;
import com.clinic.vetpet.configure.model.AuthenticationRequest;
import com.clinic.vetpet.configure.model.AuthenticationResponse;
import com.clinic.vetpet.configure.util.JWTUtil;
import com.clinic.vetpet.modules.admin.models.Role;
import com.clinic.vetpet.modules.admin.models.RoleTypes;
import com.clinic.vetpet.modules.admin.models.User;
import com.clinic.vetpet.modules.admin.models.UserDto;
import com.clinic.vetpet.modules.admin.service.UserInfoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Sql(scripts = "classpath:init/testdata.sql", config = @SqlConfig(separator = ";", commentPrefix = "--"))
@Transactional
public class JwtFilterTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JWTUtil jWTUtil;

    @Autowired
    UserInfoService userInfoService;

    @Autowired
    JwtFilter jwtFilter;

    private UserDetails detail;
    private String jwtoken;

    @BeforeEach
    public void init() {

       mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(jwtFilter).build();
        User user = new User();
        user.setRoles(Set.of(new Role(RoleTypes.ROLE_USER), new Role(RoleTypes.ROLE_ADMIN)));
        user.setPassword(this.bCryptPasswordEncoder.encode("password"));
        user.setUserId("TestUserOne");

        detail = new AppUserDetail(user);
        jwtoken = jWTUtil.generateToken(detail);
    }


    @Test
    public void testJwtHeaderWithValidToken() throws Exception {
        mockMvc.perform(get("/clinicvetpet/v1/userInfoHandler/users/AdminUser")
                        .header("Authorization", "Bearer " + jwtoken))
                .andExpect(status().isOk());
    }

    @Test
    public void testJwtHeaderWithInvalidToken() throws Exception {
        String invalidoken = "eyJhbGciOiJIUzI1NiJ9.eyJJc3N1ZXIiOiJrZXNoYW5pIiwiVXNlcm5hbWUiOiJBZG1pblVzZXIiLCJleHAiOjE2Nzk3NzYzMjIsImlhdCI6MTY3OTc3NjMyMn0.I6b0UPpvypzd-mjdedpB4vlmlYcc0iz5VZ5l90-6Qrs";
       Assertions.assertThrows( SignatureException.class,() -> mockMvc.perform(get("/clinicvetpet/v1/userInfoHandler/users/AdminUser")
                        .header("Authorization", "Bearer "+invalidoken)));
    }

    @Test
    public void testJwtHeaderWithExpiredToken() throws Exception {
        String expiredToken = "eyJhbGciOiJIUzI1NiJ9.eyJVc2VybmFtZSI6IlRlc3RVc2VyT25lIiwiZXhwIjoxNjc5NjkyNzYzLCJpYXQiOjE2Nzk2MDYzNjN9.IMT38MSNoLBEfGgvrzGeHk4EFsJ4bWWSGWBl9kHPXHM";
        Assertions.assertThrows( ExpiredJwtException.class,() -> mockMvc.perform(get("/clinicvetpet/v1/userInfoHandler/users/AdminUser")
                .header("Authorization", "Bearer "+expiredToken)));
    }

    @Test
    public void testRequestWithoutJWTHeader() throws Exception {
        mockMvc.perform(get("/clinicvetpet/v1/userInfoHandler/users/AdminUser"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testWhiteListedUrlsWithoutJWTToken() throws Exception {
        AuthenticationRequest authReq = new AuthenticationRequest( "TestUserOne", "password");
        mockMvc.perform(post("/clinicvetpet/v1/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CommonUtilFunction.asJsonString(authReq)))
                .andExpect(status().isOk());

        UserDto newUser = new UserDto("userId", "userFullName", "password", true);
        mockMvc.perform(post("/clinicvetpet/v1/userInfoHandler/registerUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CommonUtilFunction.asJsonString(newUser)))
                .andExpect(status().isOk());
    }

    @Test
    public void testJWTTokenWithInvalidUserId() throws Exception {
        User user = new User();
        user.setRoles(Set.of(new Role(RoleTypes.ROLE_USER), new Role(RoleTypes.ROLE_ADMIN)));
        user.setPassword(this.bCryptPasswordEncoder.encode("password"));
        user.setUserId("TestUser123");

        AppUserDetail detail1 = new AppUserDetail(user);
        String jwtoken1 = jWTUtil.generateToken(detail1);
        Assertions.assertThrows(NoSuchElementException.class, () -> mockMvc.perform(get("/clinicvetpet/v1/userInfoHandler/users/AdminUser")
                        .header("Authorization", "Bearer " + jwtoken1)));
    }
}
