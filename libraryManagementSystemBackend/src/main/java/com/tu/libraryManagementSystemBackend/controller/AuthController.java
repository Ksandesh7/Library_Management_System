package com.tu.libraryManagementSystemBackend.controller;

import com.tu.libraryManagementSystemBackend.dto.LoginRequest;
import com.tu.libraryManagementSystemBackend.dto.LoginResponse;
import com.tu.libraryManagementSystemBackend.dto.UserRequest;
import com.tu.libraryManagementSystemBackend.dto.UserResponse;
import com.tu.libraryManagementSystemBackend.model.User;
import com.tu.libraryManagementSystemBackend.service.JwtService;
import com.tu.libraryManagementSystemBackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRequest request) {
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        // Authenticating User
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // Generating JWT Token
        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(
                token,
                user.getId(),
                user.getRole()
        ));
    }
}
