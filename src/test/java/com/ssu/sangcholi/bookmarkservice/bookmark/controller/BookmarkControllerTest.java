package com.ssu.sangcholi.bookmarkservice.bookmark.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssu.sangcholi.bookmarkservice.bookmark.dto.BookmarkDto;
import com.ssu.sangcholi.bookmarkservice.bookmark.entity.Bookmark;
import com.ssu.sangcholi.bookmarkservice.bookmark.service.BookmarkService;
import com.ssu.sangcholi.bookmarkservice.users.entity.Users;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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
class BookmarkControllerTest {
    @Autowired
    BookmarkService bookmarkService;
    @PersistenceContext
    EntityManager em;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ModelMapper modelMapper;
    ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @DisplayName("Bookmark 생성 요청")
    public void getBookmark() throws Exception {
        // given
        Users user = Users.builder().userId("user1").password("123").build();
        Bookmark bookmark = Bookmark.builder().original("original").summarization("sum").build();
        em.persist(user);
        // when & then
        BookmarkDto dto = modelMapper.map(bookmark, BookmarkDto.class);
        mockMvc.perform(post(String.format("/bookmarks/%s", user.getUserId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("original").value(dto.getOriginal()))
                .andExpect(jsonPath("summarization").value(dto.getSummarization()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("데이터 누락으로 인한 Bookmark 생성 실패")
    public void getBookmarkFailBecauseOfData() throws Exception {
        // given
        Users user = Users.builder().userId("user1").password("123").build();
        Bookmark bookmark = Bookmark.builder().original("").summarization("sum").build();
        em.persist(user);
        // when & then
        BookmarkDto dto = modelMapper.map(bookmark, BookmarkDto.class);
        mockMvc.perform(post(String.format("/bookmarks/%s", user.getUserId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
        bookmark = Bookmark.builder().original("org").summarization("").build();
        dto = modelMapper.map(bookmark, BookmarkDto.class);
        mockMvc.perform(post(String.format("/bookmarks/%s", user.getUserId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("없는 userId 인한 Bookmark 생성 실패")
    public void getBookmarkFail() throws Exception {
        // given
        Users user = Users.builder().userId("user1").password("123").build();
        Bookmark bookmark = Bookmark.builder().original("org").summarization("sum").build();
        // when & then
        BookmarkDto dto = modelMapper.map(bookmark, BookmarkDto.class);
        mockMvc.perform(post(String.format("/bookmarks/%s", user.getUserId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Not Exist UserId"));
    }

    @Test
    @DisplayName("Bookmark 전체 조회")
    public void retrieveBookmarkAll() throws Exception {
        // given
        Users user = Users.builder().userId("mockuser").password("123").build();
        Bookmark bookmark = Bookmark.builder().original("org").summarization("sum").build();
        em.persist(user);
        em.persist(bookmark);
        bookmark.saveUser(user);

        // when & then
        mockMvc.perform(get(String.format("/bookmarks/%s", user.getUserId())))
                .andDo(print())
                .andExpect(jsonPath("$[0].original").value(bookmark.getOriginal()))
                .andExpect(jsonPath("$[0].summarization").value(bookmark.getSummarization()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("잘못된 userId로 인한 Bookmark 전체 조회 실패")
    public void retrieveBookmarkAllFailBecauseOfUserId() throws Exception {
        // given
        Users user = Users.builder().userId("mockuser").password("123").build();
        Bookmark bookmark = Bookmark.builder().original("org").summarization("sum").build();
        em.persist(user);
        em.persist(bookmark);
        bookmark.saveUser(user);
        // when & then
        mockMvc.perform(get(String.format("/bookmarks/%s", "no one")))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Not Exist UserId"));
    }

    @Test
    @DisplayName("특정 Bookmark 조회 성공")
    public void retrieveBookmark() throws Exception {
        // given
        Users user = Users.builder().userId("mockuser").password("123").build();
        Bookmark bookmark = Bookmark.builder().original("org").summarization("sum").build();
        em.persist(user);
        em.persist(bookmark);
        bookmark.saveUser(user);

        // when & then
        mockMvc.perform(get(String.format("/bookmarks/%s/%d", user.getUserId(), bookmark.getId())))
                .andDo(print())
                .andExpect(jsonPath("original").value(bookmark.getOriginal()))
                .andExpect(jsonPath("summarization").value(bookmark.getSummarization()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("없는 userId로 특정 Bookmark 조회 실패")
    public void retrieveBookmarkFailBecauseOfUserId() throws Exception {
        // given
        Users user = Users.builder().userId("mockuser").password("123").build();
        Bookmark bookmark = Bookmark.builder().original("org").summarization("sum").build();
        em.persist(user);
        em.persist(bookmark);
        bookmark.saveUser(user);

        // when & then
        mockMvc.perform(get(String.format("/bookmarks/%s/%d", "no one", bookmark.getId())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Not Exist UserId"));
    }

    @Test
    @DisplayName("없는 bookmark로 특정 Bookmark 조회 실패")
    public void retrieveBookmarkFailBecauseOfNoBookmark() throws Exception {
        // given
        Users user = Users.builder().userId("mockuser").password("123").build();
        em.persist(user);

        // when & then
        mockMvc.perform(get(String.format("/bookmarks/%s/%d", user.getUserId(), 100L)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Not Find bookmark"));
    }

    @Test
    @DisplayName("특정 bookmark 삭제")
    public void deleteBookmark() throws Exception {
        // given
        Users user = Users.builder().userId("mockuser").password("123").build();
        Bookmark bookmark = Bookmark.builder().original("org").summarization("sum").build();
        em.persist(user);
        em.persist(bookmark);
        bookmark.saveUser(user);
        // when
        mockMvc.perform(delete(String.format("/bookmarks/%s/%d", user.getUserId(), bookmark.getId())))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        Assertions.assertThat(em.find(Bookmark.class, bookmark.getId())).isNull();
    }


    @Test
    @DisplayName("없는 userId로 특정 bookmark 삭제 실패")
    public void deleteBookmarkFailBecauseOfUserId() throws Exception {
        // given
        Users user = Users.builder().userId("mockuser").password("123").build();
        Bookmark bookmark = Bookmark.builder().original("org").summarization("sum").build();
        em.persist(user);
        em.persist(bookmark);
        bookmark.saveUser(user);
        // when & then
        mockMvc.perform(delete(String.format("/bookmarks/%s/%d", "no one", bookmark.getId())))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Not Exist UserId"));
    }

    @Test
    @DisplayName("없는 Bookmark로 특정 bookmark 삭제 실패")
    public void deleteBookmarkFailBecauseOfBookmark() throws Exception {
        // given
        Users user = Users.builder().userId("mockuser").password("123").build();
        Bookmark bookmark = Bookmark.builder().original("org").summarization("sum").build();
        em.persist(user);
        em.persist(bookmark);
        bookmark.saveUser(user);
        // when & then
        mockMvc.perform(delete(String.format("/bookmarks/%s/%d", user.getUserId(), 100L)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message").value("Not Find bookmark"));
    }

}