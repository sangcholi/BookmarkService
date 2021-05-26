package com.ssu.sangcholi.bookmarkservice.bookmark.controller;

import com.ssu.sangcholi.bookmarkservice.bookmark.dto.BookmarkDto;
import com.ssu.sangcholi.bookmarkservice.bookmark.service.BookmarkService;
import com.ssu.sangcholi.bookmarkservice.bookmark.vo.RequestBookmark;
import com.ssu.sangcholi.bookmarkservice.bookmark.vo.ResponseBookmark;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/bookmarks", produces = MediaTypes.HAL_JSON_VALUE)
public class BookmarkController {
    private final BookmarkService bookmarkService;
    private final ModelMapper modelMapper;

    @GetMapping("/{userId}")
    public ResponseEntity<Page<ResponseBookmark>> getAllBookmarkUsingPaging(@PathVariable String userId,
                                                                            Pageable pageable) {
        Page<BookmarkDto> bookmarks = bookmarkService.getAllBookmarkByUserIdUsingPaging(userId, pageable);
        Page<ResponseBookmark> responseBookmarks = bookmarks.map(
                bookmarkDto -> new ResponseBookmark(bookmarkDto));
        return ResponseEntity.ok(responseBookmarks);
    }

    @GetMapping("/{userId}/{bookmarkId}")
    public ResponseEntity<ResponseBookmark> getBookmark(@PathVariable String userId,
                                                        @PathVariable Long bookmarkId) {
        BookmarkDto findBookmark = bookmarkService.getBookmarkByUserIdAndBookmarkId(userId, bookmarkId);
        ResponseBookmark responseBookmark = new ResponseBookmark(findBookmark);
        return ResponseEntity.ok(responseBookmark);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ResponseBookmark> createBookmark(@PathVariable String userId,
                                                           @RequestBody @Valid RequestBookmark requestBookmark,
                                                           Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        BookmarkDto bookmarkDto = modelMapper.map(requestBookmark, BookmarkDto.class);
        BookmarkDto bookmark = bookmarkService.addBookmark(userId, bookmarkDto);
        ResponseBookmark body = new ResponseBookmark(bookmark);
        return ResponseEntity
                .created(linkTo(BookmarkController.class).slash(userId).slash(bookmark.getId()).toUri())
                .body(body);
    }

    @DeleteMapping("/{userId}/{bookmarkId}")
    public ResponseEntity deleteBookmark(@PathVariable String userId, @PathVariable Long bookmarkId) {
        bookmarkService.deleteBookmark(userId, bookmarkId);
        return ResponseEntity.ok().build();
    }
}
