package com.culinair.entities;

public class Transport {

    private int id;
    private int dureeTransport;
    private String typeTransport;
    private int capacite;

    public Transport(int id, int dureeTransport, String typeTransport, int capacite) {
        this.id = id;
        this.dureeTransport = dureeTransport;
        this.typeTransport = typeTransport;
        this.capacite = capacite;
    }

    public Transport(int dureeTransport, String typeTransport, int capacite) {
        this.dureeTransport = dureeTransport;
        this.typeTransport = typeTransport;
        this.capacite = capacite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDureeTransport() {
        return dureeTransport;
    }

    public void setDureeTransport(int dureeTransport) {
        this.dureeTransport = dureeTransport;
    }

    public String getTypeTransport() {
        return typeTransport;
    }

    public void setTypeTransport(String typeTransport) {
        this.typeTransport = typeTransport;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }
}
