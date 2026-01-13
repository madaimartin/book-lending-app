package com.martinmadai.booklendingapp.domain.loan.mapper;

import com.martinmadai.booklendingapp.domain.book.model.Book;
import com.martinmadai.booklendingapp.domain.bookcopy.model.BookCopy;
import com.martinmadai.booklendingapp.domain.loan.dto.AdminLoanDto;
import com.martinmadai.booklendingapp.domain.loan.model.Loan;
import org.springframework.stereotype.Component;

@Component
public class AdminLoanMapper {

    public AdminLoanDto toDto(Loan loan) {
        BookCopy copy = loan.getCopy();
        Book book = copy.getBook();

        return AdminLoanDto.builder()
                .loanId(loan.getId())
                .username(loan.getUser().getUsername())
                .title(book.getTitle())
                .author(book.getAuthor())
                .barcode(copy.getBarcode())
                .loanDate(loan.getLoanDate())
                .dueDate(loan.getDueDate())
                .returnDate(loan.getReturnDate())
                .status(loan.getStatus())
                .build();
    }
}