package com.smart_delivery_management.smartlogi_delivery.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "historique_livraison", indexes = {
        @Index(name = "idx_historique_colis", columnList = "colis_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueLivraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colis_id", nullable = false)
    private Colis colis;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatutColis statut;

    @Column(name = "date_changement", nullable = false)
    private LocalDateTime dateChangement;

    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @PrePersist
    protected void onCreate() {
        dateChangement = LocalDateTime.now();
    }
}