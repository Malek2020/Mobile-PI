package com.culinair.entities;

public class Guide {

    private Integer id;
    private String nom;
    private String prenom;
    private String description;
    private String image;

    public Guide(Integer id, String nom, String prenom, String description,  String image) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.description = description;
        this.image = image;
    }

    public Guide(String nom, String prenom, String description, String image ) {
        this.nom = nom;
        this.prenom = prenom;
        this.description = description;
        this.image = image;
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

    public String getPrenom() {
        return prenom;
    }

    public void setprenom(String typeCuisine) {
        this.prenom = typeCuisine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
      public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

  
}
