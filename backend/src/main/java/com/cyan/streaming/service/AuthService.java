package com.cyan.streaming.service;

import com.cyan.streaming.dto.AuthResponse;
import com.cyan.streaming.dto.LoginRequest;
import com.cyan.streaming.dto.RegisterRequest;
import com.cyan.streaming.exception.ConflictException;
import com.cyan.streaming.model.Role;
import com.cyan.streaming.model.User;
import com.cyan.streaming.repository.UserRepository;
import com.cyan.streaming.security.JwtService;
import com.cyan.streaming.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ConflictException("Username is already taken");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new ConflictException("Email is already registered");
        }

        User user = userRepository.save(User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build());

        String token = jwtService.generateToken(new UserPrincipal(user));
        return toAuthResponse(user, token);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsernameOrEmail(request.identifier(), request.identifier())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                request.password()
        ));

        String token = jwtService.generateToken(new UserPrincipal(user));
        return toAuthResponse(user, token);
    }

    private AuthResponse toAuthResponse(User user, String token) {
        return new AuthResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}
