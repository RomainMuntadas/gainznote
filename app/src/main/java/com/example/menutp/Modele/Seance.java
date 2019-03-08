package com.example.menutp.Modele;

import java.io.Serializable;

public class Seance implements Serializable {
    private int idSeance;
    private String nomSeance;
    private String typeSeance;
    private String dateSeance;
    private int dureeSeance;
    private String notes;
    int idUtilisateur;
   // private List<String> Excercice;

    public Seance(String nomSeance, String typeSeance, String dateSeance, int dureeSeance, String notes) {
        this.idSeance = idSeance;
        this.nomSeance = nomSeance;
        this.typeSeance = typeSeance;
        this.dateSeance = dateSeance;
        this.dureeSeance = dureeSeance;
        this.notes = notes;
        this.idUtilisateur =1;
      //  this.Excercice = new ArrayList<>();
    }

    public int getIdSeance() {
        return idSeance;
    }


    public String getNomSeance() {
        return nomSeance;
    }

    public String getTypeSeance() {
        return typeSeance;
    }

    public String getDateSeance() {
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
        return "Seance{" +
                "'" + nomSeance + '\'' +
                ", Type: '" + typeSeance + '\'' +
                ", Date: '" + dateSeance + '\''+
                ", dureeSeance: " + dureeSeance +" minutes" +
                '}';
    }
}
