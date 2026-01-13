package com.martinmadai.booklendingapp.domain.bookcopy.controller;

import com.martinmadai.booklendingapp.domain.book.service.BookService;
import com.martinmadai.booklendingapp.domain.bookcategory.service.BookCategoryService;
import com.martinmadai.booklendingapp.domain.bookcondition.service.BookConditionService;
import com.martinmadai.booklendingapp.domain.bookcopy.dto.BookCopyDetailsDto;
import com.martinmadai.booklendingapp.domain.bookcopy.dto.CreateUpdateBookCopyDto;
import com.martinmadai.booklendingapp.domain.bookcopy.dto.SearchBookCopyDto;
import com.martinmadai.booklendingapp.domain.bookcopy.mapper.BookCopyFormMapper;
import com.martinmadai.booklendingapp.domain.bookcopy.model.BookCopy;
import com.martinmadai.booklendingapp.domain.bookcopy.service.BookCopyService;
import com.martinmadai.booklendingapp.domain.bookstatus.enums.BookStatus;
import com.martinmadai.booklendingapp.domain.loan.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Locale;

@Controller
@RequestMapping("/bookcopies")
@RequiredArgsConstructor
public class BookCopyController {

    private final BookCopyService bookCopyService;
    private final BookService bookService;
    private final BookConditionService bookConditionService;
    private final BookCategoryService bookCategoryService;
    private final LoanService loanService;
    private final BookCopyFormMapper bookCopyFormMapper;
    private final MessageSource messageSource;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String listBookCopies(Model model, Locale locale) {
        model.addAttribute("copies", bookCopyService.listCopies(locale));
        return "admin/bookcopies/copy-list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
        model.addAttribute("copyForm", CreateUpdateBookCopyDto.empty());
        populateCreateUpdateDropdowns(model);
        return "admin/bookcopies/copy-edit";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute(
                "copyForm",
                bookCopyFormMapper.fromEntity(bookCopyService.findBookCopyById(id))
        );
        populateCreateUpdateDropdowns(model);
        return "admin/bookcopies/copy-edit";
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String saveBookCopy(
            @Valid @ModelAttribute("copyForm") CreateUpdateBookCopyDto dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            populateCreateUpdateDropdowns(model);
            return "admin/bookcopies/copy-edit";
        }

        if (dto.getId() == null) {
            bookCopyService.createBookCopy(dto);
        } else {
            bookCopyService.updateBookCopy(dto.getId(), dto);
        }

        return "redirect:/bookcopies";
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String searchForm(Model model) {
        model.addAttribute("searchForm", SearchBookCopyDto.empty());
        populateSearchDropdowns(model);
        return "lending/search-copies";
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String search(
            @Valid @ModelAttribute("searchForm") SearchBookCopyDto dto,
            BindingResult result,
            Model model) {

        populateSearchDropdowns(model);

        if (result.hasErrors()) {
            return "lending/search-copies";
        }

        model.addAttribute("results", bookCopyService.searchCopies(dto));
        return "lending/search-copies";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String viewCopyDetails(@PathVariable Long id, Model model) {
        BookCopy copy = bookCopyService.findBookCopyById(id);
        model.addAttribute("copy", BookCopyDetailsDto.fromEntity(copy));
        return "lending/copy-details";
    }

    @PostMapping("/{id}/loan")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Transactional
    public String loanCopy(
            @PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes,
            Locale locale) {

        loanService.loanBookCopy(id, principal.getName());

        redirectAttributes.addFlashAttribute(
                "successMessage",
                messageSource.getMessage("loan.success", null, locale)
        );

        return "redirect:/bookcopies/" + id;
    }

    private void populateCreateUpdateDropdowns(Model model) {
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("conditions", bookConditionService.findAll());
        model.addAttribute("statuses", BookStatus.values());
    }

    private void populateSearchDropdowns(Model model) {
        model.addAttribute("categories", bookCategoryService.findAll());
        model.addAttribute("conditions", bookConditionService.findAll());
        model.addAttribute("statuses", BookStatus.values());
    }
}
