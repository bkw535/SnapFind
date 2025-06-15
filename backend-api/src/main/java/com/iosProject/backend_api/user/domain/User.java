package com.iosProject.backend_api.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String provider;
    private String providerId;
    private String email;
    private String name;

    @CreatedDate
    private LocalDateTime createdAt;

    public User(String provider, String providerId, String email, String name, LocalDateTime now) {
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.name = name;
        this.createdAt = now;
    }
}
