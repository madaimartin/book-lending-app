package com.martinmadai.booklendingapp.domain.bookcategory.exception;

public class BookCategoryNotFoundException extends RuntimeException {

    public BookCategoryNotFoundException(Long categoryId) {
        super("book.category.notFound:" + categoryId);
    }

    public BookCategoryNotFoundException(String message) {
        super(message);
    }
}
