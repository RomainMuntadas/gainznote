package com.example.menutp.Modele;

public class Serie {
    private int idSerie;
    private int nbRepetitions;
    private Double poid;
    private int idExercice;

    public Serie(int nbRepetitions, Double poid, int idExercice) {
        this.nbRepetitions = nbRepetitions;
        this.poid = poid;
        this.idExercice = idExercice;
    }

    public int getIdSerie() {
        return idSerie;
    }

    public void setIdSerie(int idSerie) {
        this.idSerie = idSerie;
    }

    public int getNbRepetitions() {
        return nbRepetitions;
    }

    public void setNbRepetitions(int nbRepetitions) {
        this.nbRepetitions = nbRepetitions;
    }

    public Double getPoid() {
        return poid;
    }

    public void setPoid(Double poid) {
        this.poid = poid;
    }

    public int getIdExercice() {
        return idExercice;
    }

    public void setIdExercice(int idExercice) {
        this.idExercice = idExercice;
    }
}
