package com.tu.libraryManagementSystemBackend.service;


import com.tu.libraryManagementSystemBackend.dto.UserRequest;
import com.tu.libraryManagementSystemBackend.dto.UserResponse;
import com.tu.libraryManagementSystemBackend.exception.ResourceNotFoundException;
import com.tu.libraryManagementSystemBackend.model.User;
import com.tu.libraryManagementSystemBackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;


import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void whenValidUser_thenSaveUser() {
        UserRequest request = new UserRequest("Steve", "Smith", "steve@gmail.com", "password", "PATRON");
        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .email("steve@gmail.com")
                .role("PATRON")
                .build();

        when(userRepository.existsByEmail("steve@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse response = userService.registerUser(request);

        assertEquals("steve@gmail.com", response.email());
        assertEquals("PATRON", response.role());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void whenDuplicateEmail_thenThrowException() {
        UserRequest request = new UserRequest("Steve", "Smith", "steve@gmail.com", "password", "PATRON");

        when(userRepository.existsByEmail("steve@gmail.com")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, ()->userService.registerUser(request));
    }

    @Test
    void whenValidId_thenReturnUser() {
        UUID userId = UUID.randomUUID();
        User user = User.builder().id(userId).email("test@library.com").build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(userId);
        assertEquals(userId, response.id());
    }

    @Test
    void whenInvalidId_thenThrowException() {
        UUID invalidId = UUID.randomUUID();
        when(userRepository.findById(invalidId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, ()->userService.getUserById(invalidId));
    }
}
