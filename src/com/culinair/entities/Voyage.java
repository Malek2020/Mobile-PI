package com.culinair.entities;

import com.culinair.gui.front.voyage.DisplayAll;

import java.util.Date;

public class Voyage implements Comparable<Voyage> {

    private int id;
    private Date dateDepart;
    private Date dateRetour;
    private int nbrVoyageurs;
    private float prix;
    private Ville ville;

    public Voyage(int id, Date dateDepart, Date dateRetour, int nbrVoyageurs, float prix, Ville ville) {
        this.id = id;
        this.dateDepart = dateDepart;
        this.dateRetour = dateRetour;
        this.nbrVoyageurs = nbrVoyageurs;
        this.prix = prix;
        this.ville = ville;
    }

    public Voyage(Date dateDepart, Date dateRetour, int nbrVoyageurs, float prix, Ville ville) {
        this.dateDepart = dateDepart;
        this.dateRetour = dateRetour;
        this.nbrVoyageurs = nbrVoyageurs;
        this.prix = prix;
        this.ville = ville;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public Date getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
    }

    public int getNbrVoyageurs() {
        return nbrVoyageurs;
    }

    public void setNbrVoyageurs(int nbrVoyageurs) {
        this.nbrVoyageurs = nbrVoyageurs;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }

    public Ville getVille() {
        return ville;
    }

    public void setVille(Ville ville) {
        this.ville = ville;
    }

    @Override
    public int compareTo(Voyage voyage) {
        switch (DisplayAll.voyageCompareVar) {
            case "dateDepart":
                return Long.compare(this.getDateDepart().getTime(), voyage.getDateDepart().getTime());
            case "dateRetour":
                return Long.compare(this.dateRetour.getTime(), voyage.dateRetour.getTime());
            case "nbrVoyageurs":
                return Float.compare(this.nbrVoyageurs, voyage.nbrVoyageurs);
            case "prix":
                return Float.compare(this.prix, voyage.prix);
            case "ville":
                return this.ville.getVille().compareTo(voyage.ville.getVille());
            default:
                return 0;
        }
    }
}