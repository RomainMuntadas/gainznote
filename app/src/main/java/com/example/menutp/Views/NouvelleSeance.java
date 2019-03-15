package com.example.menutp.Views;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menutp.Controles.Controle;
import com.example.menutp.Modele.AccesLocal;
import com.example.menutp.Outils.FileOperation;
import com.example.menutp.R;
import com.example.menutp.Modele.Seance;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Calendar;
import java.util.Date;


public class NouvelleSeance extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private static Controle controle;
    Calendar c;
    DatePickerDialog dpd;
    Integer nbSeance;
    Button btnDate;
    File f;
    int idSeanceModif;


    @SuppressLint({"ResourceAsColor", "CutPasteId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        idSeanceModif = getIntent().getIntExtra("ID_SEANCE", -1);

        this.controle = Controle.getInstance(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouvelle_seance);
        Controleur controleur = new Controleur();


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.titreNewSeance));


        btnDate = findViewById(R.id.datePicker);

        final Spinner spinner_Type = (Spinner) findViewById(R.id.spinner_Seance);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.seances, R.layout.my_spinner);
        adapter.setDropDownViewResource(R.layout.my_spinner_drop_down);
        spinner_Type.setAdapter(adapter);
        spinner_Type.setOnItemSelectedListener(this);


        btnDate.setText(FileOperation.dateToString(new Date()));
        EditText libelleSeance = findViewById(R.id.Edit_NomSeance);
        TextView Txt_Duree = (TextView) findViewById(R.id.Txt_Duree);
        TextView Txt_Notes = (TextView) findViewById(R.id.Txt_Notes);
        Button btn_valider = (Button) findViewById(R.id.validerSeance);


        // setFont(libelleSeance, "RyukExtra copy.ttf");
        setFont(btnDate, "death_font_ver1_0.ttf");
        setFont(Txt_Duree, "death_font_ver1_0.ttf");
       // setFont(btn_valider, "death_font_ver1_0.ttf");
        setFont(Txt_Notes, "RyukExtra copy.ttf");
        //setFont(, "death_font_ver1_0.ttf");

        //Si l'idSeanceModif !=1, c'est qu'on vient de l'historique
        if (idSeanceModif != -1) {
            AccesLocal accesLocal = new AccesLocal(this);
            Button btn_editerExo = findViewById(R.id.btn_EditExo);

            //On affiche le bouton modifier
            btn_editerExo.setVisibility(View.VISIBLE);
            btnDate.setOnClickListener(controleur);

            //changement du titre
            getSupportActionBar().setTitle(getString(R.string.titleModifSeance));
            //changement du texte du bouton valider
            btn_valider.setText(R.string.finAjoutModif);

            //récupération des données de la séance
            Seance seance = accesLocal.getSeanceFromId(idSeanceModif);
            TextView dureeSeance = findViewById(R.id.dureeSeance);
            //Remplissage des textviews a partir de la séance existante
            dureeSeance.setText(Integer.toString(seance.getDureeSeance()));
            Txt_Notes.setText(seance.getNotes());
            btnDate.setText(FileOperation.dateToString(seance.getDateSeance()));
            libelleSeance.setText(seance.getNomSeance());

            btn_editerExo.setOnClickListener(controleur);
        }
        //  btnType.setOnClickListener(controleurTypeSeance);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                dpd = new DatePickerDialog(NouvelleSeance.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        btnDate.setText(dayOfMonth + "/" + month + "/" + year);
                    }
                }, day, month, year);
                dpd.show();
            }
        });
        btn_valider.setOnClickListener(controleur);

        final TextView textDate = (TextView) findViewById(R.id.datePicker);
        textDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EditText NomSeance = findViewById(R.id.Edit_NomSeance);
                if (!spinner_Type.getSelectedItem().toString().equals("Autre")) {
                    NomSeance.setText("Seance " + spinner_Type.getSelectedItem().toString() + " du " + textDate.getText());
                } else {
                    NomSeance.setText("Seance du " + textDate.getText());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        EditText NomSeance = findViewById(R.id.Edit_NomSeance);
        Spinner spinner_Type = (Spinner) findViewById(R.id.spinner_Seance);
        TextView textDate = (TextView) findViewById(R.id.datePicker);
        if (!spinner_Type.getSelectedItem().toString().equals("Autre")) {
            NomSeance.setText("Seance " + spinner_Type.getSelectedItem().toString() + " du " + textDate.getText());
        } else {
            NomSeance.setText("Seance du " + textDate.getText());

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.toString() == getResources().getString(R.string.Param)) {
            Intent i = new Intent(NouvelleSeance.this, Parametres.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    public int getIdSeanceModif() {
        return idSeanceModif;
    }

    /**
     * Recupere les données dans les textView
     * @return un objet Seance contenant les données récupérées
     */
    public Seance recupererDonneeSeance(){
        AccesLocal accesLocal = new AccesLocal(NouvelleSeance.this);
        Spinner spinner_Type = (Spinner) findViewById(R.id.spinner_Seance);
        TextView libelleSeance = (TextView) findViewById(R.id.Edit_NomSeance);
        TextView duree = (TextView) findViewById(R.id.dureeSeance);
        TextView Txt_Notes = (TextView) findViewById(R.id.Txt_Notes);


        Date dateSeance = FileOperation.stringToDate(btnDate.getText().toString());

        String nomSeance = libelleSeance.getText().toString();
        String TypeSeance = spinner_Type.getSelectedItem().toString();

        String notes = Txt_Notes.getText().toString();


        if (nomSeance.equals("")) {
            nomSeance = "Seance" + TypeSeance;
        }
        int dureeSeance = 0;
        if (!duree.getText().toString().equals("")) {
            dureeSeance = Integer.parseInt(duree.getText().toString());
        }
        Seance seance = new Seance(nomSeance, TypeSeance, dateSeance, dureeSeance, notes);
        return seance;

    }
    class Controleur implements View.OnClickListener {
        private AccesLocal accesLocal;


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {
            if (v instanceof EditText) {
                EditText nomSeance = (EditText) v;
                nomSeance.setCursorVisible(true);
                Toast.makeText(NouvelleSeance.this, "clic", Toast.LENGTH_LONG).show();
            }


            if (v instanceof Button) {
                Button button = (Button) v;
                switch (button.getText().toString()) {

                    case "Commencer":
                        AccesLocal accesLocal = new AccesLocal(NouvelleSeance.this);
                        Spinner spinner_Type = (Spinner) findViewById(R.id.spinner_Seance);
                        TextView libelleSeance = (TextView) findViewById(R.id.Edit_NomSeance);
                        TextView duree = (TextView) findViewById(R.id.dureeSeance);
                        TextView Txt_Notes = (TextView) findViewById(R.id.Txt_Notes);


                        Date dateSeance = FileOperation.stringToDate(btnDate.getText().toString());

                        String nomSeance = libelleSeance.getText().toString();
                        String TypeSeance = spinner_Type.getSelectedItem().toString();

                        String notes = Txt_Notes.getText().toString();


                        if (nomSeance.equals("")) {
                            nomSeance = "Seance" + TypeSeance;
                        }
                        int dureeSeance = 0;
                        if (!duree.getText().toString().equals("")) {
                            dureeSeance = Integer.parseInt(duree.getText().toString());
                        }
                        Seance seance = new Seance(nomSeance, TypeSeance, dateSeance, dureeSeance, notes);
                        //sauvegarde de la seance
                        accesLocal.addSeance(seance);

                        Intent intent = new Intent(NouvelleSeance.this, ajout_exercice.class);
                        //ATTENTION Le getLastSeance ne marchera que dans le cadre de la création d'une séance
                        intent.putExtra("ID_SEANCE", accesLocal.getLastSeance().getIdSeance());
                        startActivity(intent);
                        break;
                    case "Editer les exercices":
                        accesLocal = new AccesLocal(NouvelleSeance.this);
                        seance = recupererDonneeSeance();
                        seance.setIdSeance(getIdSeanceModif());
                        accesLocal.mettreAjourSeance(seance);
                        intent = new Intent(NouvelleSeance.this, ajout_exercice.class);
                        intent.putExtra("ID_SEANCE", getIdSeanceModif());
                        startActivity(intent);
                        break;

                    case "Terminer":
                        accesLocal = new AccesLocal(NouvelleSeance.this);
                        seance = recupererDonneeSeance();
                        seance.setIdSeance(getIdSeanceModif());
                        accesLocal.mettreAjourSeance(seance);
                        intent = new Intent(NouvelleSeance.this, MainActivity.class);
                        startActivity(intent);

                    default:

                        break;

                }





            }
        }


    }


    public void setFont(TextView textView, String fontName) {
        if (fontName != null) {
            try {
                Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/" + fontName);
                textView.setTypeface(typeface);
            } catch (Exception e) {
                Log.e("FONT", fontName + " not found", e);
            }
        }
    }

}


