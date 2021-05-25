package com.ssu.sangcholi.bookmarkservice.error.controller;

import com.ssu.sangcholi.bookmarkservice.bookmark.exception.NotFindBookmarkExcption;
import com.ssu.sangcholi.bookmarkservice.error.vo.ErrorResponse;
import com.ssu.sangcholi.bookmarkservice.users.exception.DuplicatedIdException;
import com.ssu.sangcholi.bookmarkservice.users.exception.NotFindUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NotFindUserException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> noFindUserHandler(NotFindUserException exception) {
        return ResponseEntity.badRequest().body(ErrorResponse.builder().message(exception.getMessage()).build());
    }

    @ExceptionHandler(NotFindBookmarkExcption.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> notFindBookmark(NotFindBookmarkExcption exception) {
        return ResponseEntity.badRequest().body(ErrorResponse.builder().message(exception.getMessage()).build());
    }

    @ExceptionHandler(DuplicatedIdException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> duplicateUserId(DuplicatedIdException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.builder().message(exception.getMessage())
                .build());
    }
}
