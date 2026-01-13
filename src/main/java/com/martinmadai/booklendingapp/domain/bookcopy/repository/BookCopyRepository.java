package com.martinmadai.booklendingapp.domain.bookcopy.repository;

import com.martinmadai.booklendingapp.domain.book.model.Book;
import com.martinmadai.booklendingapp.domain.bookcopy.model.BookCopy;
import com.martinmadai.booklendingapp.domain.bookstatus.enums.BookStatus;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long>, JpaSpecificationExecutor<BookCopy> {

    long countByBookAndStatus(Book book, BookStatus status);

    Optional<BookCopy> findFirstByBookAndStatus(Book book, BookStatus bookStatus);

    boolean existsByBarcode(String barcode);
}
