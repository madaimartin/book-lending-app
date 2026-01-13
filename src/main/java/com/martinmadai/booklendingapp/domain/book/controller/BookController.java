package com.martinmadai.booklendingapp.domain.book.controller;

import com.martinmadai.booklendingapp.domain.book.dto.CreateUpdateBookDto;
import com.martinmadai.booklendingapp.domain.book.mapper.BookFormMapper;
import com.martinmadai.booklendingapp.domain.book.service.BookService;
import com.martinmadai.booklendingapp.domain.bookcategory.model.BookCategory;
import com.martinmadai.booklendingapp.domain.bookcategory.service.BookCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookCategoryService categoryService;
    private final BookFormMapper bookFormMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String listBooks(Model model, Locale locale) {
        model.addAttribute("books", bookService.listBooks(locale));
        return "admin/book/book-list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("bookForm", CreateUpdateBookDto.empty());
        return "admin/book/book-edit";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute(
                "bookForm",
                bookFormMapper.fromEntity(bookService.findBookById(id))
        );
        return "admin/book/book-edit";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveBook(
            @Valid @ModelAttribute("bookForm") CreateUpdateBookDto dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "admin/book/book-edit";
        }

        if (dto.getId() == null) {
            bookService.createBook(dto);
        } else {
            bookService.updateBook(dto.getId(), dto);
        }

        return "redirect:/books";
    }

    @ModelAttribute("categories")
    public List<BookCategory> categories() {
        return categoryService.findAll();
    }
}
