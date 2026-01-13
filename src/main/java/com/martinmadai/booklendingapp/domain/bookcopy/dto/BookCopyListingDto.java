package com.martinmadai.booklendingapp.domain.bookcopy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCopyListingDto {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private String barcode;
        private String condition;
        private String status;
}