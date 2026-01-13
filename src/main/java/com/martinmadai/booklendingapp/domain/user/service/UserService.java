package com.martinmadai.booklendingapp.domain.user.service;

import com.martinmadai.booklendingapp.domain.user.dto.EditUserDto;
import com.martinmadai.booklendingapp.domain.user.dto.UserRegisterDto;
import com.martinmadai.booklendingapp.domain.user.enums.Role;
import com.martinmadai.booklendingapp.domain.user.exception.UserAlreadyExistsException;
import com.martinmadai.booklendingapp.domain.user.exception.UserNotFoundException;
import com.martinmadai.booklendingapp.domain.user.model.User;
import com.martinmadai.booklendingapp.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("user.not.found"));
    }

    @Transactional
    public void updateUserByAdmin(Long id, EditUserDto userDto) {
        User user = findById(id);

        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        user.setRole(userDto.getRole());

        userRepository.save(user);
    }

    public void registerUser(UserRegisterDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UserAlreadyExistsException("user.exists.username");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("user.exists.email");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .registrationDate(LocalDateTime.now())
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
    }


}
