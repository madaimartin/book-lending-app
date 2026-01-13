package com.martinmadai.booklendingapp.domain.user.dto;

import com.martinmadai.booklendingapp.domain.user.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditUserDto {
    private Long id;

    @NotBlank(message = "{user.username.notBlank}")
    @Size(min = 3, max = 30, message = "{user.username.size}")
    private String username;

    @NotBlank(message = "{user.email.notBlank}")
    @Email(message = "{user.email.invalid}")
    private String email;

    private String phone;

    @NotNull
    private Role role;
}
