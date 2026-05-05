package com.keifa.bookease.user.mapper;

import com.keifa.bookease.auth.dto.RegisterRequest;
import com.keifa.bookease.user.User;
import com.keifa.bookease.user.dto.request.UserUpdateRequest;
import com.keifa.bookease.user.dto.response.AdminUserViewResponse;
import com.keifa.bookease.user.dto.response.CurrentUserResponse;
import com.keifa.bookease.user.dto.response.UserPublicResponse;
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
    void updateUserFromDto(UserUpdateRequest request, @MappingTarget User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toUser(RegisterRequest request);

    UserPublicResponse toPublicResponse(User user);

    CurrentUserResponse toCurrentUserResponse(User user);

    @Mapping(source = "id", target = "userId")
    AdminUserViewResponse toAdminUserView(User user);
}