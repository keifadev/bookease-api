package com.keifa.bookease.auth;

import com.keifa.bookease.auth.dto.LoginRequest;
import com.keifa.bookease.auth.dto.RegisterRequest;
import com.keifa.bookease.auth.dto.TokenResponse;
import com.keifa.bookease.common.security.JwtService;
import com.keifa.bookease.common.security.UserDetailsImpl;
import com.keifa.bookease.user.User;
import com.keifa.bookease.user.UserRepository;
import com.keifa.bookease.user.exception.InvalidPasswordException;
import com.keifa.bookease.user.exception.UserNotFoundException;
import com.keifa.bookease.user.mapper.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository repository, UserMapper mapper, JwtService jwtService, PasswordEncoder encoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }

    @Transactional
    public TokenResponse register(RegisterRequest request) {
        User user = mapper.toUser(request);

        user.setPassword(encoder.encode(request.password()));

        User saved = repository.save(user);

        UserDetailsImpl details = AuthService.toUserDetails(saved);

        String token = jwtService.generateToken(details);

        return new TokenResponse(token);
    }

    @Transactional
    public TokenResponse login(LoginRequest request) {
        User user = repository.findByEmail(request.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new InvalidPasswordException("Password is incorrect");
        }

        UserDetailsImpl details = AuthService.toUserDetails(user);

        String token = jwtService.generateToken(details);

        return new TokenResponse(token);
    }

    private static UserDetailsImpl toUserDetails(User user) {
        return new UserDetailsImpl(user);
    }
}
