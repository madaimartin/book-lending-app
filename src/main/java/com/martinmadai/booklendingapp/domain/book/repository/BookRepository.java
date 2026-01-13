package com.martinmadai.booklendingapp.domain.book.repository;

import com.martinmadai.booklendingapp.domain.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    boolean existsByIsbn(String isbn);

    Page<Book> findAll(Specification<Book> spec, Pageable pageable);
}
