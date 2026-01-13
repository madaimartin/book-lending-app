package com.martinmadai.booklendingapp.domain.bookcopy.exception;

public class BookCopyNotFoundException extends RuntimeException {

    public BookCopyNotFoundException(Long copyId) {
        super("copy.not.found:" + copyId);
    }
}
