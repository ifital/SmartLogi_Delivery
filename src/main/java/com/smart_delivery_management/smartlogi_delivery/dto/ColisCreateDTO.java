package com.smart_delivery_management.smartlogi_delivery.dto;

import com.smart_delivery_management.smartlogi_delivery.entity.enums.PrioriteColis;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColisCreateDTO {
    private String description;

    @NotNull(message = "Le poids est obligatoire")
    @DecimalMin(value = "0.01", message = "Le poids doit être supérieur à 0")
    private BigDecimal poids;

    @NotNull(message = "La priorité est obligatoire")
    private PrioriteColis priorite;

    @NotBlank(message = "La ville de destination est obligatoire")
    @Size(max = 100)
    private String villeDestination;

    @NotNull(message = "Le client expéditeur est obligatoire")
    private String clientExpediteurId;

    @NotNull(message = "Le destinataire est obligatoire")
    private String destinataireId;

    @NotNull(message = "La zone est obligatoire")
    private String zoneId;
}
