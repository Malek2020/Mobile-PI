package com.culinair.entities;

public class Reservation{

    private Integer id;
    private Float num_vol;
    private String destination;
    private String date_depart;
    private String date_arrivee;
    private Float adulte;
    private Float enfant;
    private Guide guide;

    public Reservation(Integer id,  Float num_vol, String destination, String date_depart, String date_arrivee, Float adulte, Float enfant, Guide guide) {
        this.id = id;
        this.num_vol = num_vol;
        this.destination = destination;
        this.date_depart = date_depart;
        this.date_arrivee = date_arrivee;
        this.adulte = adulte;
        this.enfant = enfant;
        this.guide = guide;


        
    }

    public Reservation( Float num_vol, String destination, String date_depart, String date_arrivee, Float adulte, Float enfant, Guide guide) {
        this.num_vol = num_vol;
        this.destination = destination;
        this.date_depart = date_depart;
        this.date_arrivee = date_arrivee;
        this.adulte = adulte;
        this.enfant = enfant;
        this.guide = guide;


    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Float getnum_vol() {
        return num_vol;
    }

    public void setnum_vol(Float num_vol) {
        this.num_vol = num_vol;
    }

    public String getdestination() {
        return destination;
    }

    public void setdestination(String destination) {
        this.destination = destination;
    }

    public String getdate_depart() {
        return date_depart;
    }

    public void setdate_depart(String date_depart) {
        this.date_depart = date_depart;
    }

    public String getdate_arrivee() {
        return date_arrivee;
    }

    public void setdate_arrivee(String date_arrivee) {
        this.date_arrivee = date_arrivee;
    }

    public Float getadulte() {
        return adulte;
    }

    public void setadulte(Float adulte) {
        this.adulte = adulte;
    }
    public Float getenfant() {
        return enfant;
    }

    public void setenfant(Float enfant) {
        this.enfant = enfant;
    }
    
        public Guide getGuide() {
        return guide;
    }

    public void setGuide(Restaurant Guide) {
        this.guide = guide;
    }
    
}
