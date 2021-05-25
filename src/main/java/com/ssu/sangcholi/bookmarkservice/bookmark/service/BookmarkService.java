package com.ssu.sangcholi.bookmarkservice.bookmark.service;

import com.ssu.sangcholi.bookmarkservice.bookmark.dto.BookmarkDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookmarkService {

    List<BookmarkDto> getAllBookmarkByUserId(String userId);

    Page<BookmarkDto> getAllBookmarkByUserIdUsingPaging(String userId, Pageable page);

    BookmarkDto addBookmark(String userId, BookmarkDto bookmarkDto);

    void deleteBookmark(String userId, Long bookmarkId);

    BookmarkDto getBookmarkByUserIdAndBookmarkId(String userId, Long BookmarkId);
}
