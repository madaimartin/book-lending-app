package com.martinmadai.booklendingapp.domain.loan.exception;

public class MaxActiveLoansExceededException extends RuntimeException {

    public MaxActiveLoansExceededException() {
        super("loan.max.loans.exceeded");
    }
}
