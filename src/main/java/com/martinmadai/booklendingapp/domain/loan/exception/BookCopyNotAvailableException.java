package com.martinmadai.booklendingapp.domain.loan.exception;

import lombok.Getter;

@Getter
public class BookCopyNotAvailableException extends RuntimeException {

    private final String barcode;

    public BookCopyNotAvailableException(String barcode) {
        super("loan.copy.not.available:" + barcode);
        this.barcode = barcode;
    }
}
