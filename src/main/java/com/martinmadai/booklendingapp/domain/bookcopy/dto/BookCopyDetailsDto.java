package com.martinmadai.booklendingapp.domain.bookcopy.dto;

import com.martinmadai.booklendingapp.domain.book.model.Book;
import com.martinmadai.booklendingapp.domain.bookcategory.model.BookCategory;
import com.martinmadai.booklendingapp.domain.bookcopy.model.BookCopy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookCopyDetailsDto {

    private Long id;

    // Book data
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private Integer publishedYear;
    private List<String> categoryCodes;

    // Copy data
    private String barcode;
    private LocalDate acquisitionDate;
    private String conditionCode;
    private String status;
    private String notes;

    public static BookCopyDetailsDto fromEntity(BookCopy copy) {
        Book book = copy.getBook();
        return BookCopyDetailsDto.builder()
                .id(copy.getId())

                // Book details
                .title(copy.getBook().getTitle())
                .author(copy.getBook().getAuthor())
                .publisher(copy.getBook().getPublisher())
                .isbn(copy.getBook().getIsbn())
                .publishedYear(copy.getBook().getPublishedYear())
                .categoryCodes(
                        book.getCategories().stream()
                                .map(BookCategory::getCode)
                                .sorted()
                                .toList()
                )

                // Copy details
                .barcode(copy.getBarcode())
                .acquisitionDate(copy.getAcquisitionDate())
                .conditionCode(copy.getCondition().getCode())
                .status(copy.getStatus().name())
                .notes(copy.getNotes())

                .build();
    }
}