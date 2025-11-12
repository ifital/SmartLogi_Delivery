package com.smart_delivery_management.smartlogi_delivery.entity.enums;

public enum StatutColis {
    CREE("Créé"),
    COLLECTE("Collecté"),
    LIVRE("Livré"),
    ANNULE("Annulé");

    private final String libelle;

    StatutColis(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
