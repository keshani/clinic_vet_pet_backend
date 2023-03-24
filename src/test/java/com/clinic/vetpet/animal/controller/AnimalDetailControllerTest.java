package com.clinic.vetpet.animal.controller;

import com.clinic.vetpet.CommonUtilFunction;
import com.clinic.vetpet.configure.model.AppUserDetail;
import com.clinic.vetpet.modules.admin.models.Role;
import com.clinic.vetpet.modules.admin.models.RoleTypes;
import com.clinic.vetpet.modules.admin.models.User;
import com.clinic.vetpet.modules.admin.models.UserDto;
import com.clinic.vetpet.modules.animals.models.AnimalDetail;
import com.clinic.vetpet.modules.animals.models.AnimalDetailDto;
import com.clinic.vetpet.modules.animals.models.AnimalTypes;
import com.clinic.vetpet.modules.animals.repository.AnimalDetailRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import com.fasterxml.jackson.core.type.TypeReference;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@WebAppConfiguration
@Sql(scripts = "classpath:init/testdata.sql", config = @SqlConfig(separator = ";", commentPrefix = "--")
, executionPhase = BEFORE_TEST_METHOD)
public class AnimalDetailControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AnimalDetailRepository animalDetailRepository;

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
    public void testFetchAllAnimalsBasedOnUser() throws Exception {
        mockMvc.perform(get("/clinicvetpet/v1/animalDetailHandler/animals"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testFetchAllAnimalBasedOnAdmin() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        mockMvc.perform(get("/clinicvetpet/v1/animalDetailHandler/animals")
                        .param("pageNumber", "0")
                        .param("pageSize", "5"))
                .andExpect(status().isOk());
    }

    @Test
    public void testFetchAllAnimalPagination() throws Exception {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        mockMvc.perform(get("/clinicvetpet/v1/animalDetailHandler/animals")
                        .param("pageNumber", "0")
                        .param("pageSize", "5"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(6));

        mockMvc.perform(get("/clinicvetpet/v1/animalDetailHandler/animals")
                        .param("pageNumber", "1")
                        .param("pageSize", "5"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.totalElements").value(6));
    }

    @Test
    public void testGetAnimalByUser() throws Exception {
        MvcResult result = mockMvc.perform(get("/clinicvetpet/v1/animalDetailHandler/TestUserOne/animals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty()).andReturn();

        String content = result.getResponse().getContentAsString();
        List<AnimalDetail> animalDetailList = new ObjectMapper().readValue(content, new TypeReference<List<AnimalDetail>>(){});
        assertNotNull(animalDetailList);
    }

    @Test
    @Transactional
    public void testAddAnimal() throws Exception {
        animalDetailRepository.deleteAll();
        User user = new User("TestUserOne");
        AnimalDetailDto animalDetailDto = new AnimalDetailDto(0L, "TestAnimal1", AnimalTypes.CAT,null);
        mockMvc.perform(post("/clinicvetpet/v1/animalDetailHandler/TestUserOne/animals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(CommonUtilFunction.asJsonString(animalDetailDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());

    }

    @Test
    @Transactional
    public void testAddAnimalWithDiffrentOwnerIds() throws Exception {
        animalDetailRepository.deleteAll();
        //  Test animal ownerId is taken from the path variable, not from the dto
        AnimalDetailDto animalDetailDto = new AnimalDetailDto(0L, "TestAnimal1", AnimalTypes.CAT, "TestUserOne");
        MvcResult result = mockMvc.perform(post("/clinicvetpet/v1/animalDetailHandler/TestUserTwo/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CommonUtilFunction.asJsonString(animalDetailDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty()).andReturn();
        String content = result.getResponse().getContentAsString();

        AnimalDetail animalDetail = new ObjectMapper().readValue(content, AnimalDetail.class);
        // Owner Id is not convert to json string, need to retrive the object from the  db to
        // check the owner id
        AnimalDetail savedAnimalDetail = animalDetailRepository.findById(animalDetail.getId()).get();
        assertEquals("TestUserTwo", savedAnimalDetail.getOwner().getUserId() );
    }

    @Test
    public void testAddAnimalWithIncorectData() throws Exception {
        AnimalDetailDto animalDetailDto = new AnimalDetailDto(0L, null, null, "");
        mockMvc.perform(post("/clinicvetpet/v1/animalDetailHandler/TestUserTwo/animals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(CommonUtilFunction.asJsonString(animalDetailDto)))
                .andExpect(status().isBadRequest());
    }


}
