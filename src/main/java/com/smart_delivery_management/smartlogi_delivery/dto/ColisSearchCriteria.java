package com.smart_delivery_management.smartlogi_delivery.dto;

import com.smart_delivery_management.smartlogi_delivery.entities.enums.PrioriteColis;
import com.smart_delivery_management.smartlogi_delivery.entities.enums.StatutColis;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColisSearchCriteria {
    private String keyword;
    private StatutColis statut;
    private PrioriteColis priorite;
    private Long zoneId;
    private String ville;
    private Long livreurId;
    private Long clientExpediteurId;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
}
