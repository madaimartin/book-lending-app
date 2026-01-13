package com.martinmadai.booklendingapp.domain.book.exception;

public class BookAlreadyExistsException extends RuntimeException {
    public BookAlreadyExistsException(String messageKey) {
        super(messageKey);
    }
}
