package com.keifa.bookease.user;

import com.keifa.bookease.user.dto.request.UserUpdatePasswordRequest;
import com.keifa.bookease.user.dto.request.UserUpdateRequest;
import com.keifa.bookease.user.dto.response.AdminUserViewResponse;
import com.keifa.bookease.user.dto.response.CurrentUserResponse;
import com.keifa.bookease.user.dto.response.UserPublicResponse;
import com.keifa.bookease.user.exception.*;
import com.keifa.bookease.user.mapper.UserMapper;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
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
    public void updateUser(String email, UserUpdateRequest request) {
        User user = getUserByEmail(email);

        if (request.email() != null && repository.existsByEmail(request.email()) && !request.email().equals(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use " + request.email());
        }

        mapper.updateUserFromDto(request, user);

        repository.save(user);
    }

    @Transactional
    public void updatePassword(String email, UserUpdatePasswordRequest request) {
        User user = getUserByEmail(email);

        if (!encoder.matches(request.password(), user.getPassword())) {
            throw new InvalidPasswordException("Password is incorrect");
        }

        if (encoder.matches(request.newPassword(), user.getPassword())) {
            throw new InvalidPasswordException("New password cannot be the same as the old password");
        }

        if (!request.newPassword().equals(request.confirmNewPassword())) {
            throw new PasswordMismatchException("New password and confirm new password do not match");
        }

        user.setPassword(encoder.encode(request.newPassword()));

        repository.save(user);
    }

    public void deactivateUser(UUID userId) {
        User user = getUserById(userId);

        if (!user.isActive()) throw new UserAlreadyInactiveException("This user is already inactive");

        user.setActive(false);

        repository.save(user);
    }

    public void activateUser(UUID userId) {
        User user = getUserById(userId);

        if (user.isActive()) throw new UserAlreadyActiveException("This user is already active");

        user.setActive(true);

        repository.save(user);
    }

    public CurrentUserResponse getCurrentUser(String email) {
        User user = getUserByEmail(email);

        return mapper.toCurrentUserResponse(user);
    }

    public UserPublicResponse getUserPublicInfo(UUID userId) {
        User user = getUserById(userId);

        return mapper.toPublicResponse(user);
    }

    public Page<AdminUserViewResponse> getAllUsers(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toAdminUserView);
    }

    private @NonNull User getUserByEmail(String email) {
        return repository.findByEmail(email).
                orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }
    
    private @NonNull User getUserById(UUID userId) {
        return repository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
    }
}
