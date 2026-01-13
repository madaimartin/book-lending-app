package com.martinmadai.booklendingapp.domain.admin.contoller;

import com.martinmadai.booklendingapp.domain.user.dto.EditUserDto;
import com.martinmadai.booklendingapp.domain.user.mapper.EditUserDtoMapper;
import com.martinmadai.booklendingapp.domain.user.model.User;
import com.martinmadai.booklendingapp.domain.user.service.UserService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final EditUserDtoMapper editUserDtoMapper;

    @GetMapping
    public String adminHome() {
        return "admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/user-list";
    }

    @GetMapping("/users/{id}/edit")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("userForm", editUserDtoMapper.toDto(user));
        return "admin/user-edit";
    }

    @PostMapping("/users/{id}/edit")
    public String updateUser(
            @PathVariable Long id,
            @Valid @ModelAttribute("userForm") EditUserDto dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "admin/user-edit";
        }

        try {
            userService.updateUserByAdmin(id, dto);
            redirectAttributes.addFlashAttribute(
                    "flashMessage",
                    "user.update.success"
            );
            return "redirect:/admin/users";

        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute(
                    "flashError",
                    ex.getMessage()
            );
            return "redirect:/admin/users/" + id + "/edit";
        }
    }

}