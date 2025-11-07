package com.smart_delivery_management.smartlogi_delivery.dto;

import com.smart_delivery_management.smartlogi_delivery.entity.enums.PrioriteColis;
import com.smart_delivery_management.smartlogi_delivery.entity.enums.StatutColis;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColisDTO {
    private String id;

    private String description;

    @NotNull(message = "Le poids est obligatoire")
    @DecimalMin(value = "0.01", message = "Le poids doit être supérieur à 0")
    private BigDecimal poids;

    @NotNull(message = "Le statut est obligatoire")
    private StatutColis statut;

    @NotNull(message = "La priorité est obligatoire")
    private PrioriteColis priorite;

    @NotBlank(message = "La ville de destination est obligatoire")
    @Size(max = 100)
    private String villeDestination;

    private LocalDateTime dateCreation;

    private String livreurId;
    private String livreurNom;

    @NotNull(message = "Le client expéditeur est obligatoire")
    private String clientExpediteurId;
    private String clientExpediteurNom;

    @NotNull(message = "Le destinataire est obligatoire")
    private String destinataireId;
    private String destinataireNom;

    @NotNull(message = "La zone est obligatoire")
    private String zoneId;
    private String zoneNom;

    private List<ColisProduitDTO> produits;
}
