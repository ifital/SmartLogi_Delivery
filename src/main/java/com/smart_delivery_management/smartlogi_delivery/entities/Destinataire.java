package com.smart_delivery_management.smartlogi_delivery.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "destinataire")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Destinataire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, length = 100)
    private String prenom;

    @Column(length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String telephone;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String adresse;
}
