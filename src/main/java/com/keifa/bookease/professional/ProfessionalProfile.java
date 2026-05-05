package com.keifa.bookease.professional;

import com.keifa.bookease.professional.enums.Specialty;
import com.keifa.bookease.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "professional_profiles")
@NoArgsConstructor
@Getter
@Setter
public class ProfessionalProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String bio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialty specialty;

    @Column(name = "cancellation_policy_hours",nullable = false)
    private Integer cancellationPolicyHours = 2;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
