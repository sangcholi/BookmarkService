package com.ssu.sangcholi.bookmarkservice.bookmark.exception;

public class NotFindBookmarkExcption extends RuntimeException {
    private static final String MESSAGE = "Not Find bookmark";

    public NotFindBookmarkExcption() {
        super(MESSAGE);
    }
}
