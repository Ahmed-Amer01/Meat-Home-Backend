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
    private Region region;

    private Integer stNumber;
    private String stName;
    private Integer buildingNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public enum Region {
        Cairo,
        Giza,
        Alexandria,
        Port_Said,
        Suez,
        Damietta,
        Dakahlia,
        Sharqia,
        Qalyubia,
        Kafr_El_Sheikh,
        Gharbia,
        Monufia,
        Beheira,
        Ismailia,
        Minya,
        Beni_Suef,
        Faiyum,
        Assiut,
        Sohag,
        Qena,
        Luxor,
        Aswan,
        Red_Sea,
        New_Valley,
        Matrouh,
        North_Sinai,
        South_Sinai
    }
}