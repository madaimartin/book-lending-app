package com.martinmadai.booklendingapp.domain.bookcopy.exception;

public class BookCopyAlreadyExistsException extends RuntimeException {
    public BookCopyAlreadyExistsException(String messageKey) {
        super(messageKey);
    }
}
