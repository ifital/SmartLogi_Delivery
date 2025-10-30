package com.smart_delivery_management.smartlogi_delivery.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColisProduitId implements Serializable {
    @Column(name = "colis_id")
    private Long colisId;

    @Column(name = "produit_id")
    private Long produitId;
}