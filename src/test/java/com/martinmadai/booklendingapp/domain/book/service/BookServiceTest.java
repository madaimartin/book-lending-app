package com.martinmadai.booklendingapp.domain.book.service;

import com.martinmadai.booklendingapp.domain.book.dto.BookListingDto;
import com.martinmadai.booklendingapp.domain.book.dto.CreateUpdateBookDto;
import com.martinmadai.booklendingapp.domain.book.exception.BookAlreadyExistsException;
import com.martinmadai.booklendingapp.domain.book.exception.BookNotFoundException;
import com.martinmadai.booklendingapp.domain.book.mapper.BookListingMapper;
import com.martinmadai.booklendingapp.domain.book.model.Book;
import com.martinmadai.booklendingapp.domain.book.repository.BookRepository;
import com.martinmadai.booklendingapp.domain.bookcategory.exception.BookCategoryNotFoundException;
import com.martinmadai.booklendingapp.domain.bookcategory.model.BookCategory;
import com.martinmadai.booklendingapp.domain.bookcategory.repository.BookCategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookCategoryRepository categoryRepository;

    @Mock
    private BookListingMapper bookListingMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void createBook_newBook_savesBook() {
        CreateUpdateBookDto dto = new CreateUpdateBookDto();
        dto.setTitle("Title");
        dto.setAuthor("Author");
        dto.setIsbn("123456");
        dto.setPublisher("Publisher");
        dto.setPublishedYear(2020);
        dto.setCategoryIds(Set.of(1L,2L));

        BookCategory cat1 = new BookCategory();
        cat1.setId(1L);
        BookCategory cat2 = new BookCategory();
        cat2.setId(2L);

        when(bookRepository.existsByIsbn("123456")).thenReturn(false);
        when(categoryRepository.findAllById(dto.getCategoryIds())).thenReturn(List.of(cat1, cat2));

        bookService.createBook(dto);

        ArgumentCaptor<Book> captor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(captor.capture());
        Book saved = captor.getValue();

        assertEquals("Title", saved.getTitle());
        assertEquals(2, saved.getCategories().size());
    }

    @Test
    void createBook_duplicateIsbn_throwsException() {
        CreateUpdateBookDto dto = new CreateUpdateBookDto();
        dto.setIsbn("123");

        when(bookRepository.existsByIsbn("123")).thenReturn(true);

        assertThrows(BookAlreadyExistsException.class, () -> bookService.createBook(dto));
    }

    @Test
    void createBook_invalidCategory_throwsException() {
        CreateUpdateBookDto dto = new CreateUpdateBookDto();
        dto.setTitle("Test Book");
        dto.setAuthor("Author");
        dto.setIsbn("123");
        dto.setPublisher("Publisher");
        dto.setPublishedYear(2020);
        dto.setCategoryIds(Set.of(1L));

        when(bookRepository.existsByIsbn("123")).thenReturn(false);
        when(categoryRepository.findAllById(Set.of(1L))).thenReturn(List.of());

        BookCategoryNotFoundException ex =
                assertThrows(BookCategoryNotFoundException.class,
                        () -> bookService.createBook(dto));

        assertEquals("book.category.invalid", ex.getMessage());
    }

    @Test
    void findBookById_existing_returnsBook() {
        Book book = new Book();
        book.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.findBookById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void findBookById_nonExisting_throwsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.findBookById(1L));
    }

    @Test
    void updateBook_existing_updatesAndSaves() {
        Book book = new Book();
        book.setId(1L);
        book.setCategories(new HashSet<>());

        CreateUpdateBookDto dto = new CreateUpdateBookDto();
        dto.setTitle("New Title");
        dto.setAuthor("New Author");
        dto.setPublisher("New Publisher");
        dto.setPublishedYear(2021);
        dto.setCategoryIds(Set.of(1L));

        BookCategory cat = new BookCategory();
        cat.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(categoryRepository.findAllById(dto.getCategoryIds())).thenReturn(List.of(cat));

        bookService.updateBook(1L, dto);

        assertEquals("New Title", book.getTitle());
        assertEquals("New Author", book.getAuthor());
        assertEquals(1, book.getCategories().size());
        verify(bookRepository).save(book);
    }

    @Test
    void deleteBook_existing_deletesBook() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.deleteBook(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_nonExisting_throwsException() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(1L));
    }

    @Test
    void listBooks_returnsDtoList() {
        Book book = new Book();
        List<Book> books = List.of(book);
        when(bookRepository.findAll()).thenReturn(books);

        BookListingDto dto = new BookListingDto();
        when(bookListingMapper.toDtoList(books, Locale.ENGLISH)).thenReturn(List.of(dto));

        List<BookListingDto> result = bookService.listBooks(Locale.ENGLISH);
        assertEquals(1, result.size());
        assertSame(dto, result.getFirst());
    }
}
