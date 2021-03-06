package com.ssu.sangcholi.bookmarkservice.bookmark.service;

import com.ssu.sangcholi.bookmarkservice.bookmark.dto.BookmarkDto;
import com.ssu.sangcholi.bookmarkservice.bookmark.entity.Bookmark;
import com.ssu.sangcholi.bookmarkservice.bookmark.exception.NotFindBookmarkExcption;
import com.ssu.sangcholi.bookmarkservice.bookmark.repository.BookmarkRepository;
import com.ssu.sangcholi.bookmarkservice.users.entity.Users;
import com.ssu.sangcholi.bookmarkservice.users.exception.NotFindUserException;
import com.ssu.sangcholi.bookmarkservice.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkServiceImpl implements BookmarkService {
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<BookmarkDto> getAllBookmarkByUserId(String userId) {
        userRepository.findIdByUserId(userId).orElseThrow(NotFindUserException::new);
        return bookmarkRepository.findAllByUserId(userId).stream()
                .map(bookmark -> modelMapper.map(bookmark, BookmarkDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<BookmarkDto> getAllBookmarkByUserIdUsingPaging(String userId, Pageable page) {
        userRepository.findIdByUserId(userId).orElseThrow(NotFindUserException::new);
        Page<Bookmark> allByUserId = bookmarkRepository.findAllByUserId(userId, page);
        return allByUserId.map(bookmark -> modelMapper.map(bookmark, BookmarkDto.class));
    }

    @Override
    @Transactional
    public BookmarkDto addBookmark(String userId, BookmarkDto bookmarkDto) {
        Users user = userRepository.findByUserId(userId).orElseThrow(NotFindUserException::new);
        Bookmark bookmark = Bookmark.builder().original(bookmarkDto.getOriginal())
                .summarization(bookmarkDto.getSummarization())
                .build();
        bookmark.saveUser(user);
        Bookmark savedBookmark = bookmarkRepository.save(bookmark);
        return modelMapper.map(savedBookmark, BookmarkDto.class);
    }

    @Override
    @Transactional
    public void deleteBookmark(String userId, Long bookmarkId) {
        userRepository.findIdByUserId(userId).orElseThrow(NotFindUserException::new);
        Bookmark bookmark = bookmarkRepository.findByUserAndId(userId, bookmarkId).orElseThrow(NotFindBookmarkExcption::new);
        bookmarkRepository.delete(bookmark);
    }

    @Override
    public BookmarkDto getBookmarkByUserIdAndBookmarkId(String userId, Long BookmarkId) {
        userRepository.findIdByUserId(userId).orElseThrow(NotFindUserException::new);
        Bookmark bookmark = bookmarkRepository.findByUserAndId(userId, BookmarkId).orElseThrow(NotFindBookmarkExcption::new);
        return modelMapper.map(bookmark, BookmarkDto.class);
    }
}
