package com.martinmadai.booklendingapp.domain.user.controller;

import com.martinmadai.booklendingapp.domain.user.dto.UserRegisterDto;
import com.martinmadai.booklendingapp.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute("userForm") UserRegisterDto userForm,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        userService.registerUser(userForm);
        return "redirect:/login?registered";
    }

    @ModelAttribute("userForm")
    public UserRegisterDto userForm() {
        return UserRegisterDto.builder().build();
    }
}
