package com.example.menutp.Modele;

import android.content.Context;

import com.example.menutp.Controles.Controle;
import com.example.menutp.Outils.FileOperation;
import com.example.menutp.R;
import com.example.menutp.Views.MainActivity;

import java.util.Date;

public final class Utilisateur {
    private String nom ="Defaut";
    private String prenom="Defaut";
    private Double poid=50D;
    private Date dateNaissance= FileOperation.stringToDate("01/01/2019");
    private double taille=50.0;
    private Integer nb_Seance =3 ;
    private static Utilisateur instance = null;
    private String langue = "Français";

    public Utilisateur(){

    }

    public static final Utilisateur getInstance(Context contexte) {
        if (Utilisateur.instance == null) {
            Utilisateur.instance = new Utilisateur();

        }
        return Utilisateur.instance;
    }

    public void setLangue(String langue) {this.langue = langue;}


    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setPoid(Double poid) {
        this.poid = poid;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setTaille(double taille) {
        this.taille = taille;
    }

    public void setNb_Seance(Integer nb_Seance) {
        this.nb_Seance = nb_Seance;
    }

    public static void setInstance(Utilisateur instance) {
        Utilisateur.instance = instance;
    }

    public String getNom() {
        return this.nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Double getPoid() {
        return poid;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public double getTaille() {
        return taille;
    }

    public Integer getNb_Seance() {
        return nb_Seance;
    }

    public String getLangue() {return this.langue;}

    public static Utilisateur getInstance() {
        return instance;
    }
}
