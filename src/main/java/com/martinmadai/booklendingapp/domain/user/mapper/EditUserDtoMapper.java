package com.martinmadai.booklendingapp.domain.user.mapper;

import com.martinmadai.booklendingapp.domain.user.dto.EditUserDto;
import com.martinmadai.booklendingapp.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EditUserDtoMapper {

    public EditUserDto toDto(User user) {
        return EditUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .build();
    }
}
