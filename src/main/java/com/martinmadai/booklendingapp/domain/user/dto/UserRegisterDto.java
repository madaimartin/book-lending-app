package com.martinmadai.booklendingapp.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {

    @NotBlank(message = "{user.username.notBlank}")
    @Size(min = 3, max = 30, message = "{user.username.size}")
    private String username;

    @NotBlank(message = "{user.password.notBlank}")
    @Size(min = 6, message = "{user.password.size}")
    private String password;

    @NotBlank(message = "{user.email.notBlank}")
    @Email(message = "{user.email.invalid}")
    private String email;

    private String phone;
}
