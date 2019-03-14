package com.example.menutp.Modele;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.menutp.Outils.FileOperation;
import com.example.menutp.Outils.MySQLiteOpenHelper;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AccesLocal {
    //propriétés
    private String nomBase = "bdGainzNote.sqlite";
    private Integer versionBase = 1;
    private MySQLiteOpenHelper accesBd;
    private SQLiteDatabase bd;
    private static Utilisateur utilisateur;
    private String type_Exercice =
            "CREATE TABLE TYPE_EXERICE("
                    + " ID_TYPE INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + " NOM TEXT NOT NULL,"
                    + " GROUPE_MUSCULAIRE TEXT NOT NULL,"
                    + "EST_POLY INTEGER NOT NULL);";


    /**
     * Constructeur
     *
     * @param contexte
     */
    public AccesLocal(Context contexte) {
        accesBd = new MySQLiteOpenHelper(contexte, nomBase, null, versionBase);
        utilisateur = Utilisateur.getInstance(contexte);
    }
    //region methodes de Seance

    /**
     * Ajout d'une nouvelle seance à la BDD
     *
     * @param seance
     */
    public void addSeance(Seance seance) {
        bd = accesBd.getWritableDatabase();
        String req = "Insert into Seance (nomSeance,dateSeance,typeSeance,dureeSeance,notes) values";
        req += "(\"" + seance.getNomSeance() + "\",\"" + FileOperation.dateToString(seance.getDateSeance()) + "\",\"" + seance.getTypeSeance() + "\",\"" + seance.getDureeSeance() + "\",\"" + seance.getNotes() + "\");";
        bd.execSQL(req);


    }

    /**
     * Recuperation de la derniere seance de la base de donnee.
     *
     * @return
     */
    public Seance getLastSeance() {
        bd = accesBd.getReadableDatabase();
        Seance seance = null;
        String req = "Select * from Seance";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToLast();
        if (!curseur.isAfterLast()) {
            seance = cursorToSeance(curseur);
        }
        curseur.close();
        return seance;
    }

    /**
     * A IMPLEMENTER, OU MODIFIER addSeance EN CONSEQUENCE
     */
    public void udateSeance(Seance seance) {

    }

    /**
     * Cree une liste de toutes les séances
     *
     * @return une liste des séances
     */
    public List<Seance> getAllSeance() {
        bd = accesBd.getReadableDatabase();
        List<Seance> seances = new ArrayList<Seance>();
        String req = "Select * from Seance";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        if (!curseur.isAfterLast()) {
            seances.add(cursorToSeance(curseur));
            while (!curseur.isLast()) {
                curseur.moveToNext();
                seances.add(cursorToSeance(curseur));

            }
        }
        curseur.close();
        return seances;
    }

    /**
     * Supprime toute la séance ainsi que ses dépendances (exercices, series)
     *
     * @param idSeance id de la seance a supprimer
     */
    public void supprimerSeance(int idSeance) {
        bd = accesBd.getWritableDatabase();
        List<Exercice> listExercices = getExerciceSeance(idSeance);
        for (Exercice e : listExercices) {
            supprimerSerieExercice(e.getIdExercice());
            supprimerExercice(e.getIdExercice(), idSeance);
        }
        String req = "DELETE FROM SEANCE WHERE idSeance =" + idSeance + ";";
        bd.execSQL(req);
    }

    /**
     * Retourne un objet seance de l'id donné
     * @param idSeance id de la séance
     * @return l'objet seance
     */
    public Seance getSeanceFromId(int idSeance) {
        bd = accesBd.getReadableDatabase();
        Seance seance = null;
        String req = "SELECT * FROM SEANCE WHERE idSeance = " + idSeance + ";";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        if(!curseur.isAfterLast()){
            seance = cursorToSeance(curseur);
        }
        curseur.close();
        return seance;
    }

    /**
     * Met a jour la séance donnée dans la bd
     * @param seance objet contenant les données a mettre a jour
     */
    public void mettreAjourSeance(Seance seance) {
        bd = accesBd.getWritableDatabase();
        String req = "UPDATE SEANCE " +
                "SET nomSeance = \""+seance.getNomSeance()+"\"," +
                "dateSeance = \""+FileOperation.dateToString(seance.getDateSeance())+"\"," +
                "typeSeance = \""+seance.getTypeSeance()+"\"," +
                "dureeSeance = "+seance.getDureeSeance()+"," +
                "notes = \""+seance.getNotes()+"\"" +
                "WHERE idSeance ="+seance.getIdSeance()+";";
        bd.execSQL(req);

    }
    /**
     * Permet de créer une seance directement avec un curseur
     *
     * @param curseur curseur résultant d'une requête rawQuery
     * @return une séance contenant son ID d'apres la bd
     */
    public Seance cursorToSeance(Cursor curseur) {
        int idSeance = curseur.getInt(0);
        String nomSeance = curseur.getString(1);
        String dateSeance = curseur.getString(2);
        String typeSeance = curseur.getString(3);
        int dureeSeance = curseur.getInt(4);
        String notes = curseur.getString(5);
        Seance seance = new Seance(nomSeance, typeSeance, FileOperation.stringToDate(dateSeance), dureeSeance, notes);
        seance.setIdSeance(idSeance);
        return seance;
    }


    //endregion

    //region Methodes d'utilisateur

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    /**
     * Sauvegarde les données de l'utilisateur dans la BD
     * A faire à chaque fois que l'on modifie les données de l'utilisateur
     */
    public void sauvegarderUtilisateur() {
        bd = accesBd.getWritableDatabase();
        String req = "UPDATE UTILISATEUR "
                + "SET NOM = \'" + utilisateur.getNom() + "\', "
                + "PRENOM = \'" + utilisateur.getPrenom() + "\', "
                + "POID = " + Double.toString(utilisateur.getPoid()) + ", "
                + "DATE_NAISSANCE = \'" + FileOperation.dateToString(utilisateur.getDateNaissance()) + "\', "
                + "TAILLE = " + utilisateur.getTaille() + ", "
                + "NB_SEANCE = " + Integer.toString(utilisateur.getNb_Seance()) + ";";
        bd.execSQL(req);
    }

    /**
     * Initialise les données de l'utilisateur au lancement de l'appli
     * en fonction de la BD
     */
    public void initialiserUtilisateur(Context context) {
        bd = accesBd.getReadableDatabase();

        String req = "Select * from Utilisateur";
//NE SE FAIT QUE SI LA BD EST VIDE (1ER LANCEMENT DE L'APPLI )
// NB: ACTUELLEMENT ELLE SE VIDE AUTOMATIQUEMENT
        Cursor curseur = bd.rawQuery(req, null);
        if (curseur != null && curseur.moveToFirst()) {
            utilisateur.setNom(curseur.getString(0));
            utilisateur.setPrenom(curseur.getString(2));
            utilisateur.setPoid(Double.parseDouble(curseur.getString(3)));
            utilisateur.setDateNaissance(FileOperation.stringToDate(curseur.getString(4)));
            utilisateur.setTaille(Double.parseDouble(curseur.getString(5)));
            utilisateur.setNb_Seance(curseur.getInt(6));

        } else {
            this.utilisateur = Utilisateur.getInstance(context);
            utilisateur.setPrenom("test");
            req = "INSERT INTO UTILISATEUR (NOM, PRENOM, POID, DATE_NAISSANCE, TAILLE, NB_SEANCE, LANGUE) VALUES("
                    + "\"" + utilisateur.getNom() + "\","
                    + "\"" + utilisateur.getPrenom() + "\","
                    + Double.toString(utilisateur.getPoid()) + ","
                    + "\"" + utilisateur.getDateNaissance() + "\","
                    + Double.toString(utilisateur.getTaille()) + ","
                    + utilisateur.getNb_Seance() + ")";
            bd = accesBd.getWritableDatabase();
            bd.execSQL(req);

        }
        curseur.close();

    }


    //endregion

    //region Méthodes de typeExercice

    public void creerTypeExercice(TypeExercice typeExercice) {
        bd = accesBd.getWritableDatabase();
        int estPoly;
        if (typeExercice.estPolyarticulaire()) {
            estPoly = 1;
        } else {
            estPoly = 0;
        }

        String req = "INSERT INTO EXERCICE VALUES("
                + "\"" + typeExercice.getNom() + "\""
                + "\"" + typeExercice.getGroupeMusculaire() + "\""
                + "\"" + estPoly + "\");";
        bd.execSQL(req);

    }

    public TypeExercice getTypeExercice(Exercice exercice) {
        bd = accesBd.getReadableDatabase();
        String req = "SELECT * FROM TYPE_EXERCICE"
                + " WHERE TYPE_EXERCICE.ID_TYPE = " + exercice.getIdType() + ";";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        TypeExercice typeExercice = curseurToTypeExercice(curseur);
        curseur.close();
        return typeExercice;
    }

    public String getNomExercice(int idType) {
        bd = accesBd.getReadableDatabase();
        String req = "SELECT NOM FROM TYPE_EXERCICE"
                + " WHERE TYPE_EXERCICE.ID_TYPE = " + idType + ";";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        if (!curseur.isAfterLast()) {
            return curseur.getString(0);
        }
        return null;
    }

    public List<TypeExercice> getToutLesTypesExercices() {
        bd = accesBd.getReadableDatabase();
        List<TypeExercice> types_exercices = new ArrayList<TypeExercice>();
        String req = "SELECT * FROM TYPE_EXERCICE";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        if (!curseur.isAfterLast()) {
            types_exercices.add(curseurToTypeExercice(curseur));
            while (!curseur.isLast()) {
                curseur.moveToNext();
                types_exercices.add(curseurToTypeExercice(curseur));

            }
        }
        return types_exercices;
    }

    public List<CharSequence> groupesMusculaire() {
        bd = accesBd.getReadableDatabase();
        String req = "SELECT DISTINCT GROUPE_MUSCULAIRE FROM TYPE_EXERCICE;";
        List<CharSequence> groupes = new ArrayList<>();

        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        if (!curseur.isAfterLast()) {
            groupes.add(curseur.getString(0));
            while (!curseur.isLast()) {
                curseur.moveToNext();
                groupes.add(curseur.getString(0));

            }
        }
        return groupes;
    }

    public List<TypeExercice> getTypeFromGroupe(String groupeMusculaire) {
        bd = accesBd.getReadableDatabase();
        List<TypeExercice> types_exercices = new ArrayList<TypeExercice>();
        String req = "SELECT * FROM TYPE_EXERCICE WHERE GROUPE_MUSCULAIRE =\"" + groupeMusculaire + "\";";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        if (!curseur.isAfterLast()) {
            types_exercices.add(curseurToTypeExercice(curseur));
            while (!curseur.isLast()) {
                curseur.moveToNext();
                types_exercices.add(curseurToTypeExercice(curseur));
            }
        }
        return types_exercices;
    }

    public TypeExercice getTypeFromString(String nomType) {
        bd = accesBd.getReadableDatabase();
        String req = "SELECT * FROM TYPE_EXERCICE WHERE NOM = " + nomType + ';';
        Cursor curseur = bd.rawQuery(req, null);
        TypeExercice type = curseurToTypeExercice(curseur);
        if (!curseur.isAfterLast()) {
            return type;
        }
        curseur.close();
        return null;

    }

    public int getIdTypeFromString(String nomType) {
        bd = accesBd.getReadableDatabase();
        String req = "SELECT * FROM TYPE_EXERCICE WHERE NOM = \"" + nomType + "\";";
        Cursor curseur = bd.rawQuery(req, null);
        if (!curseur.isAfterLast() && curseur.moveToFirst()) {
            return curseur.getInt(0);
        }
        return -1;

    }

    public TypeExercice curseurToTypeExercice(Cursor curseur) {
        if (!curseur.isAfterLast()) {
            if (curseur.getInt(3) == 0) {
                return new TypeExercice(curseur.getString(1), curseur.getString(2), false);

            } else {

                return new TypeExercice(curseur.getString(1), curseur.getString(2), true);
            }
        }
        return null;
    }


//endregion

    //region Methodes d'exercice

    /**
     * l'exercice sera ajouté à la séance grâce à Id séance contenu dans l'objet exercice
     *
     * @param exercice
     * @return l'id de l'exercice crée.
     */
    public int addExerciceToSeance(Exercice exercice) {
        bd = accesBd.getWritableDatabase();
        String req = "insert into Exercice(TPS_REPOS, NOTES, ID_SEANCE , ID_TYPE) values(";
        req += "\"" + Double.toString(exercice.getTempsRepos()) + "\""
                + ",\"" + exercice.getNotes() + "\","
                + exercice.getIdSeance() + ","
                + exercice.getIdType()
                + ");";
        bd.execSQL(req);
        return getLastExo().getIdExercice();
    }

    /**
     * Récupère le dernier exercice crée
     *
     * @return
     */
    public Exercice getLastExo() {
        bd = accesBd.getReadableDatabase();
        Exercice exercice = null;
        String req = "Select * from Exercice";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToLast();
        if (!curseur.isAfterLast()) {
            exercice = cursorToExercice(curseur);
            exercice.setIdExercice(curseur.getInt(0));
        }
        curseur.close();
        return exercice;
    }

    /**
     * Retourne la liste des exercices d'une séance donnée
     *
     * @param idSeance id de la séance dont on veut récupérer les exercices
     * @return
     */
    public List<Exercice> getExerciceSeance(int idSeance) {
        bd = accesBd.getReadableDatabase();

        String req = "Select * from Exercice WHERE ID_SEANCE = " + idSeance + ";";
        Cursor curseur = bd.rawQuery(req, null);
        List<Exercice> exercices = new ArrayList<Exercice>();
        curseur.moveToFirst();
        if (!curseur.isAfterLast()) {
            exercices.add(cursorToExercice(curseur));
            while (!curseur.isLast()) {
                curseur.moveToNext();
                exercices.add(cursorToExercice(curseur));
            }
        }
        curseur.close();
        return exercices;

    }

    /**
     * Récupere un objet exercice a partir de son id
     *
     * @param idExercice id de l'exercice
     * @return L'exercice correspondant a l'id
     */
    public Exercice getExercice(int idExercice) {
        bd = accesBd.getReadableDatabase();
        String req = "Select * from Exercice where ID_EXERCICE = " + idExercice + ";";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        Exercice exercice = cursorToExercice(curseur);
        curseur.close();
        return exercice;
    }

    /**
     * Met a jour dans la base de donnée
     *
     * @param exercice   objet contenant les données a changer
     * @param idExercice id de l'exercice a modifier
     */
    public void miseAJourExercice(Exercice exercice, int idExercice) {
        bd = accesBd.getWritableDatabase();
        String req = "UPDATE Exercice "
                + "SET TPS_REPOS = \"" + Double.toString(exercice.getTempsRepos()) + "\","
                + "NOTES  = \"" + exercice.getNotes() + "\","
                + "ID_SEANCE = " + exercice.getIdSeance() + ","
                + "ID_TYPE = " + exercice.getIdType() + " "
                + "WHERE ID_EXERCICE =" + idExercice + ";";
        bd.execSQL(req);
    }

    /**
     * Supprime l'exercice de la séance passée en parametre
     *
     * @param idExercice id de l'exercice
     * @param idSeance   id de la séance
     */
    public void supprimerExercice(int idExercice, int idSeance) {
        bd = accesBd.getWritableDatabase();
        supprimerSerieExercice(idExercice);
        String req = "DELETE FROM EXERCICE" +
                " WHERE ID_EXERCICE = " + idExercice + "" +
                " AND ID_SEANCE = " + idSeance + ";";

        bd.execSQL(req);
    }

    /**
     * Récupère un objet a partir d'un curseur. Set l'id de l'exercice de la bd dans l'objet exercice
     *
     * @param curseur curseur résultant d'une requete rawQuery
     * @return Un objet exercice
     */
    public Exercice cursorToExercice(Cursor curseur) {
        Double tempsRepos = 0D;
        Integer idExercice = curseur.getInt(0);
        if (!(curseur.getString(1).equals(""))) {
            tempsRepos = Double.parseDouble(curseur.getString(1));
        }
        String notes = "";
        if (!curseur.isNull(2)) {
            notes = curseur.getString(2);
        }
        Integer idSeance = curseur.getInt(3);
        Integer idType = curseur.getInt(4);

        Exercice exercice = new Exercice(tempsRepos, notes, idSeance, idType);
        exercice.setIdExercice(idExercice);
        return exercice;
    }

    //endregion

    //region Methode de séries

    /**
     * Ajoute une série a un exercice.
     *
     * @param serie Contient l'id de l'exercice
     */
    public void addSerie(Serie serie) {

        bd = accesBd.getWritableDatabase();
        String req = "INSERT INTO SERIE(REPETITIONS, POIDS, ID_EXERCICE) VALUES("
                + serie.getNbRepetitions() + ","
                + "\"" + Double.toString(serie.getPoid()) + "\","
                + serie.getIdExercice() + ");";
        bd.execSQL(req);

    }

    /**
     * Compte le nombre de séries d'un exercice donné.
     *
     * @param exercice
     * @return
     */
    public int countSerieExercice(Exercice exercice) {
        int nbSerie = -1;
        bd = accesBd.getReadableDatabase();
        String req = "SELECT * FROM SERIE WHERE ID_EXERCICE =" + exercice.getIdExercice();
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        nbSerie = curseur.getCount();
        curseur.close();
        return nbSerie;
    }

    /**
     * Retourne 1 serie d'un exercice. Utilisé car les séries sont toutes identiques pour un exercice donné pour l'instant
     *
     * @param exercice
     * @return
     */
    public Serie getUneSerie(Exercice exercice) {

        bd = accesBd.getReadableDatabase();
        String req = "SELECT * FROM SERIE WHERE ID_EXERCICE =" + exercice.getIdExercice() + ";";
        Cursor curseur = bd.rawQuery(req, null);
        curseur.moveToFirst();
        Serie serie = curseurToSerie(curseur);

        curseur.close();
        return serie;
    }

    /**
     * Modifie les séries dans la bd d'apres les données entrées dans creer_modifier_exercice
     *
     * @param serie
     * @param idExercice
     */
    public void miseAJourSeries(Serie serie, int idExercice) {
        bd = accesBd.getWritableDatabase();
        String req = "UPDATE SERIE "
                + "SET REPETITIONS = " + serie.getNbRepetitions() + ","
                + "POIDS = \"" + serie.getPoid() + "\" "
                + "WHERE ID_EXERCICE =" + idExercice + ";";
        bd.execSQL(req);
    }

    /**
     * Supprime les séries d'un exercice / Utilisé pour supprimer toutes traces d'un exercice
     *
     * @param idExercice
     */
    public void supprimerSerieExercice(int idExercice) {
        bd = accesBd.getWritableDatabase();
        String req = "DELETE FROM SERIE WHERE ID_EXERCICE = " + idExercice + ";";
        bd.execSQL(req);
    }

    public Serie curseurToSerie(Cursor curseur) {

        return new Serie(curseur.getInt(1), Double.parseDouble(curseur.getString(2)), curseur.getInt(3));

    }
    //endregion

    /**
     * Clean la bd
     */
    public void viderBdd() {
        bd = accesBd.getWritableDatabase();
        String req = "delete from Seance";
        bd.execSQL(req);
    }



}
