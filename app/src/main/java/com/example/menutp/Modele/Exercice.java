package com.example.menutp.Modele;

public class Exercice {
    public Double tempsRepos;

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



    public Integer getIdSeance() {
        return idSeance;
    }

    public int getIdType() {
        return idType;
    }

    public String getNotes() {
        return notes;
    }

    @Override
    public String toString() {
        return "Exercice{" +
                "tempsRepos=" + tempsRepos +
                ", notes='" + notes + '\'' +
                '}';
    }
}
