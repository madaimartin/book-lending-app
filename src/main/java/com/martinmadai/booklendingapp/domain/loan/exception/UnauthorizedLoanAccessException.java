package com.martinmadai.booklendingapp.domain.loan.exception;

public class UnauthorizedLoanAccessException extends RuntimeException {

    public UnauthorizedLoanAccessException() {
        super("loan.access.denied");
    }
}
