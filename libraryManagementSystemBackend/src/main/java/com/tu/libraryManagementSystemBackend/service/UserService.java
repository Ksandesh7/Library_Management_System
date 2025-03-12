package com.tu.libraryManagementSystemBackend.service;

import com.tu.libraryManagementSystemBackend.dto.UserRequest;
import com.tu.libraryManagementSystemBackend.dto.UserResponse;
import com.tu.libraryManagementSystemBackend.model.User;
import com.tu.libraryManagementSystemBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(UserRequest request) {
        if(userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
//                .password(passwordEncoder.encode(request.password()))
                .password(request.password())
                .role(request.role())
                .build();

        user = userRepository.save(user);
        return convertToUserResponse(user);
    }

    private UserResponse convertToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
