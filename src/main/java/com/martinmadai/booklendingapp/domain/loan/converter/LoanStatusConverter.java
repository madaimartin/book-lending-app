package com.martinmadai.booklendingapp.domain.loan.converter;

import com.martinmadai.booklendingapp.domain.loan.enums.LoanStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

@Converter(autoApply = true)
public class LoanStatusConverter implements AttributeConverter<LoanStatus, String> {

    @Override
    public String convertToDatabaseColumn(LoanStatus status) {
        return status == null ? null : status.getCode();
    }

    @Override
    public LoanStatus convertToEntityAttribute(String dbValue) {
        return Arrays.stream(LoanStatus.values())
                .filter(s -> s.getCode().equals(dbValue))
                .findFirst()
                .orElseThrow();
    }
}