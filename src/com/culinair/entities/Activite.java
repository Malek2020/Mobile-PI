package com.culinair.entities;

public class Activite {

    private int id;
    private String nom;
    private String typeActivite;
    private String description;
    private int temps;
    private int prixActivite;
    private int nombreParticipant;
    private Transport transport;

    public Activite(int id, String nom, String typeActivite, String description, int temps, int prixActivite, int nombreParticipant, Transport transport) {
        this.id = id;
        this.nom = nom;
        this.typeActivite = typeActivite;
        this.description = description;
        this.temps = temps;
        this.prixActivite = prixActivite;
        this.nombreParticipant = nombreParticipant;
        this.transport = transport;
    }

    public Activite(String nom, String typeActivite, String description, int temps, int prixActivite, int nombreParticipant, Transport transport) {
        this.nom = nom;
        this.typeActivite = typeActivite;
        this.description = description;
        this.temps = temps;
        this.prixActivite = prixActivite;
        this.nombreParticipant = nombreParticipant;
        this.transport = transport;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTypeActivite() {
        return typeActivite;
    }

    public void setTypeActivite(String typeActivite) {
        this.typeActivite = typeActivite;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTemps() {
        return temps;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }

    public int getPrixActivite() {
        return prixActivite;
    }

    public void setPrixActivite(int prixActivite) {
        this.prixActivite = prixActivite;
    }

    public int getNombreParticipant() {
        return nombreParticipant;
    }

    public void setNombreParticipant(int nombreParticipant) {
        this.nombreParticipant = nombreParticipant;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    @Override
    public String toString() {
        return "Activite{" + "id=" + id + ", nom=" + nom + ", typeActivite=" + typeActivite + ", description=" + description + ", temps=" + temps + ", prixActivite=" + prixActivite + ", nombreParticipant=" + nombreParticipant + ", transport=" + transport + '}';
    }
    
    
}
