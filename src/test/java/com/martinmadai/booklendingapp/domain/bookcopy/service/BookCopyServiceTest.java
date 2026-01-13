package com.martinmadai.booklendingapp.domain.bookcopy.service;

import com.martinmadai.booklendingapp.domain.book.model.Book;
import com.martinmadai.booklendingapp.domain.book.service.BookService;
import com.martinmadai.booklendingapp.domain.bookcondition.model.BookCondition;
import com.martinmadai.booklendingapp.domain.bookcondition.service.BookConditionService;
import com.martinmadai.booklendingapp.domain.bookcopy.dto.BookCopyListingDto;
import com.martinmadai.booklendingapp.domain.bookcopy.dto.CreateUpdateBookCopyDto;
import com.martinmadai.booklendingapp.domain.bookcopy.dto.SearchBookCopyDto;
import com.martinmadai.booklendingapp.domain.bookcopy.exception.BookCopyAlreadyExistsException;
import com.martinmadai.booklendingapp.domain.bookcopy.exception.BookCopyNotFoundException;
import com.martinmadai.booklendingapp.domain.bookcopy.mapper.BookCopyListingMapper;
import com.martinmadai.booklendingapp.domain.bookcopy.model.BookCopy;
import com.martinmadai.booklendingapp.domain.bookcopy.repository.BookCopyRepository;
import com.martinmadai.booklendingapp.domain.bookstatus.enums.BookStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookCopyServiceTest {

    @Mock
    private BookCopyRepository bookCopyRepository;

    @Mock
    private BookCopyListingMapper bookCopyListingMapper;

    @Mock
    private BookService bookService;

    @Mock
    private BookConditionService bookConditionService;

    @InjectMocks
    private BookCopyService bookCopyService;

    @Test
    void findBookCopyById_existingCopy_returnsCopy() {
        BookCopy copy = new BookCopy();
        copy.setId(1L);

        when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(copy));

        BookCopy result = bookCopyService.findBookCopyById(1L);

        assertEquals(copy, result);
    }

    @Test
    void findBookCopyById_notFound_throwsException() {
        when(bookCopyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookCopyNotFoundException.class, () -> bookCopyService.findBookCopyById(1L));
    }

    @Test
    void updateBookCopy_savesEntity() {
        BookCopy copy = new BookCopy();

        bookCopyService.updateBookCopy(copy);

        verify(bookCopyRepository).save(copy);
    }

    @Test
    void listCopies_returnsMappedDtos() {
        BookCopy copy = new BookCopy();
        List<BookCopy> copies = List.of(copy);
        List<BookCopyListingDto> dtos = List.of(new BookCopyListingDto());

        when(bookCopyRepository.findAll()).thenReturn(copies);
        when(bookCopyListingMapper.toDtoList(copies, Locale.ENGLISH)).thenReturn(dtos);

        List<BookCopyListingDto> result = bookCopyService.listCopies(Locale.ENGLISH);

        assertEquals(dtos, result);
    }

    @Test
    void createBookCopy_validData_savesCopy() {
        CreateUpdateBookCopyDto dto = validDto();

        Book book = new Book();
        BookCondition condition = new BookCondition();

        when(bookCopyRepository.existsByBarcode(dto.getBarcode())).thenReturn(false);
        when(bookService.findBookById(dto.getBookId())).thenReturn(book);
        when(bookConditionService.findConditionById(dto.getConditionId())).thenReturn(condition);

        bookCopyService.createBookCopy(dto);

        verify(bookCopyRepository).save(any(BookCopy.class));
    }

    @Test
    void createBookCopy_duplicateBarcode_throwsException() {
        CreateUpdateBookCopyDto dto = validDto();

        when(bookCopyRepository.existsByBarcode(dto.getBarcode())).thenReturn(true);

        assertThrows(BookCopyAlreadyExistsException.class, () -> bookCopyService.createBookCopy(dto));
    }

    @Test
    void updateBookCopy_updatesAndSaves() {
        CreateUpdateBookCopyDto dto = validDto();
        BookCopy copy = new BookCopy();
        BookCondition condition = new BookCondition();

        when(bookCopyRepository.findById(1L)).thenReturn(Optional.of(copy));
        when(bookConditionService.findConditionById(dto.getConditionId())).thenReturn(condition);

        bookCopyService.updateBookCopy(1L, dto);

        verify(bookCopyRepository).save(copy);
        assertEquals(dto.getBarcode(), copy.getBarcode());
        assertEquals(dto.getNotes(), copy.getNotes());
    }

    @Test
    void searchCopies_withCriteria_callsRepository() {
        SearchBookCopyDto dto = new SearchBookCopyDto();
        dto.setTitle("Harry");
        dto.setBarcode("ABC");

        when(bookCopyRepository.findAll(any(Specification.class))).thenReturn(List.of(new BookCopy()));

        List<BookCopy> result = bookCopyService.searchCopies(dto);

        assertEquals(1, result.size());
        verify(bookCopyRepository).findAll(any(Specification.class));
    }

    @Test
    void searchCopies_noCriteria_throwsException() {
        SearchBookCopyDto dto = new SearchBookCopyDto();

        assertThrows(NoSuchElementException.class, () -> bookCopyService.searchCopies(dto));
    }

    private CreateUpdateBookCopyDto validDto() {
        CreateUpdateBookCopyDto dto = new CreateUpdateBookCopyDto();
        dto.setBookId(1L);
        dto.setConditionId(1L);
        dto.setBarcode("BC123");
        dto.setAcquisitionDate(LocalDate.now());
        dto.setStatus(BookStatus.AVAILABLE);
        dto.setNotes("Test copy");
        return dto;
    }
}
