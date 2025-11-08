package com.smart_delivery_management.smartlogi_delivery.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.StatutColis;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "historique_livraison",
        indexes = {
                @Index(name = "idx_historique_colis", columnList = "colis_id")}
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueLivraison {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false, unique = true, length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colis_id", nullable = false)
    @JsonManagedReference
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
