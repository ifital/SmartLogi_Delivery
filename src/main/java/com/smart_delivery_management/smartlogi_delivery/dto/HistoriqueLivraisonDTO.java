package com.smart_delivery_management.smartlogi_delivery.dto;

import com.smart_delivery_management.smartlogi_delivery.entities.enums.StatutColis;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueLivraisonDTO {
    private Long id;

    @NotNull(message = "Le statut est obligatoire")
    private StatutColis statut;

    private LocalDateTime dateChangement;

    private String commentaire;
}
