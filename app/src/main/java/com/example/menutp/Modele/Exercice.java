package com.example.menutp.Modele;

public class Exercice {
    public Double tempsRepos;
    private int idExercice;
    private Integer idSeance;
    private String notes;
    private int idType;

    public Exercice(Double tempsRepos, String notes,Integer idSeance, Integer idType) {
        this.idSeance = idSeance;
        this.idType = idType;
        this.notes = notes;
        this.tempsRepos = tempsRepos;
    }

    public Double getTempsRepos() {
        return tempsRepos;
    }


    public int getIdExercice() {
        return idExercice;
    }

    public Integer getIdSeance() {
        return idSeance;
    }

    public void setTempsRepos(Double tempsRepos) {
        this.tempsRepos = tempsRepos;
    }

    public void setIdSeance(Integer idSeance) {
        this.idSeance = idSeance;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public int getIdType() {
        return idType;
    }

    public String getNotes() {
        return notes;
    }

    public void setIdExercice(int idExercice) {
        this.idExercice = idExercice;
    }

    @Override
    public String toString() {
        return "Temps de repos: " + tempsRepos + " minute";
    }
}
