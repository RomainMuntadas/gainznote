package com.example.menutp.Controles;

import android.content.Context;

import com.example.menutp.Modele.AccesLocal;
import com.example.menutp.Modele.Seance;
import com.example.menutp.Outils.FileOperation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public final class Controle {

    private static Controle instance = null;

    private static AccesLocal accesLocal;

    private Controle() {
        super();
    }

    public static final Controle getInstance(Context contexte) {
        if (Controle.instance == null) {
            Controle.instance = new Controle();

        }
        return Controle.instance;
    }

    public void creerSeance(String nomSeance, String typeSeance, Date dateSeance, int dureeSeance, String notes, Context contexte) {
        Seance seance = new Seance(nomSeance, typeSeance, dateSeance, dureeSeance, notes);
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

    public String dateToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public Date stringToDate(String date){

        Date dateFormat = null;
        try {
            dateFormat=new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat;
    }

    public static int countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }




}




