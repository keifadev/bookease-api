package com.keifa.bookease.professional;

import com.keifa.bookease.common.security.ProfessionalOrAdmin;
import com.keifa.bookease.common.security.UserDetailsImpl;
import com.keifa.bookease.professional.dto.request.ProfessionalProfileRequest;
import com.keifa.bookease.professional.dto.request.ProfessionalProfileUpdateRequest;
import com.keifa.bookease.professional.dto.response.CurrentProfessionalProfileResponse;
import com.keifa.bookease.professional.dto.response.ProfessionalProfileResponse;
import com.keifa.bookease.professional.dto.response.PublicProfessionalProfilesResponse;
import com.keifa.bookease.professional.enums.Specialty;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/professionals")
public class ProfessionalProfileController {
    private final ProfessionalProfileService service;

    public ProfessionalProfileController(ProfessionalProfileService service) {
        this.service = service;
    }

    @ProfessionalOrAdmin
    @PostMapping("/profile")
    public ResponseEntity<ProfessionalProfileResponse> createProfile(@Valid @RequestBody ProfessionalProfileRequest request,
                                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProfessionalProfileResponse profile = service.createProfile(request, userDetails.getUserId());

        URI location = URI.create("/api/v1/professionals/profile/" + profile.id());

        return ResponseEntity.created(location).body(profile);
    }

    @ProfessionalOrAdmin
    @GetMapping("/profile/me")
    public ResponseEntity<CurrentProfessionalProfileResponse> getCurrentProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(service.getCurrentProfessionalProfile(userDetails.getProfileId()));
    }

    @ProfessionalOrAdmin
    @PatchMapping("/profile/me")
    public ResponseEntity<Void> updateProfile
            (@Valid @RequestBody ProfessionalProfileUpdateRequest request,
             @AuthenticationPrincipal UserDetailsImpl userDetails) {

        service.updateProfessionalProfile(request, userDetails.getProfileId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<ProfessionalProfileResponse> getProfessionalProfile(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getProfessionalProfile(id));
    }

    @GetMapping
    public ResponseEntity<Page<PublicProfessionalProfilesResponse>> getAllProfessionalProfiles(Pageable pageable,
                                                                                               @RequestParam(required = false) Specialty specialty) {
        return ResponseEntity.ok(service.getAllProfessionals(pageable, specialty));
    }
}
