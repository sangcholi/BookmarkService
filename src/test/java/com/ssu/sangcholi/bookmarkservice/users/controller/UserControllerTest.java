package com.ssu.sangcholi.bookmarkservice.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssu.sangcholi.bookmarkservice.users.dto.UserDto;
import com.ssu.sangcholi.bookmarkservice.users.entity.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    @DisplayName("user 생성")
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
    @DisplayName("중복된 userId로 user 생성 실패")
    public void createUserFailBecauseOfUserId() throws Exception {
        UserDto body = UserDto.builder()
                .userId("test1")
                .password("123")
                .build();
        mockMvc.perform
                (post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value("This ID is already exist"));
    }

    @Test
    @DisplayName("데이터 누락한 시 user 생성 실패")
    public void createUserFailBecauseOfData() throws Exception {
        UserDto body = UserDto.builder()
                .userId("")
                .password("")
                .build();

        mockMvc.perform
                (post("/users/signup").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("user 검색")
    public void findUserById() throws Exception {
        mockMvc.perform(get("/users/test1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId").value("test1"));
    }

    @Test
    @DisplayName("없는 userId로 user 검색 실패")
    public void findUserByIdFailBecauseOfUserId() throws Exception {
        mockMvc.perform(get("/users/no one"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Not Exist UserId"));
    }

    @Test
    @DisplayName("없는 아이디 checkId 시 사용 가능")
    public void checkUserId() throws Exception {
        mockMvc.perform(get("/users/checkid/noone"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("있는 아이디 checkId 시 사용 불능 ")
    public void checkUserIdNotUse() throws Exception {
        mockMvc.perform(get("/users/checkid/test1"))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message").value("This ID is already exist"));
    }

    @Test
    @DisplayName("user 삭제 성공")
    public void deleteUser() throws Exception {
        Users user = Users.builder().userId("delete").password("123").build();
        em.persist(user);

        mockMvc.perform(delete("/users/delete"))
                .andExpect(status().isOk());

        Assertions.assertThat(em.find(Users.class, user.getId())).isNull();
    }

    @Test
    @DisplayName("없는 userId로 user 삭제 실패")
    public void deleteUserFail() throws Exception {
        mockMvc.perform(delete("/users/delete"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Not Exist UserId"));
    }
}