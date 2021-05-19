package com.ssu.sangcholi.bookmarkservice.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssu.sangcholi.bookmarkservice.users.dto.UserDto;
import com.ssu.sangcholi.bookmarkservice.users.entity.Users;
import jdk.jfr.Description;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void before() {
        Users user1 = Users.builder()
                .userId("test1")
                .password("123")
                .build();
        Users user2 = Users.builder()
                .userId("test2")
                .password("123")
                .build();
        Users user3 = Users.builder()
                .userId("test3")
                .password("123")
                .build();
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
    }

    @Test
    @Description("user 생성")
    public void createUser() throws Exception {
        UserDto body = UserDto.builder()
                .userId("user1")
                .password("123")
                .build();

        mockMvc.perform
                (
                        post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("userId").value(body.getUserId()));
    }

    @Test
    @Description("모든 users검색")
    public void findAllUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Description("user 검색")
    public void findUserById() throws Exception {
        mockMvc.perform(get("/users/test1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").value("test1"))
                .andExpect(jsonPath("_links").exists());
    }

}