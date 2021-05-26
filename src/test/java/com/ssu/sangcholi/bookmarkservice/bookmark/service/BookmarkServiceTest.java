package com.ssu.sangcholi.bookmarkservice.bookmark.service;

import com.ssu.sangcholi.bookmarkservice.bookmark.dto.BookmarkDto;
import com.ssu.sangcholi.bookmarkservice.bookmark.entity.Bookmark;
import com.ssu.sangcholi.bookmarkservice.bookmark.exception.NotFindBookmarkExcption;
import com.ssu.sangcholi.bookmarkservice.users.entity.Users;
import com.ssu.sangcholi.bookmarkservice.users.exception.NotFindUserException;
import com.ssu.sangcholi.bookmarkservice.users.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
                .isExactlyInstanceOf(NotFindUserException.class);
    }

    @Test
    @DisplayName("유효한 userId 이지만 Bookmark id가 없는 경우 북마크 조회 실패")
    public void findBookmarkFailBecauseOfBookmarkId() throws Exception {
        Assertions.assertThatThrownBy(() -> bookmarkService
                .getBookmarkByUserIdAndBookmarkId(mockUser.getUserId(), 100L))
                .isExactlyInstanceOf(NotFindBookmarkExcption.class);
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
        Assertions.assertThat(savedBookmark).extracting("original").isEqualTo(bookmarkDto.getOriginal());
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
                .isExactlyInstanceOf(NotFindUserException.class);
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
                .isExactlyInstanceOf(NotFindUserException.class);
    }


    @Test
    @DisplayName("북마크 모두 조회")
    public void findAllBookmark() throws Exception {
        Bookmark bookmark1 = Bookmark.builder().original("bookmark1").summarization("sum").build();
        Bookmark bookmark2 = Bookmark.builder().original("bookmark2").summarization("sum").build();
        Bookmark bookmark3 = Bookmark.builder().original("bookmark3").summarization("sum").build();
        em.persist(bookmark1);
        em.persist(bookmark2);
        em.persist(bookmark3);

        bookmark1.saveUser(mockUser);
        bookmark2.saveUser(mockUser);
        bookmark3.saveUser(mockUser);

        em.flush();
        em.clear();

        List<BookmarkDto> bookmarks = bookmarkService.getAllBookmarkByUserId(mockUser.getUserId());
        Assertions.assertThat(bookmarks).extracting("original")
                .contains(bookmark1.getOriginal(), bookmark2.getOriginal(), bookmark3.getOriginal());
    }

    @Test
    @DisplayName("없는 UserId로 북마크 모두 조회 실패")
    public void findAllBookmarkFailByUserId() throws Exception {
        Bookmark bookmark1 = Bookmark.builder().original("bookmark1").summarization("sum").build();
        Bookmark bookmark2 = Bookmark.builder().original("bookmark2").summarization("sum").build();
        Bookmark bookmark3 = Bookmark.builder().original("bookmark3").summarization("sum").build();
        em.persist(bookmark1);
        em.persist(bookmark2);
        em.persist(bookmark3);

        bookmark1.saveUser(mockUser);
        bookmark2.saveUser(mockUser);
        bookmark3.saveUser(mockUser);

        em.flush();
        em.clear();

        Assertions.assertThatThrownBy(() -> bookmarkService.getAllBookmarkByUserId("no one"))
                .isExactlyInstanceOf(NotFindUserException.class);

    }

    @Test
    @DisplayName("페이징 사용하여 북마크 모두 조회")
    public void findAllBookmarkUsingPaging() throws Exception {
        Bookmark bookmark1 = Bookmark.builder().original("bookmark1").summarization("sum").build();
        Bookmark bookmark2 = Bookmark.builder().original("bookmark2").summarization("sum").build();
        Bookmark bookmark3 = Bookmark.builder().original("bookmark3").summarization("sum").build();
        em.persist(bookmark1);
        em.persist(bookmark2);
        em.persist(bookmark3);

        bookmark1.saveUser(mockUser);
        bookmark2.saveUser(mockUser);
        bookmark3.saveUser(mockUser);

        PageRequest pageRequest = PageRequest.of(0, 2);
        Page<BookmarkDto> bookmarks = bookmarkService.getAllBookmarkByUserIdUsingPaging(mockUser.getUserId(), pageRequest);


        Assertions.assertThat(bookmarks).extracting("original")
                .contains(bookmark1.getOriginal(), bookmark2.getOriginal());
    }


}