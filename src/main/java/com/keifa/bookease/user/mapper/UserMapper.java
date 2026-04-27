package com.keifa.bookease.user.mapper;

import com.keifa.bookease.auth.dto.RegisterRequestDTO;
import com.keifa.bookease.user.User;
import com.keifa.bookease.user.dto.request.UserUpdateRequestDto;
import com.keifa.bookease.user.dto.response.AdminUserViewDTO;
import com.keifa.bookease.user.dto.response.CurrentUserResponseDto;
import com.keifa.bookease.user.dto.response.UserPublicResponseDto;
import com.keifa.bookease.user.dto.response.UserUpdateResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUserFromDto(UserUpdateRequestDto dto, @MappingTarget User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(RegisterRequestDTO dto);

    UserUpdateResponseDto toResponseDto(User user);

    UserPublicResponseDto toPublicDto(User user);

    CurrentUserResponseDto toCurrentUserResponseDto(User user);

    @Mapping(source = "id", target = "userId")
    AdminUserViewDTO toAdminUserViewDto(User user);
}