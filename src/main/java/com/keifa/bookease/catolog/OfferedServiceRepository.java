package com.keifa.bookease.catolog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OfferedServiceRepository extends JpaRepository<OfferedService, UUID> {
    Page<OfferedService> findOfferedServicesByProfessionalProfileId(UUID professionalProfileId, Pageable pageable);

    Optional<OfferedService> findByIdAndProfessionalProfileId(UUID serviceId, UUID professionalProfileId);
}