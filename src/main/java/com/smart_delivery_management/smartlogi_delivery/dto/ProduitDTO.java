package com.smart_delivery_management.smartlogi_delivery.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProduitDTO {
    private String id;

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(max = 200)
    private String nom;

    @Size(max = 100)
    private String categorie;

    @DecimalMin(value = "0.0", message = "Le poids doit être positif")
    private BigDecimal poids;

    @DecimalMin(value = "0.0", message = "Le prix doit être positif")
    private BigDecimal prix;
}

