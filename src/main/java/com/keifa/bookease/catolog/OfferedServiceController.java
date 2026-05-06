package com.keifa.bookease.catolog;

import com.keifa.bookease.catolog.dto.request.OfferedServiceRequest;
import com.keifa.bookease.catolog.dto.request.OfferedServiceUpdateRequest;
import com.keifa.bookease.catolog.dto.response.OfferedServiceResponse;
import com.keifa.bookease.catolog.dto.response.ProfessionalServiceResponse;
import com.keifa.bookease.common.security.ProfessionalOrAdmin;
import com.keifa.bookease.common.security.UserDetailsImpl;
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
public class OfferedServiceController {
    private final OfferedServiceManager manager;

    public OfferedServiceController(OfferedServiceManager manager) {
        this.manager = manager;
    }

    @ProfessionalOrAdmin
    @PostMapping("/me/services")
    public ResponseEntity<OfferedServiceResponse> createOfferedService(@Valid @RequestBody OfferedServiceRequest request,
                                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        OfferedServiceResponse offeredService = manager.createOfferedService(request, userDetails.getProfileId());

        URI location = URI.create("/api/v1/professionals/me/services/" + offeredService.id());

        return ResponseEntity.created(location).body(offeredService);
    }

    @ProfessionalOrAdmin
    @GetMapping("/me/services")
    public ResponseEntity<Page<OfferedServiceResponse>> getCurrentOfferedServices(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                                  Pageable pageable) {
        Page<OfferedServiceResponse> services = manager.getCurrentOfferedServices(userDetails.getProfileId(), pageable);

        return ResponseEntity.ok(services);
    }

    @ProfessionalOrAdmin
    @PatchMapping("/me/services/{id}")
    public ResponseEntity<Void> updatedOfferedService(@Valid @RequestBody OfferedServiceUpdateRequest request,
                                                      @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PathVariable UUID id) {

        manager.updatedOfferedService(id, userDetails.getProfileId(), request);

        return ResponseEntity.noContent().build();
    }

    @ProfessionalOrAdmin
    @DeleteMapping("/me/services/{id}")
    public ResponseEntity<Void> deleteOfferedService(@PathVariable UUID id,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        manager.deleteOfferedService(id, userDetails.getProfileId());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("{id}/services")
    public  ResponseEntity<Page<ProfessionalServiceResponse>> getOfferedServices(@PathVariable UUID id,
                                                                                 Pageable pageable) {
        Page<ProfessionalServiceResponse> services = manager.getOfferedServices(id, pageable);

        return ResponseEntity.ok(services);
    }
}
