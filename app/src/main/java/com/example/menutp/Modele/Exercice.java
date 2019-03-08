package com.example.menutp.Modele;

public class Exercice {
    private String nom;
    private Integer idSeance;
    private String notes;
    private int idType;

    public Exercice(String nom, Integer idSeance, Integer idType, String notes) {
        this.nom = nom;
        this.idSeance = idSeance;
        this.idType = idType;
        this.notes = notes;
    }


    public String getNom() {
        return nom;
    }

    public Integer getIdSeance() {
        return idSeance;
    }

    public int getIdType() {
        return idType;
    }

    public String getNotes() {
        return notes;
    }
}
