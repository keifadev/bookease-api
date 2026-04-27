package com.keifa.bookease.user;

import com.keifa.bookease.user.dto.request.UserUpdatePasswordRequestDto;
import com.keifa.bookease.user.dto.request.UserUpdateRequestDto;
import com.keifa.bookease.user.dto.response.AdminUserViewDTO;
import com.keifa.bookease.user.dto.response.CurrentUserResponseDto;
import com.keifa.bookease.user.dto.response.UserPublicResponseDto;
import com.keifa.bookease.user.dto.response.UserUpdateResponseDto;
import com.keifa.bookease.user.exception.*;
import com.keifa.bookease.user.mapper.UserMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, UserMapper mapper, PasswordEncoder encoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.encoder = encoder;
    }

    @Transactional
    public UserUpdateResponseDto updateUser(String email, UserUpdateRequestDto dto) {
        User user = repository.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        if (dto.email() != null && repository.existsByEmail(dto.email()) && !dto.email().equals(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use " + dto.email());
        }

        mapper.updateUserFromDto(dto, user);

        User saved = repository.save(user);

        return mapper.toResponseDto(saved);
    }

    @Transactional
    public void updatePassword(String email, UserUpdatePasswordRequestDto dto) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        if (!encoder.matches(dto.password(), user.getPassword())) {
            throw new InvalidPasswordException("Password is incorrect");
        }

        if (encoder.matches(dto.newPassword(), user.getPassword())) {
            throw new InvalidPasswordException("New password cannot be the same as the old password");
        }

        if (!dto.newPassword().equals(dto.confirmNewPassword())) {
            throw new PasswordMismatchException("New password and confirm new password do not match");
        }

        user.setPassword(encoder.encode(dto.newPassword()));

        repository.save(user);
    }

    public void deactivateUser(UUID userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        if (!user.isActive()) throw new UserAlreadyInactiveException("This user is already inactive");

        user.setActive(false);

        repository.save(user);
    }

    public void activateUser(UUID userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        if (user.isActive()) throw new UserAlreadyActiveException("This user is already active");

        user.setActive(true);

        repository.save(user);
    }

    public CurrentUserResponseDto getCurrentUser(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));

        return mapper.toCurrentUserResponseDto(user);
    }

    public UserPublicResponseDto getUserPublicInfo(UUID userId) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));

        return mapper.toPublicDto(user);
    }

    public Page<AdminUserViewDTO> getAllUsers(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toAdminUserViewDto);
    }
}
