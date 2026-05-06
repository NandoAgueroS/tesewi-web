package com.tesewi.tesewi.auth.service;

import com.tesewi.tesewi.auth.dto.AuthResponse;
import com.tesewi.tesewi.auth.dto.LoginRequest;
import com.tesewi.tesewi.auth.dto.RegisterRequest;
import com.tesewi.tesewi.auth.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationConfiguration authenticationConfiguration;

    public AuthService(UserService userService, JwtService jwtService,
            AuthenticationConfiguration authenticationConfiguration) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered: " + request.getEmail());
        }

        User user = userService.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                request.getPhone(),
                request.getRole());

        UserDetails userDetails = loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, user.getEmail(), user.getName(), user.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        try {
            AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            User user = userService.findByEmail(request.getEmail());
            return new AuthResponse(token, user.getEmail(), user.getName(), user.getRole());
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .accountLocked(!user.isActive())
                .disabled(!user.isActive())
                .build();
    }
}
