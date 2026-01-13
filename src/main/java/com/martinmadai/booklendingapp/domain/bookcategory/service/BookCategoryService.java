package com.martinmadai.booklendingapp.domain.bookcategory.service;

import com.martinmadai.booklendingapp.domain.book.model.Book;
import com.martinmadai.booklendingapp.domain.bookcategory.model.BookCategory;
import com.martinmadai.booklendingapp.domain.bookcategory.repository.BookCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCategoryService {

    private final BookCategoryRepository bookCategoryRepository;

    public List<Book> findBooksByCategory(Long categoryId) {
        return bookCategoryRepository.findBooksById(categoryId);
    }

    @Cacheable("bookCategories")
    public List<BookCategory> findAll() {
        return bookCategoryRepository.findAll();
    }
}
