package com.smart_delivery_management.smartlogi_delivery.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "colis_produit")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColisProduit {
    @EmbeddedId
    private ColisProduitId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("colisId")
    @JoinColumn(name = "colis_id")
    private Colis colis;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("produitId")
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @Column(nullable = false)
    private Integer quantite;

    @Column(precision = 10, scale = 2)
    private BigDecimal prix;

    @Column(name = "date_ajout")
    private LocalDateTime dateAjout;

    @PrePersist
    protected void onCreate() {
        dateAjout = LocalDateTime.now();
    }
}
