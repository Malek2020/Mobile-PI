package com.culinair.entities;

public class Plat {

    private Integer id;
    private String nom;
    private String typeCuisine;
    private String description;
    private float prix;
    private String image;
    private Restaurant restaurant;

    public Plat(Integer id, String nom, String typeCuisine, String description, float prix, String image, Restaurant restaurant) {
        this.id = id;
        this.nom = nom;
        this.typeCuisine = typeCuisine;
        this.description = description;
        this.prix = prix;
        this.image = image;
        this.restaurant = restaurant;
    }

    public Plat(String nom, String typeCuisine, String description, float prix, String image, Restaurant restaurant) {
        this.nom = nom;
        this.typeCuisine = typeCuisine;
        this.description = description;
        this.prix = prix;
        this.image = image;
        this.restaurant = restaurant;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTypeCuisine() {
        return typeCuisine;
    }

    public void setTypeCuisine(String typeCuisine) {
        this.typeCuisine = typeCuisine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
