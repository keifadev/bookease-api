package com.keifa.bookease.user;

import com.keifa.bookease.user.dto.request.UserUpdatePasswordRequestDto;
import com.keifa.bookease.user.dto.request.UserUpdateRequestDto;
import com.keifa.bookease.user.dto.response.AdminUserViewDTO;
import com.keifa.bookease.user.dto.response.CurrentUserResponseDto;
import com.keifa.bookease.user.dto.response.UserPublicResponseDto;
import com.keifa.bookease.user.dto.response.UserUpdateResponseDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponseDto> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        CurrentUserResponseDto currentUser = service.getCurrentUser(username);

        return ResponseEntity.ok(currentUser);
    }

    @PatchMapping("/me")
    public ResponseEntity<UserUpdateResponseDto> updateUser(@Valid @RequestBody UserUpdateRequestDto dto,
                                                            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        UserUpdateResponseDto updated = service.updateUser(username, dto);

        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> updateUserPassword(@Valid @RequestBody UserUpdatePasswordRequestDto dto,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        service.updatePassword(username, dto);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPublicResponseDto> getPublicInfo(@PathVariable UUID id) {
        UserPublicResponseDto userPublicInfo = service.getUserPublicInfo(id);

        return ResponseEntity.ok(userPublicInfo);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdminUserViewDTO>> getAllUsers(Pageable pageable) {
        Page<AdminUserViewDTO> allUsers = service.getAllUsers(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id) {
        service.deactivateUser(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activateUser(@PathVariable UUID id) {
        service.activateUser(id);

        return ResponseEntity.noContent().build();
    }
}
