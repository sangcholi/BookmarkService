package com.ssu.sangcholi.bookmarkservice.bookmark.service;

import com.ssu.sangcholi.bookmarkservice.bookmark.dto.BookmarkDto;
import com.ssu.sangcholi.bookmarkservice.bookmark.entity.Bookmark;
import com.ssu.sangcholi.bookmarkservice.bookmark.exception.NotFindBookmark;
import com.ssu.sangcholi.bookmarkservice.bookmark.repository.BookmarkRepository;
import com.ssu.sangcholi.bookmarkservice.users.entity.Users;
import com.ssu.sangcholi.bookmarkservice.users.exception.NoFindUserException;
import com.ssu.sangcholi.bookmarkservice.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<BookmarkDto> getAllBookmarkByUserId(String userId) {
        return bookmarkRepository.findAll().stream()
                .map(bookmark -> modelMapper.map(bookmark, BookmarkDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public BookmarkDto addBookmark(String userId, BookmarkDto bookmarkDto) {
        Users user = userRepository.findByUserId(userId).orElseThrow(() -> new NoFindUserException(userId));
        Bookmark bookmark = Bookmark.builder().original(bookmarkDto.getOriginal())
                .summarization(bookmarkDto.getSummarization())
                .build();
        bookmark.saveUser(user);
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);
        return modelMapper.map(savedBookmark, BookmarkDto.class);
    }

    @Override
    public void deleteBookmark(String userId, Long bookmarkId) {
        Bookmark bookmark = bookmarkRepository.findByUserAndId(userId, bookmarkId).orElseThrow(NotFindBookmark::new);
        bookmarkRepository.delete(bookmark);
    }

    @Override
    public BookmarkDto getBookmarkByUserIdAndBookmarkId(String UserId, Long BookmarkId) {
        userRepository.findByUserId(UserId).orElseThrow(() -> new NoFindUserException(UserId));
        Bookmark bookmark = bookmarkRepository.findByUserAndId(UserId, BookmarkId).orElseThrow(NotFindBookmark::new);
        return modelMapper.map(bookmark, BookmarkDto.class);
    }
}
