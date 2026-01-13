package com.martinmadai.booklendingapp.domain.loan.controller;

import com.martinmadai.booklendingapp.domain.loan.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/loans")
public class LoanController {

    private final LoanService loanService;

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String myLoans(Model model, Principal principal) {

        model.addAttribute("loans", loanService.findMyLoans(principal.getName()));

        return "lending/my-loans";
    }

    @GetMapping
    public String listAllLoans(Model model) {
        model.addAttribute("loans", loanService.findAllLoans());
        return "admin/loans/admin-loans";
    }

    @PostMapping("/{id}/return")
    public String returnLoan(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        loanService.returnLoan(id);
        redirectAttributes.addFlashAttribute("flashMessage", "loan.return.success");

        return "redirect:/loans";
    }

    @PostMapping("/{id}/extend")
    @PreAuthorize("isAuthenticated()")
    public String extendLoan(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes,
            Principal principal) {

        try {
            loanService.extendLoan(id, principal.getName());
            redirectAttributes.addFlashAttribute(
                    "flashMessage", "loan.extend.success");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute(
                    "flashError", ex.getMessage());
        }

        return "redirect:/loans/my";
    }
}
