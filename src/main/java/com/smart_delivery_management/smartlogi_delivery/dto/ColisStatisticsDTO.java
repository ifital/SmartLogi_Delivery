package com.smart_delivery_management.smartlogi_delivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColisStatisticsDTO {
    private String livreurId;
    private String livreurNom;
    private String zoneId;
    private String zoneNom;
    private Long nombreColis;
    private BigDecimal poidsTotal;
}
