package com.martinmadai.booklendingapp.domain.loan.exception;

public class LoanNotFoundException extends RuntimeException {

    public LoanNotFoundException(Long loanId) {
        super("loan.not.found:" + loanId);
    }
}
