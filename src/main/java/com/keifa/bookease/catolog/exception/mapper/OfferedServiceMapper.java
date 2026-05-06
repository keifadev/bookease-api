package com.keifa.bookease.catolog.exception.mapper;

import com.keifa.bookease.catolog.OfferedService;
import com.keifa.bookease.catolog.dto.request.OfferedServiceRequest;
import com.keifa.bookease.catolog.dto.request.OfferedServiceUpdateRequest;
import com.keifa.bookease.catolog.dto.response.OfferedServiceResponse;
import com.keifa.bookease.catolog.dto.response.ProfessionalServiceResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OfferedServiceMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "professionalProfile", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OfferedService toOfferedService(OfferedServiceRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "professionalProfile", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updatedOfferedService(OfferedServiceUpdateRequest request, @MappingTarget OfferedService offeredService);

    OfferedServiceResponse toOfferedServiceResponse(OfferedService  offeredService);

    ProfessionalServiceResponse toProfessionalServiceResponse(OfferedService offeredService);
}
