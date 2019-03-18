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
    private String seances;
    private String sexe;
    private static Utilisateur instance = null;

    private Utilisateur(String username, String sexe, String seances) {
        super();
        this.username = username;
        this.seances = seances;
        this.sexe = sexe;
    }

    public static synchronized Utilisateur getInstance(Context contexte) {
        if (Utilisateur.instance == null) {
            Utilisateur.instance = new Utilisateur("Utilisateur", "M","");
        }
        return Utilisateur.instance;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSexe(String sexe)
    {
        if(String.valueOf(sexe.charAt(0)).equalsIgnoreCase("F"))
            this.sexe = "F";
        else
            this.sexe = "M";
    }

    public void setSeances(String seances) {
        this.seances = seances;
    }

    public static void setInstance(Utilisateur instance) {
        Utilisateur.instance = instance;
    }

    public String getUsername() {
        return this.username;
    }

    public String getSeances() { return this.seances; }

    public String getSexe() {return this.sexe;}

    public static Utilisateur getInstance() {
        return instance;
    }
}
