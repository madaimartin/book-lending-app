package com.martinmadai.booklendingapp.domain.user.service;

import com.martinmadai.booklendingapp.domain.user.dto.EditUserDto;
import com.martinmadai.booklendingapp.domain.user.dto.UserRegisterDto;
import com.martinmadai.booklendingapp.domain.user.enums.Role;
import com.martinmadai.booklendingapp.domain.user.exception.UserAlreadyExistsException;
import com.martinmadai.booklendingapp.domain.user.exception.UserNotFoundException;
import com.martinmadai.booklendingapp.domain.user.model.User;
import com.martinmadai.booklendingapp.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void findAll_returnsAllUsers() {
        User user1 = new User();
        User user2 = new User();
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = userService.findAll();

        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void findById_existingId_returnsUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void findById_nonExistingId_throwsException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void findByUsername_existing_returnsUser() {
        User user = new User();
        user.setUsername("john");
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("john");
        assertEquals("john", result.getUsername());
    }

    @Test
    void findByUsername_nonExisting_throwsException() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername("john"));
    }

    @Test
    void updateUserByAdmin_updatesFieldsAndSaves() {
        User user = new User();
        user.setId(1L);
        user.setEmail("old@example.com");
        user.setPhone("123");
        user.setRole(Role.ROLE_USER);

        EditUserDto dto = EditUserDto.builder()
                .id(1L)
                .username("john")
                .email("john@example.com")
                .phone("123456")
                .role(Role.ROLE_ADMIN)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.updateUserByAdmin(1L, dto);

        assertEquals("john@example.com", user.getEmail());
        assertEquals("123456", user.getPhone());
        assertEquals(Role.ROLE_ADMIN, user.getRole());

        verify(userRepository).save(user);
    }

    @Test
    void registerUser_newUser_savesWithEncodedPassword() {
        UserRegisterDto dto = UserRegisterDto.builder()
                .username("john")
                .email("john@example.com")
                .phone("123456")
                .password("password1")
                .build();

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password1")).thenReturn("encoded-pass");

        userService.registerUser(dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();

        assertEquals("john", savedUser.getUsername());
        assertEquals("encoded-pass", savedUser.getPassword());
        assertEquals("john@example.com", savedUser.getEmail());
        assertEquals("123456", savedUser.getPhone());
        assertEquals(Role.ROLE_USER, savedUser.getRole());
        assertNotNull(savedUser.getRegistrationDate());
    }

    @Test
    void registerUser_existingUsername_throwsException() {
        UserRegisterDto dto = new UserRegisterDto();
        dto.setUsername("john");

        when(userRepository.existsByUsername("john")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(dto));
    }

    @Test
    void registerUser_existingEmail_throwsException() {
        UserRegisterDto dto = new UserRegisterDto();
        dto.setUsername("john");
        dto.setEmail("john@example.com");

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(dto));
    }
}
