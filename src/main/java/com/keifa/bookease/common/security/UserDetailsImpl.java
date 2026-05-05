package com.keifa.bookease.common.security;

import com.keifa.bookease.professional.ProfessionalProfile;
import com.keifa.bookease.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class UserDetailsImpl implements UserDetails {
    private final User user;
    private final UUID profileId;

    public UserDetailsImpl(User user) {
        this.user = user;
        this.profileId = null;
    }

    public UserDetailsImpl(User user, ProfessionalProfile profile) {
        this.user = user;
        this.profileId = profile != null ? profile.getId() : null;
    }

    public UUID getUserId() {
        return user.getId();
    }

    public UUID getProfileId() {
        return profileId;
    }

    public boolean isProfessional() {
        return profileId != null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}
