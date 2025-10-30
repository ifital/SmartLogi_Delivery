package com.smart_delivery_management.smartlogi_delivery.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "colis", indexes = {
        @Index(name = "idx_colis_statut", columnList = "statut"),
        @Index(name = "idx_colis_priorite", columnList = "priorite"),
        @Index(name = "idx_colis_ville", columnList = "ville_destination")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Colis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal poids;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatutColis statut;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PrioriteColis priorite;

    @Column(name = "ville_destination", nullable = false, length = 100)
    private String villeDestination;

    @Column(name = "date_creation")
    private LocalDateTime dateCreation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id")
    private Livreur livreur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_expediteur_id", nullable = false)
    private ClientExpediteur clientExpediteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinataire_id", nullable = false)
    private Destinataire destinataire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @OneToMany(mappedBy = "colis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ColisProduit> produits = new ArrayList<>();

    @OneToMany(mappedBy = "colis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoriqueLivraison> historique = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }
}
