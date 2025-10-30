package com.smart_delivery_management.smartlogi_delivery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneDTO {
    private String id;

    @NotBlank(message = "Le nom de la zone est obligatoire")
    @Size(max = 100)
    private String nom;

    @NotBlank(message = "Le code postal est obligatoire")
    @Size(max = 10)
    private String codePostal;
}