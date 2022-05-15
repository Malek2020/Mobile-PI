package com.culinair.entities;

import java.util.Date;

public class Evenement {

    private int id;
    private String nom;
    private Date dateDebut;
    private Date dateFin;
    private String lieu;
    private String image;
    private float prix;
    private String description;
    private Promotion promotion;

    public Evenement(int id, String nom, Date dateDebut, Date dateFin, String lieu, String image, float prix, String description, Promotion promotion) {
        this.id = id;
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieu = lieu;
        this.image = image;
        this.prix = prix;
        this.description = description;
        this.promotion = promotion;
    }

    public Evenement(String nom, Date dateDebut, Date dateFin, String lieu, String image, float prix, String description, Promotion promotion) {
        this.nom = nom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieu = lieu;
        this.image = image;
        this.prix = prix;
        this.description = description;
        this.promotion = promotion;
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

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public void setPromotion(Promotion promotion) {
        this.promotion = promotion;
    }

}