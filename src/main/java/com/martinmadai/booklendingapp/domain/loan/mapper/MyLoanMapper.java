package com.martinmadai.booklendingapp.domain.loan.mapper;

import com.martinmadai.booklendingapp.domain.book.model.Book;
import com.martinmadai.booklendingapp.domain.bookcopy.model.BookCopy;
import com.martinmadai.booklendingapp.domain.loan.dto.MyLoanDto;
import com.martinmadai.booklendingapp.domain.loan.model.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MyLoanMapper {
    public MyLoanDto toDto(Loan loan) {
        BookCopy copy = loan.getCopy();
        Book book = copy.getBook();

        return MyLoanDto.builder()
                .loanId(loan.getId())
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
