package com.keifa.bookease.professional;

import com.keifa.bookease.professional.enums.Specialty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfessionalProfileRepository extends JpaRepository<ProfessionalProfile, UUID> {
    Page<ProfessionalProfile> findProfessionalProfileBySpecialty(Specialty specialty, Pageable pageable);

    Optional<ProfessionalProfile> findProfessionalProfileByUserId(UUID userId);

    boolean existsByUserId(UUID userId);

    Optional<ProfessionalProfile> findByIdAndUserId(UUID id, UUID userId);
}
