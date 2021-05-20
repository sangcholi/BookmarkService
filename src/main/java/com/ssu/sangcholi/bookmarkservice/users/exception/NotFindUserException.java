package com.ssu.sangcholi.bookmarkservice.users.exception;

public class NoFindUserException extends RuntimeException {
    private static final String MESSAGE = " 찾을 수 없는 ID 입니다.";

    public NoFindUserException() {
        super(MESSAGE);
    }
}
