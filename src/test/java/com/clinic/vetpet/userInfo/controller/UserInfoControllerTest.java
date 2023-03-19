package com.clinic.vetpet.userInfo.controller;

import com.clinic.vetpet.CommonUtilFunction;
import com.clinic.vetpet.configure.model.AppUserDetail;
import com.clinic.vetpet.modules.admin.models.Role;
import com.clinic.vetpet.modules.admin.models.RoleTypes;
import com.clinic.vetpet.modules.admin.models.User;
import com.clinic.vetpet.modules.admin.models.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
@Sql(scripts = "classpath:init/testdata.sql", config = @SqlConfig(separator = ";", commentPrefix = "--"))
public class UserInfoControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        User user = new User();
        user.setRoles(Set.of(new Role(RoleTypes.ROLE_USER)));
        user.setPassword(this.bCryptPasswordEncoder.encode("testPsw"));
        user.setUserId("testUser");
        UserDetails detail = new AppUserDetail(user);
        SecurityContextHolder.setContext( SecurityContextHolder.createEmptyContext());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken (detail,null,detail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testUserRegister() throws Exception {
        UserDto newUser = new UserDto("userId", "userFullName", "password", true);
        mockMvc.perform(post("/clinicvetpet/v1/userInfoHandler/registerUser")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtilFunction.asJsonString(newUser)))
                .andExpect(status().isOk());
    }

    @Test
    public void testExistingUserRegister() throws Exception {
        UserDto newUser = new UserDto("TestUserOne", "userFullName", "password", true);
        mockMvc.perform(post("/clinicvetpet/v1/userInfoHandler/registerUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CommonUtilFunction.asJsonString(newUser)))
                .andExpect(status().isConflict());
    }

    @Test
    public void testRetriveUserByUserId() throws Exception {
        UserDto newUser = new UserDto("userId", "userFullName", "password", true);
        mockMvc.perform(post("/clinicvetpet/v1/userInfoHandler/registerUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtilFunction.asJsonString(newUser))) ;

        MvcResult result = mockMvc.perform(get("/clinicvetpet/v1/userInfoHandler/users/userId","userId"))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        User actualUser = new ObjectMapper().readValue(content, User.class);

        assertNotNull(actualUser);
        assertEquals(newUser.getUserId(), actualUser.getUserId());
    }

    @Test
    public void testUpdateUserByUserId() throws Exception {
        UserDto newUser = new UserDto();
        newUser.setUserFullName("Keshani New Full Name");
        mockMvc.perform(put("/clinicvetpet/v1/userInfoHandler/users/TestUserOne", "TestUserOne")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtilFunction.asJsonString(newUser)))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/clinicvetpet/v1/userInfoHandler/users/TestUserOne","TestUserOne"))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        User actualUser = new ObjectMapper().readValue(content, User.class);

        assertNotNull(actualUser);
        assertEquals(newUser.getUserFullName(), actualUser.getUserFullName());
    }

    @Test
    public void testFetchAllUsersBasedOnUser() throws Exception {
          mockMvc.perform(get("/clinicvetpet/v1/userInfoHandler/users"))
                  .andExpect(status().isBadRequest());
    }

    @Test
    public void testFetchAllUsersBasedOnAdmin() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        mockMvc.perform(get("/clinicvetpet/v1/userInfoHandler/users")
                .param("pageNumber", "0")
                .param("pageSize", "5"))
                .andExpect(status().isOk());
    }
}
