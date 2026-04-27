package com.keifa.bookease.service;

import com.keifa.bookease.enums.Role;
import com.keifa.bookease.user.User;
import com.keifa.bookease.user.UserRepository;
import com.keifa.bookease.user.UserService;
import com.keifa.bookease.user.dto.request.UserUpdatePasswordRequestDto;
import com.keifa.bookease.user.dto.request.UserUpdateRequestDto;
import com.keifa.bookease.user.dto.response.AdminUserViewDTO;
import com.keifa.bookease.user.dto.response.CurrentUserResponseDto;
import com.keifa.bookease.user.dto.response.UserPublicResponseDto;
import com.keifa.bookease.user.dto.response.UserUpdateResponseDto;
import com.keifa.bookease.user.exception.*;
import com.keifa.bookease.user.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService service;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Feynman");
        user.setEmail("richardfeynman@gmail.com");
        user.setPassword(encoder.encode("Feynman123@!"));
        user.setPhone("31972452995");
        user.setRole(Role.CLIENT);
        user.setActive(true);
    }

    @Test
    @DisplayName("Should update user successfully when everything is OK")
    void updateUser_success() {
        UserUpdateRequestDto request = new UserUpdateRequestDto("Richard Feynman",
                "feynman@proton.me", "(31) 97245-2995");

        UserUpdateResponseDto responseDto = new UserUpdateResponseDto(
                request.name(),
                request.email(),
                request.phone()
        );

        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(repository.existsByEmail(request.email())).thenReturn(false);
        when(repository.save(user)).thenReturn(user);
        when(mapper.toResponseDto(user)).thenReturn(responseDto);

        doNothing().when(mapper).updateUserFromDto(request, user);

        UserUpdateResponseDto result = service.updateUser(user.getEmail(), request);

        verify(repository).findByEmail(user.getEmail());
        verify(repository).existsByEmail(request.email());
        verify(mapper).updateUserFromDto(request, user);
        verify(repository).save(user);
        verify(mapper).toResponseDto(user);

        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user does not exist")
    void updateUser_userNotFound() {
        UserUpdateRequestDto request = new UserUpdateRequestDto("Richard Feynman",
                "feynman@proton.me", "(31) 97245-2995");

        String email = request.email();

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateUser(email, request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with email " + email + " not found");

        verify(repository).findByEmail(email);
        verify(repository, never()).existsByEmail(any());
        verify(repository, never()).save(any());
        verify(mapper, never()).updateUserFromDto(any(), any());
        verify(mapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when email is already in use")
    void updateUser_emailAlreadyExists() {
        UserUpdateRequestDto request = new UserUpdateRequestDto("Richard Feynman",
                "feynman@proton.me", "(31) 97245-2995");

        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(repository.existsByEmail(request.email())).thenReturn(true);

        String email = user.getEmail();

        assertThatThrownBy(() -> service.updateUser(email, request))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("Email already in use " + request.email());

        verify(repository).findByEmail(user.getEmail());
        verify(repository).existsByEmail(request.email());
        verify(repository, never()).save(any());
        verify(mapper, never()).updateUserFromDto(any(), any());
        verify(mapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("Should update user when email is null")
    void updateUser_emailIsNull_shouldUpdateUser() {
        UserUpdateRequestDto request = new UserUpdateRequestDto("Richard Feynman",
                null, "(31) 97245-2995");

        UserUpdateResponseDto responseDto = new UserUpdateResponseDto(
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );

        String email = user.getEmail();

        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);
        when(mapper.toResponseDto(user)).thenReturn(responseDto);

        doNothing().when(mapper).updateUserFromDto(request, user);

        UserUpdateResponseDto result = service.updateUser(email, request);

        verify(repository, times(1)).findByEmail(email);
        verify(repository).save(user);
        verify(mapper).toResponseDto(user);
        assertThat(result).isEqualTo(responseDto);
    }

    @Test
    @DisplayName("Should update password successfully when everything is OK")
    void updatePassword_success() {
        UserUpdatePasswordRequestDto request = new UserUpdatePasswordRequestDto(
                user.getPassword(), "ImNotElliot1@!", "ImNotElliot1@!"
        );

        String email = user.getEmail();

        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        when(encoder.matches(request.password(), user.getPassword())).thenReturn(true);
        when(encoder.matches(request.newPassword(), user.getPassword())).thenReturn(false);
        when(encoder.encode(request.newPassword())).thenReturn("new-encoded-password");
        when(repository.save(user)).thenReturn(user);

        service.updatePassword(email, request);

        verify(repository, times(1)).findByEmail(email);
        verify(repository).save(user);
        assertThat(user.getPassword()).isEqualTo("new-encoded-password");
    }

    @Test
    @DisplayName("Should throw InvalidPasswordException when password is incorrect")
    void updatePassword_InvalidPassword() {
        UserUpdatePasswordRequestDto request = new UserUpdatePasswordRequestDto("ThraggTheRealEmperor1@#",
                "MrRobot3@silence", "MrRobot3@silence");

        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(encoder.matches(request.password(), user.getPassword())).thenReturn(false);

        String email = user.getEmail();

        verify(repository, never()).save(any());

        assertThatThrownBy(() -> service.updatePassword(email, request))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessage("Password is incorrect");
    }

    @Test
    @DisplayName("Should throw InvalidPasswordException when new password is the same as the old password")
    void updatePassword_InvalidPasswordOldPassword() {
        UserUpdatePasswordRequestDto request = new UserUpdatePasswordRequestDto("Feynman123@!",
                "Feynman123@!", "Feynman123@!");

        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(encoder.matches(request.password(), user.getPassword())).thenReturn(true);
        when(encoder.matches(request.newPassword(), user.getPassword())).thenReturn(true);

        String email = user.getEmail();

        verify(repository, never()).save(any());
        assertThatThrownBy(() -> service.updatePassword(email, request))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessage("New password cannot be the same as the old password");
    }

    @Test
    @DisplayName("Should throw PasswordMismatchException when new password and confirm password do not match")
    void updatePassword_MismatchPassword() {
        UserUpdatePasswordRequestDto request = new UserUpdatePasswordRequestDto("Feynman123@!",
                "Oppenheimer123@!", "Oppenheimer123@");

        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(encoder.matches(request.password(), user.getPassword())).thenReturn(true);
        when(encoder.matches(request.newPassword(), user.getPassword())).thenReturn(false);

        String email = user.getEmail();

        verify(repository, never()).save(any());
        assertThatThrownBy(() -> service.updatePassword(email, request))
                .isInstanceOf(PasswordMismatchException.class)
                .hasMessage("New password and confirm new password do not match");
    }

    @Test
    @DisplayName("Should deactivate user successfully")
    void deactivateUser_successful() {
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        when(repository.save(user)).thenReturn(user);

        service.deactivateUser(user.getId());

        verify(repository, times(1)).save(user);
        assertThat(user.isActive()).isNotEqualTo(true);
    }

    @Test
    @DisplayName("Should throw UserAlreadyInactiveException when user already inactive")
    void deactivateUser_UserAlreadyInactiveException() {
        User user1 = User.UserBuilder.anUser()
                .withId(UUID.randomUUID())
                .withName("Light")
                .withEmail("Light@gmail.com")
                .withPassword("Light01@!")
                .withPhone("319972452995")
                .withRole(Role.PROFESSIONAL)
                .withActive(false)
                .build();

        when(repository.findById(user1.getId())).thenReturn(Optional.of(user1));

        UUID userId = user1.getId();

        verify(repository, never()).save(any());
        assertThatThrownBy(() -> service.deactivateUser(userId))
                .isInstanceOf(UserAlreadyInactiveException.class)
                .hasMessage("This user is already inactive");
    }

    @Test
    @DisplayName("Should active user successfully")
    void activeUser_successful() {
        User user1 = User.UserBuilder.anUser()
                .withId(UUID.randomUUID())
                .withName("Light")
                .withEmail("Light@gmail.com")
                .withPassword("Light01@!")
                .withPhone("319972452995")
                .withRole(Role.PROFESSIONAL)
                .withActive(false)
                .build();

        when(repository.findById(user1.getId())).thenReturn(Optional.of(user1));
        when(repository.save(user1)).thenReturn(user1);

        service.activateUser(user1.getId());

        verify(repository, times(1)).save(user1);
        assertThat(user1.isActive()).isNotEqualTo(false);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user not found during activation")
    void activateUser_UserNotFound() {
        UUID userId = UUID.randomUUID();
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.activateUser(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with id " + userId + " not found");
    }

    @Test
    @DisplayName("Should throw UserAlreadyActiveException when user already active")
    void activateUser_UserAlreadyActiveException() {
        when(repository.findById(user.getId())).thenReturn(Optional.of(user));

        UUID userId = user.getId();

        assertThatThrownBy(() -> service.activateUser(userId))
                .isInstanceOf(UserAlreadyActiveException.class)
                .hasMessage("This user is already active");
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user not found during deactivation")
    void deactivateUser_UserNotFound() {
        UUID userId = UUID.randomUUID();
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deactivateUser(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with id " + userId + " not found");
    }

    @Test
    @DisplayName("Should return current user when everything is OK")
    void getCurrentUser_success() {
        CurrentUserResponseDto responseDto = new CurrentUserResponseDto(user.getId(), user.getName(), user.getEmail(),
                user.getPhone(), user.getRole(), java.time.LocalDateTime.now());

        when(repository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        when(mapper.toCurrentUserResponseDto(user)).thenReturn(responseDto);

        CurrentUserResponseDto result = service.getCurrentUser(user.getEmail());

        assertThat(result).isEqualTo(responseDto);
        verify(repository).findByEmail(user.getEmail());
    }

    @Test
    @DisplayName("Should return user public info when everything is OK")
    void getUserPublicInfo_success() {
        UserPublicResponseDto responseDto = new UserPublicResponseDto(user.getId(), user.getName(), user.getRole());

        when(repository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mapper.toPublicDto(user)).thenReturn(responseDto);

        UserPublicResponseDto result = service.getUserPublicInfo(user.getId());

        assertThat(result).isEqualTo(responseDto);
        verify(repository).findById(user.getId());
    }

    @Test
    @DisplayName("Should return all users paginated")
    void getAllUsers_success() {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        org.springframework.data.domain.Page<User> page = new org.springframework.data.domain.PageImpl<>(java.util.List.of(user));
        AdminUserViewDTO adminViewDto = new AdminUserViewDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone(), user.getRole(), user.isActive());

        when(repository.findAll(pageable)).thenReturn(page);
        when(mapper.toAdminUserViewDto(user)).thenReturn(adminViewDto);

        org.springframework.data.domain.Page<AdminUserViewDTO> result = service.getAllUsers(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(adminViewDto);
        verify(repository).findAll(pageable);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user not found during password update")
    void updatePassword_UserNotFound() {
        UserUpdatePasswordRequestDto request = new UserUpdatePasswordRequestDto("old", "new", "new");
        String email = "notfound@gmail.com";
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updatePassword(email, request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User with email " + email + " not found");
    }
}
