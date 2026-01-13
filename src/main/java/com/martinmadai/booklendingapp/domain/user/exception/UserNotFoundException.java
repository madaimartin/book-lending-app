package com.martinmadai.booklendingapp.domain.user.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("user.not.found:" + userId);
    }

}
