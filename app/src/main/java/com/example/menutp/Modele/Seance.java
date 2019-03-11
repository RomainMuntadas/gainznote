package com.example.menutp.Modele;

import com.example.menutp.Outils.FileOperation;

import java.io.Serializable;
import java.util.Date;

public class Seance implements Serializable {
    private int idSeance;
    private String nomSeance;
    private String typeSeance;
    private Date dateSeance;
    private int dureeSeance;
    private String notes;





    public Seance(String nomSeance, String typeSeance, Date dateSeance, int dureeSeance, String notes) {
        this.idSeance = idSeance;
        this.nomSeance = nomSeance;
        this.typeSeance = typeSeance;
        this.dateSeance = dateSeance;
        this.dureeSeance = dureeSeance;
        this.notes = notes;

    }

    public int getIdSeance() {
        return idSeance;
    }

    public void setIdSeance(int idSeance) {
        this.idSeance = idSeance;
    }

    public String getNomSeance() {
        return nomSeance;
    }

    public String getTypeSeance() {
        return typeSeance;
    }

    public Date getDateSeance() {
        return dateSeance;
    }

    public int getDureeSeance() {
        return dureeSeance;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return  nomSeance +", Durée: " + dureeSeance +" minutes";
    }
}
