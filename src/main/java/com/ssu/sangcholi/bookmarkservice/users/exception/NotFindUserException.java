package com.ssu.sangcholi.bookmarkservice.users.exception;

public class NotFindUserException extends RuntimeException {
    private static final String MESSAGE = "Not Exist UserId";

    public NotFindUserException() {
        super(MESSAGE);
    }
}
