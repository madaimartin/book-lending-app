package com.martinmadai.booklendingapp.domain.book.service;

import com.martinmadai.booklendingapp.domain.book.dto.BookListingDto;
import com.martinmadai.booklendingapp.domain.book.dto.CreateUpdateBookDto;
import com.martinmadai.booklendingapp.domain.book.dto.SearchBookDto;
import com.martinmadai.booklendingapp.domain.book.exception.BookAlreadyExistsException;
import com.martinmadai.booklendingapp.domain.book.exception.BookNotFoundException;
import com.martinmadai.booklendingapp.domain.book.mapper.BookListingMapper;
import com.martinmadai.booklendingapp.domain.book.model.Book;
import com.martinmadai.booklendingapp.domain.book.repository.BookRepository;
import com.martinmadai.booklendingapp.domain.bookcategory.exception.BookCategoryNotFoundException;
import com.martinmadai.booklendingapp.domain.bookcategory.model.BookCategory;
import com.martinmadai.booklendingapp.domain.bookcategory.repository.BookCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookCategoryRepository categoryRepository;
    private final BookListingMapper bookListingMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public void createBook(CreateUpdateBookDto dto) {
        if (bookRepository.existsByIsbn(dto.getIsbn())) {
            throw new BookAlreadyExistsException("book.exists.isbn");
        }

        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .publisher(dto.getPublisher())
                .publishedYear(dto.getPublishedYear())
                .build();

        saveCategories(dto, book);

        bookRepository.save(book);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional(readOnly = true)
    public List<BookListingDto> listBooks(Locale locale) {
        List<Book> books = bookRepository.findAll();
        return bookListingMapper.toDtoList(books, locale);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void updateBook(Long bookId, CreateUpdateBookDto dto) {
        Book book = findBookById(bookId);

        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPublisher(dto.getPublisher());
        book.setPublishedYear(dto.getPublishedYear());
        // Note: We cannot update the ISBN

        saveCategories(dto, book);

        bookRepository.save(book);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException(bookId);
        }

        bookRepository.deleteById(bookId);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional(readOnly = true)
    public Page<Book> searchBooks(
            SearchBookDto searchBookDto,
            Pageable pageable) {

        Specification<Book> spec = Specification.where((Specification<Book>) null);

        if (searchBookDto.getTitle() != null && !searchBookDto.getTitle().isBlank()) {
            spec = spec.and(BookSpecification.titleContains(searchBookDto.getTitle()));
        }

        if (searchBookDto.getAuthor() != null && !searchBookDto.getAuthor().isBlank()) {
            spec = spec.and(BookSpecification.authorContains(searchBookDto.getAuthor()));
        }

        if (searchBookDto.getPublisher() != null && !searchBookDto.getPublisher().isBlank()) {
            spec = spec.and(BookSpecification.publisherContains(searchBookDto.getPublisher()));
        }

        if (searchBookDto.getCategoryId() != null) {
            spec = spec.and(BookSpecification.hasCategory(searchBookDto.getCategoryId()));
        }

        return bookRepository.findAll(spec, pageable);
    }

    public Book findBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));
    }

    private Set<BookCategory> resolveCategories(Set<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            throw new BookCategoryNotFoundException("book.category.required");
        }

        List<BookCategory> categories = categoryRepository.findAllById(categoryIds);

        if (categories.size() != categoryIds.size()) {
            throw new BookCategoryNotFoundException("book.category.invalid");
        }

        return new HashSet<>(categories);
    }

    private void saveCategories(CreateUpdateBookDto dto, Book book) {
        Set<BookCategory> categories = resolveCategories(dto.getCategoryIds());
        if (book.getCategories() == null) {
            book.setCategories(new HashSet<>());
        } else {
            book.getCategories().clear();
        }
        book.getCategories().addAll(categories);
    }
}
