package com.culinair.entities;

public class Promotion {

    private int id;
    private int tauxPromotion;
    private String description;

    public Promotion(int id, int tauxPromotion, String description) {
        this.id = id;
        this.tauxPromotion = tauxPromotion;
        this.description = description;
    }

    public Promotion(int tauxPromotion, String description) {
        this.tauxPromotion = tauxPromotion;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTauxPromotion() {
        return tauxPromotion;
    }

    public void setTauxPromotion(int tauxPromotion) {
        this.tauxPromotion = tauxPromotion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Promotion{" + "id=" + id + ", tauxPromotion=" + tauxPromotion + ", description=" + description + '}';
    }

}
