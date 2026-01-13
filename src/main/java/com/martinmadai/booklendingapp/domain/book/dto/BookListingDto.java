package com.martinmadai.booklendingapp.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookListingDto {
        private Long id;
        private String title;
        private String author;
        private String isbn;
        private String publisher;
        private int publishedYear;
        private String categoryNames;
}