package com.martinmadai.booklendingapp.domain.bookcategory.repository;

import com.martinmadai.booklendingapp.domain.book.model.Book;
import com.martinmadai.booklendingapp.domain.bookcategory.model.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
    List<Book> findBooksById(Long categoryId);
}
