package com.smart_delivery_management.smartlogi_delivery.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "zone")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Zone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(name = "code_postal", nullable = false, length = 10)
    private String codePostal;
}