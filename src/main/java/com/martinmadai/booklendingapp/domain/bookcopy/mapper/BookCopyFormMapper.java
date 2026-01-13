package com.martinmadai.booklendingapp.domain.bookcopy.mapper;

import com.martinmadai.booklendingapp.domain.bookcopy.dto.CreateUpdateBookCopyDto;
import com.martinmadai.booklendingapp.domain.bookcopy.model.BookCopy;
import org.springframework.stereotype.Component;

@Component
public class BookCopyFormMapper {

    public CreateUpdateBookCopyDto fromEntity(BookCopy bookCopy) {
        return CreateUpdateBookCopyDto.builder()
                .id(bookCopy.getId())
                .bookId(bookCopy.getBook().getId())
                .barcode(bookCopy.getBarcode())
                .acquisitionDate(bookCopy.getAcquisitionDate())
                .conditionId(bookCopy.getCondition().getId())
                .status(bookCopy.getStatus())
                .notes(bookCopy.getNotes())
                .build();
    }
}