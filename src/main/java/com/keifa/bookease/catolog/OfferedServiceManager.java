package com.keifa.bookease.catolog;

import com.keifa.bookease.catolog.dto.request.OfferedServiceRequest;
import com.keifa.bookease.catolog.dto.request.OfferedServiceUpdateRequest;
import com.keifa.bookease.catolog.dto.response.OfferedServiceResponse;
import com.keifa.bookease.catolog.dto.response.ProfessionalServiceResponse;
import com.keifa.bookease.catolog.exception.InvalidServiceDurationException;
import com.keifa.bookease.catolog.exception.ServiceNotActiveException;
import com.keifa.bookease.catolog.exception.mapper.OfferedServiceMapper;
import com.keifa.bookease.common.util.OwnershipValidator;
import com.keifa.bookease.professional.ProfessionalProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OfferedServiceManager {
    private final OfferedServiceRepository repository;
    private final OfferedServiceMapper mapper;
    private final ProfessionalProfileRepository profileRepository;

    public OfferedServiceManager(OfferedServiceRepository repository, OfferedServiceMapper mapper,
                                 ProfessionalProfileRepository profileRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.profileRepository = profileRepository;
    }

    @Transactional
    public OfferedServiceResponse createOfferedService(OfferedServiceRequest request, UUID profileId) {
        if (request.durationMinutes() % 15 != 0) {
            throw new InvalidServiceDurationException("The duration must be a multiple of 15 minutes");
        }

        OfferedService offeredService = mapper.toOfferedService(request);

        offeredService.setProfessionalProfile(profileRepository.getReferenceById(profileId));

        OfferedService saved = repository.save(offeredService);

        return mapper.toOfferedServiceResponse(saved);
    }

    public Page<OfferedServiceResponse> getCurrentOfferedServices(UUID profileId, Pageable pageable) {
        return repository.findOfferedServicesByProfessionalProfileId(profileId, pageable)
                .map(mapper::toOfferedServiceResponse);
    }

    @Transactional
    public void updatedOfferedService(UUID serviceId, UUID profileId, OfferedServiceUpdateRequest request) {
        OfferedService offeredService = repository.findByIdAndProfessionalProfileId(serviceId, profileId)
                .orElseThrow(ServiceNotActiveException::new);

        mapper.updatedOfferedService(request, offeredService);

        repository.save(offeredService);
    }

    public void deleteOfferedService(UUID id, UUID profileId) {
        OfferedService offeredService = repository.findByIdAndProfessionalProfileId(id, profileId)
                .orElseThrow(ServiceNotActiveException::new);

        repository.delete(offeredService);
    }

    public Page<ProfessionalServiceResponse> getOfferedServices(UUID profileId, Pageable pageable) {
        Page<OfferedService> services = repository.findOfferedServicesByProfessionalProfileId(profileId, pageable);

        return services.map(mapper::toProfessionalServiceResponse);
    }
}
