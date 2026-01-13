package com.martinmadai.booklendingapp.domain.book.dto;

import com.martinmadai.booklendingapp.common.validation.NumericString;
import com.martinmadai.booklendingapp.common.validation.YearRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUpdateBookDto {

        private Long id;

        @NotBlank(message = "{book.title.notBlank}")
        private String title;

        @NotBlank(message = "{book.author.notBlank}")
        private String author;

        @NotBlank(message = "{book.isbn.notBlank}")
        @NumericString(message = "{book.isbn.numeric}")
        private String isbn;

        @NotBlank(message = "{book.publisher.notBlank}")
        private String publisher;

        @NotNull(message = "{book.publishedYear.notNull}")
        @YearRange(min = 1450, max = Integer.MAX_VALUE)
        private Integer publishedYear;

        @NotEmpty(message = "{book.category.notNull}")
        private Set<Long> categoryIds;

        public static CreateUpdateBookDto empty() {
                return CreateUpdateBookDto.builder()
                        .id(null)
                        .publishedYear(0)
                        .categoryIds(Set.of())
                        .build();
        }
}