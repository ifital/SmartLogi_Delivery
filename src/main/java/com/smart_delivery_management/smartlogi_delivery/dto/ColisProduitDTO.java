package com.smart_delivery_management.smartlogi_delivery.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColisProduitDTO {
    @NotNull(message = "L'identifiant du produit est obligatoire")
    private String produitId;

    private String produitNom;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être au moins 1")
    private int quantite;

    @DecimalMin(value = "0.0", message = "Le prix doit être positif")
    private BigDecimal prix;

    private LocalDateTime dateAjout;
}

