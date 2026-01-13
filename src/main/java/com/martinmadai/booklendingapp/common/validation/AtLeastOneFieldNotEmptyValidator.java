package com.martinmadai.booklendingapp.common.validation;

import com.martinmadai.booklendingapp.common.utility.BookLendingUtils;
import com.martinmadai.booklendingapp.domain.bookcopy.dto.SearchBookCopyDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneFieldNotEmptyValidator
implements ConstraintValidator<AtLeastOneFieldNotEmpty, SearchBookCopyDto> {

    @Override
    public boolean isValid(SearchBookCopyDto dto, ConstraintValidatorContext context) {

        if (dto == null) {
            return false;
        }

        return BookLendingUtils.hasText(dto.getTitle())
                || BookLendingUtils.hasText(dto.getAuthor())
                || BookLendingUtils.hasText(dto.getPublisher())
                || BookLendingUtils.hasText(dto.getBarcode())
                || dto.getCategoryId() != null
                || dto.getConditionId() != null
                || dto.getStatus() != null;
    }
}