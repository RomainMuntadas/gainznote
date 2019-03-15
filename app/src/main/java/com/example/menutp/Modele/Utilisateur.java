package com.example.menutp.Modele;

import android.content.Context;

import com.example.menutp.Controles.Controle;
import com.example.menutp.Outils.FileOperation;
import com.example.menutp.R;
import com.example.menutp.Views.MainActivity;
import com.example.menutp.Views.Parametres;

import java.util.Date;

public final class Utilisateur {
    private String username;
    private Integer nb_Seance;
    private String sexe;
    private Integer nbGainz;
    private Integer nbFaible;
    private static Utilisateur instance = null;

    private Utilisateur(String username, String sexe, Integer nbGainz, Integer nbFaible, Integer nb_Seance) {
        super();
        this.username = username;
        this.nb_Seance = nb_Seance;
        this.sexe = sexe;
        this.nbGainz = nbGainz;
        this.nbFaible = nbFaible;
    }

    public static synchronized Utilisateur getInstance(Context contexte) {
        if (Utilisateur.instance == null) {
            Utilisateur.instance = new Utilisateur("Utilisateur", "M", 0, 0,-1);
        }
        return Utilisateur.instance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNbGainz(Integer nbGainz) {this.nbGainz = nbGainz;}

    public void setNbFaible(Integer nbFaible) {this.nbFaible = nbFaible;}

    public void setSexe(String sexe)
    {
        if(String.valueOf(sexe.charAt(0)).equalsIgnoreCase("F"))
            this.sexe = "F";
        else
            this.sexe = "M";
    }

    public void setNb_Seance(Integer nb_Seance) {
        this.nb_Seance = nb_Seance;
    }

    public static void setInstance(Utilisateur instance) {
        Utilisateur.instance = instance;
    }

    public String getUsername() {
        return this.username;
    }

    public Integer getNb_Seance() { return nb_Seance; }

    public String getSexe() {return this.sexe;}

    public Integer getNbGainz(){return this.nbGainz;}

    public Integer getNbFaible(){return this.nbFaible;}

    public static Utilisateur getInstance() {
        return instance;
    }
}
