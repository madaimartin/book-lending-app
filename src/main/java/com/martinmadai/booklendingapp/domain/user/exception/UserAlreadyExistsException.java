package com.martinmadai.booklendingapp.domain.user.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {

    private final String messageKey;

    public UserAlreadyExistsException(String messageKey) {
        this.messageKey = messageKey;
    }

}
