package com.martinmadai.booklendingapp.domain.loan.dto;

import com.martinmadai.booklendingapp.domain.loan.enums.LoanStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyLoanDto {

    private Long loanId;

    private String title;
    private String author;
    private String barcode;

    private LocalDateTime loanDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;

    private LoanStatus status;
}