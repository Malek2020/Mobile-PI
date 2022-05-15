package com.culinair.entities;

public class Restaurant {

    private int id;
    private String nom;
    private String type;
    private String description;
    private String emplacement;
    private String image;
    private String longitude;
    private String lattitude;

    public Restaurant(int id, String nom, String type, String description, String emplacement, String image, String longitude, String lattitude) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.description = description;
        this.emplacement = emplacement;
        this.image = image;
        this.longitude = longitude;
        this.lattitude = lattitude;
    }

    public Restaurant(String nom, String type, String description, String emplacement, String image, String longitude, String lattitude) {
        this.nom = nom;
        this.type = type;
        this.description = description;
        this.emplacement = emplacement;
        this.image = image;
        this.longitude = longitude;
        this.lattitude = lattitude;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }


}
