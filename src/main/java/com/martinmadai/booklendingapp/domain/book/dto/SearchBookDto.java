package com.martinmadai.booklendingapp.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchBookDto {
    private String title;
    private String author;
    private String publisher;
    private Long categoryId;
}