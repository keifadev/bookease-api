package com.keifa.bookease.professional;

import com.keifa.bookease.common.util.OwnershipValidator;
import com.keifa.bookease.professional.dto.request.ProfessionalProfileRequest;
import com.keifa.bookease.professional.dto.request.ProfessionalProfileUpdateRequest;
import com.keifa.bookease.professional.dto.response.CurrentProfessionalProfileResponse;
import com.keifa.bookease.professional.dto.response.ProfessionalProfileResponse;
import com.keifa.bookease.professional.dto.response.PublicProfessionalProfilesResponse;
import com.keifa.bookease.professional.enums.Specialty;
import com.keifa.bookease.professional.exceptions.DuplicateProfessionalProfileException;
import com.keifa.bookease.professional.exceptions.ProfessionalProfileNotFoundException;
import com.keifa.bookease.professional.mapper.ProfessionalMapper;
import com.keifa.bookease.user.User;
import com.keifa.bookease.user.UserRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProfessionalProfileService {
    private final ProfessionalProfileRepository repository;
    private final ProfessionalMapper mapper;
    private final UserRepository  userRepository;

    public ProfessionalProfileService(ProfessionalProfileRepository repository, ProfessionalMapper mapper,
                                      UserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProfessionalProfileResponse createProfile(ProfessionalProfileRequest request,
                                                     UUID userId) {
        if (repository.existsByUserId(userId)) {
            throw new DuplicateProfessionalProfileException("Professional profile already exists");
        }

        User user = userRepository.getReferenceById(userId);

        ProfessionalProfile profile = mapper.toProfile(request);
        profile.setUser(user);

        ProfessionalProfile saved = repository.save(profile);

        return mapper.toResponseDto(saved);
    }

    public CurrentProfessionalProfileResponse getCurrentProfessionalProfile(UUID profileId) {
        ProfessionalProfile professionalProfile = getProfile(profileId);

        OwnershipValidator.validateOwnership(professionalProfile.getUser().getId(), profileId);

        return mapper.toCurrentDto(professionalProfile);
    }

    @Transactional
    public void updateProfessionalProfile(ProfessionalProfileUpdateRequest request, UUID profileId) {
        ProfessionalProfile professionalProfile = getProfile(profileId);

        OwnershipValidator.validateOwnership(professionalProfile.getUser().getId(), profileId);

        mapper.updateProfessionalProfileFromDto(request, professionalProfile);
    }

    public ProfessionalProfileResponse getProfessionalProfile(UUID profileId) {
        ProfessionalProfile professionalProfile = getProfile(profileId);

        return mapper.toResponseDto(professionalProfile);
    }

    public Page<PublicProfessionalProfilesResponse> getAllProfessionals(Pageable pageable, Specialty specialty) {
        if (specialty != null) {
            return repository.findProfessionalProfileBySpecialty(specialty, pageable).map(mapper::toPublicDto);
        }

        return repository.findAll(pageable).map(mapper::toPublicDto);
    }

    private ProfessionalProfile getProfile(UUID profileId) {
        return repository.findById(profileId)
                .orElseThrow(() -> new ProfessionalProfileNotFoundException("Professional profile not found"));
    }
}
