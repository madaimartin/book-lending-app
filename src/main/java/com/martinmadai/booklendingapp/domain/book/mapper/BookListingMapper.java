package com.martinmadai.booklendingapp.domain.book.mapper;

import com.martinmadai.booklendingapp.domain.book.dto.BookListingDto;
import com.martinmadai.booklendingapp.domain.book.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookListingMapper {

    private final MessageSource messageSource;

    public BookListingDto toDto(Book book, Locale locale) {
        String categoryNames = book.getCategories().stream()
                .map(c -> messageSource.getMessage("book.category." + c.getCode(), null, c.getCode(), locale))
                .sorted()
                .collect(Collectors.joining(", "));

        return BookListingDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher())
                .publishedYear(book.getPublishedYear())
                .categoryNames(categoryNames)
                .build();
    }

    public List<BookListingDto> toDtoList(List<Book> books, Locale locale) {
        return books.stream()
                .map(book -> toDto(book, locale))
                .collect(Collectors.toList());
    }
}