package com.keifa.bookease.auth;

import com.keifa.bookease.auth.dto.LoginRequestDto;
import com.keifa.bookease.auth.dto.RegisterRequestDTO;
import com.keifa.bookease.auth.dto.TokenResponseDto;
import com.keifa.bookease.common.security.JwtService;
import com.keifa.bookease.common.security.UserDetailsImpl;
import com.keifa.bookease.user.User;
import com.keifa.bookease.user.UserRepository;
import com.keifa.bookease.user.exception.InvalidPasswordException;
import com.keifa.bookease.user.exception.PasswordMismatchException;
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
    public TokenResponseDto register(RegisterRequestDTO dto) {
        User user = mapper.toUser(dto);

        user.setPassword(encoder.encode(dto.password()));

        User saved = repository.save(user);

        UserDetailsImpl details = AuthService.toUserDetails(saved);

        String token = jwtService.generateToken(details);

        return new TokenResponseDto(token);
    }

    @Transactional
    public TokenResponseDto login(LoginRequestDto dto) {
        User user = repository.findByEmail(dto.email())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!encoder.matches(dto.password(), user.getPassword())) {
            throw new InvalidPasswordException("Password is incorrect");
        }

        UserDetailsImpl details = AuthService.toUserDetails(user);

        String token = jwtService.generateToken(details);

        return new TokenResponseDto(token);
    }

    private static UserDetailsImpl toUserDetails(User user) {
        return new UserDetailsImpl(user);
    }
}
