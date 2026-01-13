package com.martinmadai.booklendingapp.domain.book.mapper;

import com.martinmadai.booklendingapp.domain.book.dto.CreateUpdateBookDto;
import com.martinmadai.booklendingapp.domain.book.model.Book;
import com.martinmadai.booklendingapp.domain.bookcategory.model.BookCategory;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookFormMapper {

    public CreateUpdateBookDto fromEntity(Book book) {
        return CreateUpdateBookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher())
                .publishedYear(book.getPublishedYear())
                .categoryIds(
                        book.getCategories()
                                .stream()
                                .map(BookCategory::getId)
                                .collect(Collectors.toSet())
                )
                .build();
    }

    public CreateUpdateBookDto empty() {
        return CreateUpdateBookDto.builder()
                .title("")
                .author("")
                .isbn("")
                .publisher("")
                .publishedYear(null)
                .categoryIds(Set.of())
                .build();
    }
}