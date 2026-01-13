package com.martinmadai.booklendingapp.domain.bookcopy.mapper;

import com.martinmadai.booklendingapp.domain.bookcondition.model.BookCondition;
import com.martinmadai.booklendingapp.domain.bookcopy.dto.BookCopyListingDto;
import com.martinmadai.booklendingapp.domain.bookcopy.model.BookCopy;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookCopyListingMapper {

    private final MessageSource messageSource;

    public BookCopyListingDto toDto(BookCopy bookCopy, Locale locale) {
        String condition =  getConditionText(bookCopy.getCondition(), locale);
        return BookCopyListingDto.builder()
                .id(bookCopy.getId())
                .title(bookCopy.getBook().getTitle())
                .author(bookCopy.getBook().getAuthor())
                .isbn(bookCopy.getBook().getIsbn())
                .barcode(bookCopy.getBarcode())
                .condition(condition)
                .status(bookCopy.getStatus().toString())
                .build();
    }

    public List<BookCopyListingDto> toDtoList(List<BookCopy> bookCopies, Locale locale) {
        return bookCopies.stream()
                .map(copy -> toDto(copy, locale))
                .collect(Collectors.toList());
    }

    public String getConditionText(BookCondition condition, Locale locale) {
        if (condition == null) {
            return Strings.EMPTY;
        }
        return messageSource.getMessage(
                "book.condition." + condition.getCode(),
                null,
                condition.getCode(),
                locale);
    }
}