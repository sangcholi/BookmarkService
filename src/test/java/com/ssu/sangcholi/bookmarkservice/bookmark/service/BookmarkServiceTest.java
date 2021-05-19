package com.ssu.sangcholi.bookmarkservice.bookmark.service;

import com.ssu.sangcholi.bookmarkservice.bookmark.dto.BookmarkDto;
import com.ssu.sangcholi.bookmarkservice.bookmark.entity.Bookmark;
import com.ssu.sangcholi.bookmarkservice.bookmark.exception.NotFindBookmark;
import com.ssu.sangcholi.bookmarkservice.bookmark.repository.BookmarkRepository;
import com.ssu.sangcholi.bookmarkservice.users.entity.Users;
import com.ssu.sangcholi.bookmarkservice.users.exception.NoFindUserException;
import com.ssu.sangcholi.bookmarkservice.users.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
class BookmarkServiceTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    BookmarkService bookmarkService;
    Users mockUser;


    @BeforeEach
    public void mockData() {
        mockUser = Users.builder().userId("mockUser").password("123").build();
        em.persist(mockUser);
    }

    @Test
    @DisplayName("북마크 조회")
    public void findBookmark() throws Exception {
        // given
        Bookmark bookmark = Bookmark.builder().original("org").summarization("sum").build();
        em.persist(bookmark);
        bookmark.saveUser(mockUser);
        // when
        BookmarkDto findedBookmark = bookmarkService.getBookmarkByUserIdAndBookmarkId(mockUser.getUserId(), bookmark.getId());
        // then
        Assertions.assertThat(findedBookmark).isEqualTo(modelMapper.map(bookmark, BookmarkDto.class));
    }

    @Test
    @DisplayName("없는 userId 검색으로 북마크 조회 실패")
    public void findBookmarkFailBecauseOfUserId() throws Exception {
        // given
        Bookmark bookmark = Bookmark.builder().original("org").summarization("sum").build();
        em.persist(bookmark);
        bookmark.saveUser(mockUser);
        // when
        Assertions.assertThatThrownBy(() -> bookmarkService
                .getBookmarkByUserIdAndBookmarkId("no one", bookmark.getId()))
                .isExactlyInstanceOf(NoFindUserException.class);
    }

    @Test
    @DisplayName("유효한 userId 이지만 Bookmark id가 없는 경우 북마크 조회 실패")
    public void findBookmarkFailBecauseOfBookmarkId() throws Exception {
        Assertions.assertThatThrownBy(() -> bookmarkService
                .getBookmarkByUserIdAndBookmarkId(mockUser.getUserId(), 100L))
                .isExactlyInstanceOf(NotFindBookmark.class);
    }


    @Test
    @DisplayName("북마크 생성 성공")
    public void createBookmark() throws Exception {
        // given
        Bookmark bookmark = Bookmark.builder().original("org").summarization("sum").build();
        BookmarkDto bookmarkDto = modelMapper.map(bookmark, BookmarkDto.class);
        // when
        BookmarkDto savedBookmark = bookmarkService.addBookmark(mockUser.getUserId(), bookmarkDto);
        //then
        Assertions.assertThat(bookmarkDto).isEqualTo(savedBookmark);
    }

    @Test
    @DisplayName("없는 userId로 북마크 생성 실패")
    public void createBookmarkFailBecauseOfUserId() throws Exception {
        // given
        Bookmark bookmark = Bookmark.builder().original("org").summarization("sum").build();
        BookmarkDto bookmarkDto = modelMapper.map(bookmark, BookmarkDto.class);
        // when
        //then
        Assertions.assertThatThrownBy(() -> bookmarkService.addBookmark("no one", bookmarkDto))
                .isExactlyInstanceOf(NoFindUserException.class);
    }

    @Test
    @DisplayName("북마크 삭제")
    public void deleteBookmark() throws Exception {
        // given
        Bookmark bookmark = Bookmark.builder().original("org").summarization("sum").build();
        em.persist(bookmark);
        bookmark.saveUser(mockUser);
        bookmarkService.deleteBookmark(mockUser.getUserId(), bookmark.getId());
        // when & then
        Assertions.assertThat(em.find(Bookmark.class, bookmark.getId())).isNull();
    }

    @Test
    @DisplayName("북마크 삭제 실패 없는 userId")
    public void deleteBookmarkFail() throws Exception {
        //given
        Bookmark bookmark = Bookmark.builder().original("org").summarization("sum").build();
        em.persist(bookmark);
        bookmark.saveUser(mockUser);
        // when & then
        Assertions.assertThatThrownBy(() -> bookmarkService.deleteBookmark("no one", bookmark.getId()))
                .isExactlyInstanceOf(NotFindBookmark.class);
    }


}