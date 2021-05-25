package com.ssu.sangcholi.bookmarkservice.users.exception;

public class DuplicatedIdException extends RuntimeException {
    private static final String MESSAGE = "This ID is already exist";

    public DuplicatedIdException() {
        super(MESSAGE);
    }
}
