package com.smart_delivery_management.smartlogi_delivery.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "destinataire")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Destinataire {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, unique = true, length = 36)
    private String id;

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
