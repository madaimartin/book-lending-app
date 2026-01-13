package com.martinmadai.booklendingapp.domain.bookcopy.service;

import com.martinmadai.booklendingapp.common.utility.BookLendingUtils;
import com.martinmadai.booklendingapp.domain.book.model.Book;
import com.martinmadai.booklendingapp.domain.book.service.BookService;
import com.martinmadai.booklendingapp.domain.bookcondition.model.BookCondition;
import com.martinmadai.booklendingapp.domain.bookcondition.service.BookConditionService;
import com.martinmadai.booklendingapp.domain.bookcopy.dto.BookCopyListingDto;
import com.martinmadai.booklendingapp.domain.bookcopy.dto.CreateUpdateBookCopyDto;
import com.martinmadai.booklendingapp.domain.bookcopy.dto.SearchBookCopyDto;
import com.martinmadai.booklendingapp.domain.bookcopy.exception.BookCopyAlreadyExistsException;
import com.martinmadai.booklendingapp.domain.bookcopy.exception.BookCopyNotFoundException;
import com.martinmadai.booklendingapp.domain.bookcopy.exception.NoAvailableCopyException;
import com.martinmadai.booklendingapp.domain.bookcopy.mapper.BookCopyListingMapper;
import com.martinmadai.booklendingapp.domain.bookcopy.model.BookCopy;
import com.martinmadai.booklendingapp.domain.bookcopy.repository.BookCopyRepository;
import com.martinmadai.booklendingapp.domain.bookstatus.enums.BookStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class BookCopyService {

    private final BookCopyRepository bookCopyRepository;
    private final BookCopyListingMapper bookCopyListingMapper;
    private final BookService bookService;
    private final BookConditionService bookConditionService;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @Transactional(readOnly = true)
    public List<BookCopyListingDto> listCopies(Locale locale) {
        List<BookCopy> copies = bookCopyRepository.findAll();
        return bookCopyListingMapper.toDtoList(copies, locale);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void createBookCopy(CreateUpdateBookCopyDto dto) {
        if (bookCopyRepository.existsByBarcode(dto.getBarcode())) {
            throw new BookCopyAlreadyExistsException("copy.exists.barcode");
        }

        Book book = bookService.findBookById(dto.getBookId());
        BookCondition condition = bookConditionService.findConditionById(dto.getConditionId());
        BookCopy bookCopy = BookCopy.builder()
                .book(book)
                .barcode(dto.getBarcode())
                .acquisitionDate(dto.getAcquisitionDate())
                .condition(condition)
                .status(dto.getStatus())
                .notes(dto.getNotes())
                .build();

        bookCopyRepository.save(bookCopy);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void updateBookCopy(Long bookCopyId, CreateUpdateBookCopyDto dto) {
        BookCopy bookCopy = findBookCopyById(bookCopyId);
        BookCondition condition = bookConditionService.findConditionById(dto.getConditionId());

        bookCopy.setBarcode(dto.getBarcode());
        bookCopy.setAcquisitionDate(dto.getAcquisitionDate());
        bookCopy.setCondition(condition);
        bookCopy.setStatus(dto.getStatus());
        bookCopy.setNotes(dto.getNotes());

        bookCopyRepository.save(bookCopy);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Transactional(readOnly = true)
    public List<BookCopy> searchCopies(SearchBookCopyDto dto) {

        List<Specification<BookCopy>> specs = new ArrayList<>();

        if (BookLendingUtils.hasText(dto.getTitle())) {
            specs.add(BookCopySpecification.titleContains(dto.getTitle()));
        }
        if (BookLendingUtils.hasText(dto.getAuthor())) {
            specs.add(BookCopySpecification.authorContains(dto.getAuthor()));
        }
        if (BookLendingUtils.hasText(dto.getPublisher())) {
            specs.add(BookCopySpecification.publisherContains(dto.getPublisher()));
        }
        if (dto.getCategoryId() != null) {
            specs.add(BookCopySpecification.hasCategory(dto.getCategoryId()));
        }
        if (dto.getStatus() != null) {
            specs.add(BookCopySpecification.hasStatus(dto.getStatus()));
        }
        if (dto.getConditionId() != null) {
            specs.add(BookCopySpecification.hasCondition(dto.getConditionId()));
        }
        if (BookLendingUtils.hasText(dto.getBarcode())) {
            specs.add(BookCopySpecification.barcodeContains(dto.getBarcode()));
        }

        Specification<BookCopy> spec =
                specs.stream()
                        .reduce(Specification::and)
                        .orElseThrow();

        return bookCopyRepository.findAll(spec);
    }

    public boolean isAnyCopyAvailable(Book book) {
        return bookCopyRepository.countByBookAndStatus(book, BookStatus.AVAILABLE) > 0;
    }

    public BookCopy findAnyAvailableCopy(Book book) {
        return bookCopyRepository
                .findFirstByBookAndStatus(book, BookStatus.AVAILABLE)
                .orElseThrow(() -> new NoAvailableCopyException(book.getTitle()));
    }

    public BookCopy findBookCopyById(Long copyId) {
        return bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new BookCopyNotFoundException(copyId));
    }

    public void updateBookCopy(BookCopy bookCopy) {
        bookCopyRepository.save(bookCopy);
    }
}
