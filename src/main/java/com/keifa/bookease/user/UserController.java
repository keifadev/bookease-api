package com.keifa.bookease.user;

import com.keifa.bookease.user.dto.request.UserUpdatePasswordRequest;
import com.keifa.bookease.user.dto.request.UserUpdateRequest;
import com.keifa.bookease.user.dto.response.AdminUserViewResponse;
import com.keifa.bookease.user.dto.response.CurrentUserResponse;
import com.keifa.bookease.user.dto.response.UserPublicResponse;
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
    public ResponseEntity<CurrentUserResponse> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        CurrentUserResponse currentUser = service.getCurrentUser(username);

        return ResponseEntity.ok(currentUser);
    }

    @PatchMapping("/me")
    public ResponseEntity<Void> updateUser(@Valid @RequestBody UserUpdateRequest request,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        service.updateUser(username, request);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> updateUserPassword(@Valid @RequestBody UserUpdatePasswordRequest request,
                                                   @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();

        service.updatePassword(username, request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPublicResponse> getPublicInfo(@PathVariable UUID id) {

        UserPublicResponse userPublicInfo = service.getUserPublicInfo(id);

        return ResponseEntity.ok(userPublicInfo);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdminUserViewResponse>> getAllUsers(Pageable pageable) {
        Page<AdminUserViewResponse> allUsers = service.getAllUsers(pageable);

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
