package com.martinmadai.booklendingapp.domain.book.model;

import com.martinmadai.booklendingapp.domain.bookcategory.model.BookCategory;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "categories")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String author;

    private String isbn;

    private String publisher;

    private int publishedYear;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "book_book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "book_category_id")
    )
    @Builder.Default
    private Set<BookCategory> categories = new HashSet<>();

    public String getCategoryNames(Book book) {
        return book.getCategories().stream()
                .map(BookCategory::getCode)
                .collect(Collectors.joining(", "));
    }
}
