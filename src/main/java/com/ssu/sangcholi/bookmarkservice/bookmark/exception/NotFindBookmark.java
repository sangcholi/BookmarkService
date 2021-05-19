package com.ssu.sangcholi.bookmarkservice.bookmark.exception;

public class NotFindBookmark extends RuntimeException {
    private static final String MESSAGE = "Not Find bookmark";

    public NotFindBookmark() {
        super(MESSAGE);
    }
}
