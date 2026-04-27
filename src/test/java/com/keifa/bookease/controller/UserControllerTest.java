package com.keifa.bookease.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keifa.bookease.common.handler.GlobalExceptionHandler;
import com.keifa.bookease.enums.Role;
import com.keifa.bookease.user.User;
import com.keifa.bookease.user.UserController;
import com.keifa.bookease.user.UserService;
import com.keifa.bookease.user.dto.request.UserUpdatePasswordRequestDto;
import com.keifa.bookease.user.dto.request.UserUpdateRequestDto;
import com.keifa.bookease.user.dto.response.CurrentUserResponseDto;
import com.keifa.bookease.user.dto.response.UserPublicResponseDto;
import com.keifa.bookease.user.dto.response.UserUpdateResponseDto;
import com.keifa.bookease.user.exception.UserNotFoundException;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private MockMvc mvc;

    private User user;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UserService service;

    @Mock
    UserDetails userDetails;

    @InjectMocks
    private UserController controller;


    @BeforeEach
    void setUp() {
        HandlerMethodArgumentResolver resolver = new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(@NonNull MethodParameter parameter) {
                return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
            }

            @Override
            public @Nullable Object resolveArgument(@NonNull MethodParameter parameter,
                                                    @Nullable ModelAndViewContainer mavContainer,
                                                    @NonNull NativeWebRequest webRequest,
                                                    @Nullable WebDataBinderFactory binderFactory) throws Exception {
                return userDetails;
            }
        };

        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .setCustomArgumentResolvers(resolver, new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new org.springframework.web.servlet.view.InternalResourceView(viewName))
                .build();

        user = User.UserBuilder.anUser()
                .withId(UUID.randomUUID())
                .withName("Light")
                .withEmail("keifa@dev")
                .withPassword("Senha123")
                .withPhone("(31) 97245-2995")
                .withRole(Role.CLIENT)
                .withActive(true)
                .build();
    }

    @Nested
    @DisplayName("GET /me")
    class GetCurrentUserTests {

        @Test
        @DisplayName("Should return current user when authenticated")
        void should_ReturnCurrentUser_When_Authenticated() throws Exception {
            CurrentUserResponseDto response = new CurrentUserResponseDto(UUID.randomUUID(), "Keifa", "keifa@dev",
                    "(31) 97245-2995", Role.CLIENT, LocalDateTime.now());

            when(userDetails.getUsername()).thenReturn("keifa@dev");
            when(service.getCurrentUser("keifa@dev")).thenReturn(response);

            mvc.perform(get("/api/v1/users/me"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Keifa"))
                    .andExpect(jsonPath("$.email").value("keifa@dev"));
        }

        @Test
        @DisplayName("should return 404 when not authenticated")
        void should_Return401_When_NotAuthenticated() throws Exception {
            when(userDetails.getUsername()).thenReturn("keifa@dev");
            when(service.getCurrentUser("keifa@dev"))
                    .thenThrow(new UserNotFoundException("User not found"));

            mvc.perform(get("/api/v1/users/me"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("PATCH/me")
    class PatchCurrentUserTests {
        @Test
        @DisplayName("Should update user when request is valid")
        void should_UpdateUser_When_RequestIsValid() throws Exception {
            UserUpdateRequestDto request = new UserUpdateRequestDto("Keifa", "keifa@diva",
                    "(31) 97245-2995");

            UserUpdateResponseDto response = new UserUpdateResponseDto(request.name(),
                    request.email(),
                    request.phone());

            when(userDetails.getUsername()).thenReturn("keifa@diva");

            String username = userDetails.getUsername();

            when(service.updateUser(username, request)).thenReturn(response);

            mvc.perform(patch("/api/v1/users/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Keifa"))
                    .andExpect(jsonPath("$.email").value("keifa@diva"));
        }

    }

    @Nested
    @DisplayName("PATCH /me/password")
    class PatchPasswordTests {
        @Test
        @DisplayName("Should update password when request is valid")
        void should_UpdatePassword_When_RequestIsValid() throws Exception {
            UserUpdatePasswordRequestDto request = new UserUpdatePasswordRequestDto("oldPassword", "newPassword123@", "newPassword123@");

            when(userDetails.getUsername()).thenReturn("keifa@dev");

            mvc.perform(patch("/api/v1/users/me/password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNoContent());

            verify(service).updatePassword(eq("keifa@dev"), any(UserUpdatePasswordRequestDto.class));
        }
    }

    @Nested
    @DisplayName("GET /{id}")
    class GetPublicInfoTests {
        @Test
        @DisplayName("Should return public info when user exists")
        void should_ReturnPublicInfo_When_UserExists() throws Exception {
            UUID id = UUID.randomUUID();
            UserPublicResponseDto response = new UserPublicResponseDto(id, "Keifa", Role.CLIENT);

            when(service.getUserPublicInfo(id)).thenReturn(response);

            mvc.perform(get("/api/v1/users/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name").value("Keifa"))
                    .andExpect(jsonPath("$.role").value("CLIENT"));
        }
    }

    @Nested
    @DisplayName("GET /")
    class GetAllUsersTests {
    }

    @Nested
    @DisplayName("PATCH /{id}/deactivate")
    class DeactivateUserTests {
        @Test
        @DisplayName("Should deactivate user")
        void should_DeactivateUser() throws Exception {
            UUID id = UUID.randomUUID();

            mvc.perform(patch("/api/v1/users/{id}/deactivate", id))
                    .andExpect(status().isNoContent());

            verify(service).deactivateUser(id);
        }
    }

    @Nested
    @DisplayName("PATCH /{id}/activate")
    class ActivateUserTests {
        @Test
        @DisplayName("Should activate user")
        void should_ActivateUser() throws Exception {
            UUID id = UUID.randomUUID();

            mvc.perform(patch("/api/v1/users/{id}/activate", id))
                    .andExpect(status().isNoContent());

            verify(service).activateUser(id);
        }
    }
}