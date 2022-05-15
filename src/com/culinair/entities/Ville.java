package com.culinair.entities;

public class Ville {

    private int id;
    private String ville;
    private Pays pays;

    public Ville(int id, String ville, Pays pays) {
        this.id = id;
        this.ville = ville;
        this.pays = pays;
    }

    public Ville(String ville, Pays pays) {
        this.ville = ville;
        this.pays = pays;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public Pays getPays() {
        return pays;
    }

    public void setPays(Pays pays) {
        this.pays = pays;
    }

}