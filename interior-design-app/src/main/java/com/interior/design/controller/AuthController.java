package com.interior.design.controller;

import com.interior.design.domain.Role;
import com.interior.design.domain.User;
import com.interior.design.security.JwtTokenService;
import com.interior.design.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

record RegisterRequest(@Email String email, @NotBlank String password, @NotBlank String fullName, Role role) {}
record LoginRequest(@Email String email, @NotBlank String password) {}
record AuthResponse(String token, String role, String email, String fullName) {}

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        User user = userService.register(request.email(), request.password(), request.fullName(), request.role());
        String token = jwtTokenService.generateToken(user.getEmail(), user.getRole(), 60 * 60 * 6);
        return ResponseEntity.ok(new AuthResponse(token, user.getRole().name(), user.getEmail(), user.getFullName()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        User user = userService.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        String token = jwtTokenService.generateToken(user.getEmail(), user.getRole(), 60 * 60 * 6);
        return ResponseEntity.ok(new AuthResponse(token, user.getRole().name(), user.getEmail(), user.getFullName()));
    }
}

