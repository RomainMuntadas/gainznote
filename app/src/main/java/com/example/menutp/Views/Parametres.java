package com.example.menutp.Views;

import android.arch.lifecycle.AndroidViewModel;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menutp.Modele.AccesLocal;
import com.example.menutp.Modele.Exercice;
import com.example.menutp.Modele.Seance;
import com.example.menutp.Modele.Serie;
import com.example.menutp.Modele.TypeExercice;
import com.example.menutp.Modele.Utilisateur;
import com.example.menutp.Outils.FileOperation;
import com.example.menutp.Outils.MySQLiteOpenHelper;
import com.example.menutp.R;

import org.w3c.dom.Text;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class Parametres extends AppCompatActivity {

    AccesLocal accesLocal = new AccesLocal(this);
    Utilisateur user;
    String sexe;
    Boolean firstUse = false;
    String joursEntrainement = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        user = Utilisateur.getInstance(this);

        if(getIntent().getBooleanExtra("firstUse", false))
        {
            this.firstUse = true;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.Param));

        Button bouton = findViewById(R.id.btn_viderBDD);
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Parametres.this);
                builder.setTitle("Suppression données");
                builder.setMessage("Êtes-vous sûr de vouloir supprimer les données de l'application ? Cette opération est irreversible");
                builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Parametres.this.deleteDatabase("bdGainzNote.sqlite");
                        joursEntrainement = "";
                        accesLocal = new AccesLocal(getApplicationContext());
                        Toast.makeText(Parametres.this, "Données supprimées", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        user = Utilisateur.getInstance(this);
        accesLocal.initialiserUtilisateur(getApplicationContext());

        joursEntrainement = user.getSeances();

        final Button joursSeances = findViewById(R.id.buttonJours);
        final String[] jours = getResources().getStringArray(R.array.jours);
        final boolean[] joursChecked = new boolean[jours.length];
        final ArrayList<Integer> selectedJours = new ArrayList<>();
        final TextView libelleSeance = findViewById(R.id.libelleSeance);
        libelleSeance.setText("Jours d'entrainement"+"\n"+FileOperation.affichageJours(user.getSeances()));

        joursSeances.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Parametres.this);
                builder.setTitle("Jours d'entrainements");
                builder.setMultiChoiceItems(jours, joursChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {
                        if(isChecked)
                        {
                            if(! selectedJours.contains(position))
                                selectedJours.add(position);
                            else
                                selectedJours.remove(position);
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        joursEntrainement = "";
                        for(int i = 0; i < selectedJours.size(); i++)
                        {
                            joursEntrainement+= jours[selectedJours.get(i)]+ " ";
                        }
                        libelleSeance.setText("Jours d'entrainement"+"\n"+FileOperation.affichageJours(joursEntrainement));
                    }
                });

                builder.setNegativeButton("ANNULER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        final TextInputEditText username = findViewById(R.id.username);
        username.setText(user.getUsername());

        RadioButton sexeF = findViewById(R.id.sexeF);
        RadioButton sexeM = findViewById(R.id.sexeM);
        sexe = user.getSexe();
        if(sexe.equalsIgnoreCase("M"))
            sexeM.setChecked(true);
        else
            sexeF.setChecked(true);

        Button valider = findViewById(R.id.Param_Valider);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!joursEntrainement.equalsIgnoreCase("")) {
                    user.setSeances(joursEntrainement);
                    user.setUsername(username.getText().toString());
                    user.setSexe(sexe);
                    accesLocal.sauvegarderUtilisateur();
                    accesLocal.initialiserUtilisateur(getApplicationContext());
                    Intent i = new Intent(Parametres.this, MainActivity.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(Parametres.this, "Veuillez selectionner au moins un jour d'entrainement", Toast.LENGTH_LONG).show();
                }
            }
        });
        //Bouton qui va générer des seances pour la démonstrations lors de la présentation aux profs
        Button GenererSeance = findViewById(R.id.btn_GenSeance);
        GenererSeance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccesLocal accesLocal = new AccesLocal(Parametres.this);


                Date date1 = FileOperation.stringToDate("01/03/2019");
                Date date2 = FileOperation.stringToDate("08/03/2019");
                Date date3 = FileOperation.stringToDate("15/03/2019");
                Date date4 = FileOperation.stringToDate("22/03/2019");
                Date date5 = FileOperation.stringToDate("27/03/2019");
                Date date6 = FileOperation.stringToDate("14/03/2019");
                Date date7 = FileOperation.stringToDate("04/03/2019");


                Seance seance1 = new Seance("Seance Pectoraux du "+ FileOperation.dateToString(date1),"Pectoraux", date1, 60, "C'est la séance 1");
                Seance seance2 = new Seance("Seance Pectoraux du "+FileOperation.dateToString(date2),"Pectoraux", date2, 60, "C'est la séance 2");
                Seance seance3 = new Seance("Seance Pectoraux du "+FileOperation.dateToString(date3),"Pectoraux", date3, 60, "C'est la séance 3");
                Seance seance4 = new Seance("Seance Pectoraux du "+FileOperation.dateToString(date4),"Pectoraux", date4, 60, "C'est la séance 4");
                Seance seance5 = new Seance("Seance Jambes du "+FileOperation.dateToString(date5),"Jambes", date5, 60, "C'est la séance 4");
                Seance seance6 = new Seance("Seance Jambes du "+FileOperation.dateToString(date6),"Jambes", date6, 60, "C'est la séance 4");
                Seance seance7 = new Seance("Seance Bras du "+FileOperation.dateToString(date6),"Bras", date7, 60, "C'est la séance 4");

                accesLocal.addSeance(seance1);
                TypeExercice te1 = accesLocal.getTypeFromString("Développé couché barre");
                TypeExercice te2 = accesLocal.getTypeFromString("Développé couché haltères");
                TypeExercice te3 = accesLocal.getTypeFromString("Squat");
                TypeExercice te4 = accesLocal.getTypeFromString("Curl haltères");

                Exercice exercice1a = new Exercice(1.5, "", accesLocal.getLastSeance().getIdSeance(),te1.getIdType());
                Exercice exercice1b = new Exercice(1.5, "", accesLocal.getLastSeance().getIdSeance(),te2.getIdType());
                Exercice exercice1c = new Exercice(1.5, "", accesLocal.getLastSeance().getIdSeance(),te3.getIdType());
                accesLocal.addExerciceToSeance(exercice1a);
                Serie serie1a = new Serie(12,50.0,accesLocal.getLastExo().getIdExercice());
                accesLocal.addExerciceToSeance(exercice1b);
                Serie serie1b = new Serie(12,40.0,accesLocal.getLastExo().getIdExercice());
                accesLocal.addExerciceToSeance(exercice1c);
                Serie serie1c = new Serie(12,40.0,accesLocal.getLastExo().getIdExercice());
                accesLocal.addSerie(serie1a);
                accesLocal.addSerie(serie1b);
                accesLocal.addSerie(serie1c);

                accesLocal.addSeance(seance2);
                Exercice exercice2a = new Exercice(1.5, "", accesLocal.getLastSeance().getIdSeance(),te1.getIdType());
                Exercice exercice2b = new Exercice(1.5, "", accesLocal.getLastSeance().getIdSeance(),te2.getIdType());
                Exercice exercice2c = new Exercice(1.5, "", accesLocal.getLastSeance().getIdSeance(),te4.getIdType());
                accesLocal.addExerciceToSeance(exercice2a);
                Serie serie2a = new Serie(12,60.0,accesLocal.getLastExo().getIdExercice());
                accesLocal.addExerciceToSeance(exercice2b);
                Serie serie2b = new Serie(12,30.0,accesLocal.getLastExo().getIdExercice());
                accesLocal.addExerciceToSeance(exercice2c);
                Serie serie2c = new Serie(12,15.0,accesLocal.getLastExo().getIdExercice());
                accesLocal.addSerie(serie2c);
                accesLocal.addSerie(serie2a);
                accesLocal.addSerie(serie2b);


                accesLocal.addSeance(seance3);
                Exercice exercice3a = new Exercice(1.5, "", accesLocal.getLastSeance().getIdSeance(),te1.getIdType());
                Exercice exercice3b = new Exercice(1.5, "", accesLocal.getLastSeance().getIdSeance(),te2.getIdType());
                Exercice exercice3c = new Exercice(1.5, "", accesLocal.getLastSeance().getIdSeance(),te4.getIdType());
                accesLocal.addExerciceToSeance(exercice3a);
                Serie serie3a = new Serie(12,90.0,accesLocal.getLastExo().getIdExercice());
                accesLocal.addExerciceToSeance(exercice3b);
                Serie serie3b = new Serie(12,45.0,accesLocal.getLastExo().getIdExercice());
                accesLocal.addExerciceToSeance(exercice3c);
                Serie serie3c = new Serie(12,10.0,accesLocal.getLastExo().getIdExercice());
                accesLocal.addSerie(serie3a);
                accesLocal.addSerie(serie3b);
                accesLocal.addSerie(serie3c);



                accesLocal.addSeance(seance4);
                Exercice exercice4a = new Exercice(1.5, "", accesLocal.getLastSeance().getIdSeance(),te1.getIdType());
                Exercice exercice4b = new Exercice(1.5, "", accesLocal.getLastSeance().getIdSeance(),te2.getIdType());
                Exercice exercice4c = new Exercice(1.5, "", accesLocal.getLastSeance().getIdSeance(),te4.getIdType());
                accesLocal.addExerciceToSeance(exercice4a);
                Serie serie4a = new Serie(12,60.0,accesLocal.getLastExo().getIdExercice());
                accesLocal.addExerciceToSeance(exercice4b);
                Serie serie4b = new Serie(12,70.0,accesLocal.getLastExo().getIdExercice());
                accesLocal.addExerciceToSeance(exercice4c);
                Serie serie4c = new Serie(12,15.0,accesLocal.getLastExo().getIdExercice());
                accesLocal.addSerie(serie4c);
                accesLocal.addSerie(serie4a);
                accesLocal.addSerie(serie4b);

                accesLocal.addSeance(seance5);
                accesLocal.addSeance(seance6);
                accesLocal.addSeance(seance7);



            }
        });


    }


    public void sexeClicked(View v)
    {
        boolean checked = ((RadioButton) v).isChecked();
        switch(v.getId())
        {
            case R.id.sexeF:
                if(checked) {
                    sexe = "F";
                    break;
                }
            case R.id.sexeM:
                if(checked) {
                    sexe = "M";
                    break;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if(!this.firstUse)
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(!firstUse) {
            if (item.toString() == getResources().getString(R.string.Param)) {
                Intent i = new Intent(Parametres.this, Parametres.class);
                startActivity(i);
            } else if (item.getTitle() == getResources().getString(R.string.stats)) {
                Intent i = new Intent(Parametres.this, Stats.class);
                startActivity(i);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
