package com.keifa.bookease.professional.mapper;

import com.keifa.bookease.professional.ProfessionalProfile;
import com.keifa.bookease.professional.dto.request.ProfessionalProfileRequest;
import com.keifa.bookease.professional.dto.request.ProfessionalProfileUpdateRequest;
import com.keifa.bookease.professional.dto.response.CurrentProfessionalProfileResponse;
import com.keifa.bookease.professional.dto.response.ProfessionalProfileResponse;
import com.keifa.bookease.professional.dto.response.PublicProfessionalProfilesResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProfessionalMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateProfessionalProfileFromDto(ProfessionalProfileUpdateRequest dto, @MappingTarget ProfessionalProfile professional);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ProfessionalProfile toProfile(ProfessionalProfileRequest dto);

    ProfessionalProfileResponse toResponseDto(ProfessionalProfile profile);

    CurrentProfessionalProfileResponse toCurrentDto(ProfessionalProfile profile);

    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "specialty", source = "specialty")
    PublicProfessionalProfilesResponse toPublicDto(ProfessionalProfile profile);
}
