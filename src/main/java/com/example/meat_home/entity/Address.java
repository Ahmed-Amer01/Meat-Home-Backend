package com.example.meat_home.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "Address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer apartmentNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegionEnum region;

    private Integer stNumber;
    private String stName;
    private Integer buildingNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
}