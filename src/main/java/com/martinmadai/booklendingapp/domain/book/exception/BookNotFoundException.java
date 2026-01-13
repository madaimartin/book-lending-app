package com.martinmadai.booklendingapp.domain.book.exception;

public class BookNotFoundException extends RuntimeException{

    public BookNotFoundException(Long bookId) {
        super("book.notFound:" + bookId);
    }
}
