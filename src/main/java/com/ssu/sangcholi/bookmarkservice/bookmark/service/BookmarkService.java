package com.ssu.sangcholi.bookmarkservice.bookmark.service;

import com.ssu.sangcholi.bookmarkservice.bookmark.dto.BookmarkDto;

import java.util.List;

public interface BookmarkService {

    List<BookmarkDto> getAllBookmarkByUserId(String userId);

    BookmarkDto addBookmark(String userId, BookmarkDto bookmarkDto);

    void deleteBookmark(String userId, Long bookmarkId);

    BookmarkDto getBookmarkByUserIdAndBookmarkId(String userId, Long BookmarkId);
}
