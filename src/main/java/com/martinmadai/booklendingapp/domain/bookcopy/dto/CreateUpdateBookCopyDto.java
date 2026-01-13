package com.martinmadai.booklendingapp.domain.bookcopy.dto;

import com.martinmadai.booklendingapp.common.validation.DateRange;
import com.martinmadai.booklendingapp.domain.bookstatus.enums.BookStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUpdateBookCopyDto {

        private Long id;

        @NotNull(message = "{copy.book.notNull}")
        private Long bookId;

        @NotBlank(message = "{copy.barcode.notBlank}")
        @Pattern(regexp = "\\d+", message = "{copy.barcode.numeric}")
        private String barcode;

        @NotNull(message = "{copy.acquisition.date.notNull}")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @DateRange(allowFuture = false, message = "{book.acquisition.date.invalid}")
        private LocalDate acquisitionDate;

        @NotNull(message = "{copy.condition.notNull}")
        private Long conditionId;

        @NotNull(message = "{copy.status.notNull}")
        private BookStatus status;

        private String notes;

        public static CreateUpdateBookCopyDto empty() {
                return CreateUpdateBookCopyDto.builder()
                        .id(null)
                        .bookId(null)
                        .barcode("")
                        .acquisitionDate(null)
                        .conditionId(null)
                        .status(null)
                        .notes("")
                        .build();
        }
}