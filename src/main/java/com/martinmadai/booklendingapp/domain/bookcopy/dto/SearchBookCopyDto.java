package com.martinmadai.booklendingapp.domain.bookcopy.dto;

import com.martinmadai.booklendingapp.common.validation.AtLeastOneFieldNotEmpty;
import com.martinmadai.booklendingapp.domain.bookstatus.enums.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AtLeastOneFieldNotEmpty(message = "{validation.search.at.least.one.field.required}")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchBookCopyDto {

    private String title;
    private String author;
    private String publisher;
    private Long categoryId;
    private BookStatus status;
    private Long conditionId;
    private String barcode;

    public static SearchBookCopyDto empty() {
        return new SearchBookCopyDto();
    }
}