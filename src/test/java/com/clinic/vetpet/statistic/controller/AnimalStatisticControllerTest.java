package com.clinic.vetpet.statistic.controller;

import com.clinic.vetpet.CommonUtilFunction;
import com.clinic.vetpet.configure.model.AppUserDetail;
import com.clinic.vetpet.modules.admin.models.Role;
import com.clinic.vetpet.modules.admin.models.RoleTypes;
import com.clinic.vetpet.modules.admin.models.User;
import com.clinic.vetpet.modules.admin.models.UserDto;
import com.clinic.vetpet.statistics.animalstatistic.models.AnimalStatisticsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
@Sql(scripts = "classpath:init/testdata.sql", config = @SqlConfig(separator = ";", commentPrefix = "--"))
public class AnimalStatisticControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        User user = new User();
        user.setRoles(Set.of(new Role(RoleTypes.ROLE_ADMIN)));
        user.setPassword(this.bCryptPasswordEncoder.encode("testPsw"));
        user.setUserId("testUser");
        UserDetails detail = new AppUserDetail(user);
        SecurityContextHolder.setContext( SecurityContextHolder.createEmptyContext());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken (detail,null,detail.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testAnimalStatisticForAdminRole() throws Exception {
        mockMvc.perform(get("/clinicvetpet/v1/animalStatisticHandler/animalsStatistic"))
                .andExpect(status().isOk());
    }

    @Test
    public void testAnimalStatisticForUserRole() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        mockMvc.perform(get("/clinicvetpet/v1/animalStatisticHandler/animalsStatistic"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAnimalStatAnimalTypes() throws Exception {
        MvcResult result = mockMvc.perform(get("/clinicvetpet/v1/animalStatisticHandler/animalsStatistic"))
                .andExpect(status().isOk()).andReturn();

        String content = result.getResponse().getContentAsString();
        AnimalStatisticsDto stat = new ObjectMapper().readValue(content, AnimalStatisticsDto.class);

        Assertions.assertAll(
                () -> assertNotNull(stat),
                () -> assertEquals(3, stat.getAnimalStatisticList().size())
        );
    }

}
