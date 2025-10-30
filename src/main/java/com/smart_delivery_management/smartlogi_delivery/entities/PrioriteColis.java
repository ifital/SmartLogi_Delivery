package com.smart_delivery_management.smartlogi_delivery.entities;

public enum PrioriteColis {
    NORMALE("Normale"),
    URGENTE("Urgente"),
    EXPRESS("Express");

    private final String libelle;

    PrioriteColis(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
