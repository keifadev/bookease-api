package com.keifa.bookease.common.security;

import com.keifa.bookease.professional.ProfessionalProfile;
import com.keifa.bookease.professional.ProfessionalProfileRepository;
import com.keifa.bookease.user.User;
import com.keifa.bookease.user.UserRepository;
import com.keifa.bookease.user.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository repository;
    private final ProfessionalProfileRepository profileRepository;

    public CustomUserDetailsService(UserRepository repository, ProfessionalProfileRepository profileRepository) {
        this.repository = repository;
        this.profileRepository = profileRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return profileRepository.findProfessionalProfileByUserId(user.getId())
                .map(profile -> new UserDetailsImpl(user, profile))
                .orElse(new UserDetailsImpl(user));
    }
}
