package com.clinic.vetpet.security.controller;

import com.clinic.vetpet.CommonUtilFunction;
import com.clinic.vetpet.configure.model.AppUserDetail;
import com.clinic.vetpet.configure.model.AuthenticationRequest;
import com.clinic.vetpet.configure.model.AuthenticationResponse;
import com.clinic.vetpet.configure.util.JWTUtil;
import com.clinic.vetpet.exception.exceptionType.BadCredentialException;
import com.clinic.vetpet.modules.admin.models.Role;
import com.clinic.vetpet.modules.admin.models.RoleTypes;
import com.clinic.vetpet.modules.admin.models.User;
import com.clinic.vetpet.modules.admin.models.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
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

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@Sql(scripts = "classpath:init/testdata.sql", config = @SqlConfig(separator = ";", commentPrefix = "--"))

public class AuthenticationControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JWTUtil jWTUtil;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }


    @Test
    void testAuthenticationWithValidCredentials() throws Exception {
        AuthenticationRequest authReq = new AuthenticationRequest( "TestUserOne", "password");
        MvcResult result =  mockMvc.perform(post("/clinicvetpet/v1/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CommonUtilFunction.asJsonString(authReq)))
                .andExpect(status().isOk()).andReturn();
  }

    @Test
    void testAuthenticationReturnValidJwtToken() throws Exception {
        AuthenticationRequest authReq = new AuthenticationRequest( "TestUserOne", "password");
        MvcResult result =  mockMvc.perform(post("/clinicvetpet/v1/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CommonUtilFunction.asJsonString(authReq)))
                .andExpect(status().isOk()).andReturn();

        // Extract the JWT token from the response
        AuthenticationResponse authResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(), AuthenticationResponse.class);
        assertEquals("TestUserOne", jWTUtil.extractUsername(authResponse.getJwtToken()));
    }

    @Test
    void testAuthenticationWithInvalidUsername() throws Exception {
        AuthenticationRequest authReq = new AuthenticationRequest( "TestUserOneee", "password");
        mockMvc.perform(post("/clinicvetpet/v1/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CommonUtilFunction.asJsonString(authReq)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAuthenticationWithInvalidPassword() throws Exception {
        AuthenticationRequest authReq = new AuthenticationRequest( "TestUserOne", "password12");
        mockMvc.perform(post("/clinicvetpet/v1/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CommonUtilFunction.asJsonString(authReq)))
                .andExpect(status().isUnauthorized());
    }
}
