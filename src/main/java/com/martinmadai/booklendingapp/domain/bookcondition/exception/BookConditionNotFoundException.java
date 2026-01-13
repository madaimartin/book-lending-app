package com.martinmadai.booklendingapp.domain.bookcondition.exception;

public class BookConditionNotFoundException extends RuntimeException{

    public BookConditionNotFoundException(Long bookConditionId) {
        super("bookcondition.notFound:" + bookConditionId);
    }
}
