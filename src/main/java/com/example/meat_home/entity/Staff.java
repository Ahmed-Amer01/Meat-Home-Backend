package com.example.meat_home.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Staff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder.Default
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Builder.Default
    @Column(name = "token_version")
    private Integer tokenVersion = 0;

    public enum Role {
        ADMIN,
        CALLCENTER,
        DELIVERY
    }
}