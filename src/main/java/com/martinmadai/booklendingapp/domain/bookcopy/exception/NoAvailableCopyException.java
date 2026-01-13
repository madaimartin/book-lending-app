package com.martinmadai.booklendingapp.domain.bookcopy.exception;

public class NoAvailableCopyException extends RuntimeException {

    public NoAvailableCopyException(String title) {
        super("copy.no.available:" + title);
    }
}
