package com.martinmadai.booklendingapp.domain.bookstatus.converter;

import com.martinmadai.booklendingapp.domain.bookstatus.enums.BookStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

@Converter(autoApply = true)
public class BookStatusConverter implements AttributeConverter<BookStatus, String> {

    @Override
    public String convertToDatabaseColumn(BookStatus status) {
        return status == null ? null : status.getCode();
    }

    @Override
    public BookStatus convertToEntityAttribute(String dbValue) {
        return Arrays.stream(BookStatus.values())
                .filter(s -> s.getCode().equals(dbValue))
                .findFirst()
                .orElseThrow();
    }
}