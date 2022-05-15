package com.culinair.entities;

public class Pays {

    private int id;
    private String nomPays;

    public Pays(int id, String nomPays) {
        this.id = id;
        this.nomPays = nomPays;
    }

    public Pays(String nomPays) {
        this.nomPays = nomPays;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomPays() {
        return nomPays;
    }

    public void setNomPays(String nomPays) {
        this.nomPays = nomPays;
    }

}