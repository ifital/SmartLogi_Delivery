package com.smart_delivery_management.smartlogi_delivery.dto;

import com.smart_delivery_management.smartlogi_delivery.entity.enums.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private Role role;
}
