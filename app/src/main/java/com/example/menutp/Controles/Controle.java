package com.example.menutp.Controles;

import android.content.Context;

import com.example.menutp.Modele.AccesLocal;
import com.example.menutp.Modele.Seance;

import java.util.List;

public final class Controle {

    private static Controle instance = null;
    private static Seance seance;
    private static AccesLocal accesLocal;

    private Controle() {
        super();
    }

    public static final Controle getInstance(Context contexte) {
        if (Controle.instance == null) {
            Controle.instance = new Controle();
            accesLocal = new AccesLocal(contexte);
            seance = accesLocal.getLastSeance();
        }
        return Controle.instance;
    }

    public void creerSeance(String nomSeance, String typeSeance, String dateSeance, int dureeSeance, String notes, Context contexte) {
        Seance seance = new Seance(nomSeance, dateSeance, typeSeance, dureeSeance, notes);
        accesLocal.addSeance(seance);
    }


    public Seance getLastSeance(Context contexte){
        accesLocal = new AccesLocal(contexte);
        return accesLocal.getLastSeance();

    }

    public List<Seance> getToutesSeances(Context contexte){
        accesLocal = new AccesLocal(contexte);
        return accesLocal.getAllSeance();
    }

    public void viderBdd(Context contexte){
        accesLocal = new AccesLocal(contexte);
        accesLocal.viderBdd();
    }

}




