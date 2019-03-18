package com.example.menutp.Views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menutp.Modele.AccesLocal;
import com.example.menutp.Modele.Exercice;
import com.example.menutp.Modele.Serie;
import com.example.menutp.Modele.TypeExercice;
import com.example.menutp.R;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class creer_modifier_Exercice extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {
    private static int idSeance;
    private static int idExercice;
    private AccesLocal accesLocal;
    Spinner spinnerGroupeMusculaire;
    List<TypeExercice> listExoGroup = new ArrayList<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_modifier__exercice);
        accesLocal = new AccesLocal(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.titleCreationSeance));
        //On récupère l'id de la séance passée en parametre
        this.idSeance = getIntent().getIntExtra("ID_SEANCE", -1);
        if (this.idSeance == -1) {
            Toast.makeText(this, getResources().getString(R.string.ErrorGettingId), Toast.LENGTH_LONG).show();
        }


        List<CharSequence> groupesMusculaire = accesLocal.groupesMusculaire();
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.my_spinner, groupesMusculaire);
        adapter.setDropDownViewResource(R.layout.my_spinner_drop_down);

        spinnerGroupeMusculaire = findViewById(R.id.Spinner_TypeExo);

        spinnerGroupeMusculaire.setAdapter(adapter);
        spinnerGroupeMusculaire.setOnItemSelectedListener(this);

        Button valider = (Button) findViewById(R.id.Btn_Valider);

        //Cas où l'utilisateur a cliqué sur un exercice existant
        idExercice = getIntent().getIntExtra("ID_EXERCICE", -1);

        if (idExercice != -1) {
            Exercice exercice = accesLocal.getExercice(idExercice);

            TypeExercice type = accesLocal.getTypeExercice(exercice);
            Serie uneSerie = accesLocal.getUneSerie(exercice);
            Integer nbSerie = accesLocal.countSerieExercice(exercice);

            TextView tv_tempsRepos = findViewById(R.id.ET_TempsRepos);
            TextView tv_poid = findViewById(R.id.ET_Poid);
            TextView tv_nbSerie = findViewById(R.id.ET_NbSeries);
            TextView tv_nbRepet = findViewById(R.id.ET_NbRepet);
            TextView tv_typeChoisi = findViewById(R.id.TypeChoisi);
            TextView tv_Notes = findViewById(R.id.ET_Notes);

            tv_tempsRepos.setText(Double.toString(exercice.getTempsRepos()));
            tv_poid.setText(Double.toString(exercice.getTempsRepos()));  //Pour l'instant, l'utilisateur n'a pas la possibilité de faire des séries différentes, donc le poid est le même pour toutes les séries.
            tv_nbSerie.setText(nbSerie + "");
            tv_nbRepet.setText(uneSerie.getNbRepetitions() + "");
            tv_typeChoisi.setText(type.getNom());
            tv_poid.setText(Double.toString(uneSerie.getPoid()));
            tv_Notes.setText(exercice.getNotes());
            valider.setText("Modifier");


        }


        valider.setOnClickListener(new Controleur());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerGroupeMusculaire = findViewById(R.id.Spinner_TypeExo);
        listExoGroup = accesLocal.getTypeFromGroupe((String) spinnerGroupeMusculaire.getSelectedItem());
        List<String> listExoGrpStr = new ArrayList<>();
        for (TypeExercice tE : listExoGroup) {

            listExoGrpStr.add(tE.toString());
        }

        ArrayAdapter<String> adapteurType = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listExoGrpStr);
        ListView listViewType = findViewById(R.id.Lv_Type);
        listViewType.setAdapter(adapteurType);
        listViewType.setOnItemClickListener(creer_modifier_Exercice.this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.toString() == getResources().getString(R.string.Param)) {
            Intent i = new Intent(creer_modifier_Exercice.this, Parametres.class);
            startActivity(i);
        }
        else if (item.getTitle() == getResources().getString(R.string.stats))
        {
            Intent i = new Intent(creer_modifier_Exercice.this, Stats.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listExoGroup = accesLocal.getTypeFromGroupe((String) spinnerGroupeMusculaire.getSelectedItem());
        TypeExercice typeSelected = listExoGroup.get(position);
        TextView exerciceSelected = findViewById(R.id.TypeChoisi);
        exerciceSelected.setText(typeSelected.getNom());


    }

    class Controleur implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            AccesLocal accesLocal = new AccesLocal(creer_modifier_Exercice.this);

            if (v instanceof Button) {
                TextView exerciceSelected = findViewById(R.id.TypeChoisi);
                TextView tempsRepos = (TextView) findViewById(R.id.ET_TempsRepos);
                TextView notes = findViewById(R.id.ET_Notes);
                String nomType = exerciceSelected.getText().toString();
                idSeance = getIntent().getIntExtra("ID_SEANCE", -1);
                //CREATION
                if (((Button) v).getText().equals("Valider")) {

                    //On fait attention a ne pas entrer un exercice de type null.
                    if (!nomType.equals(getString(R.string.TextTypeDefaut))) {
                        int idType = accesLocal.getIdTypeFromString(nomType);

                        Exercice exercice = null;


                        //creation des exercices
                        Double tempsReposDbl = 0D;
                        String tempsReposStr = tempsRepos.getText().toString();

                        //Pour ne pas insert un null  => Double.parseDouble("") ne peut pas marcher
                        if (tempsReposStr.equals("")) {

                            exercice = new Exercice(0.0, notes.getText().toString(), idSeance, idType);
                        } else {

                            tempsReposDbl = Double.parseDouble(tempsReposStr);
                            exercice = new Exercice(tempsReposDbl, notes.getText().toString(), idSeance, idType);
                        }
                        //on enregistre l'exercice et on récupere son id.
                        int idExercice = accesLocal.addExerciceToSeance(exercice);


                        //Creation des series

                        TextView Et_NbSeries = findViewById(R.id.ET_NbSeries);
                        TextView Et_poid = findViewById(R.id.ET_Poid);
                        TextView Et_nbRepet = findViewById(R.id.ET_NbRepet);

                        Integer nbSeries = Integer.parseInt(Et_NbSeries.getText().toString());
                        Integer nbRepet = Integer.parseInt(Et_nbRepet.getText().toString());
                        Double poid = Double.parseDouble(Et_poid.getText().toString());

                        //enregistrement dans la bd
                        for (int i = 0; i < nbSeries; i++) {
                            Serie s = new Serie(nbRepet, poid, idExercice);
                            accesLocal.addSerie(s);

                        }
                        //On retourne a la liste des exercices.
                        Intent intent = new Intent(creer_modifier_Exercice.this, ajout_exercice.class);
                        intent.putExtra("ID_SEANCE", idSeance);
                        startActivity(intent);
                    } else {
                        Toast.makeText(creer_modifier_Exercice.this, "Veuillez choisir un type d'exercice", Toast.LENGTH_LONG).show();
                    }

                } else {
                    //MISE A JOUR
                    //On fait attention a ne pas entrer un exercice de type null.
                    if (!nomType.equals(getString(R.string.TextTypeDefaut))) {
                        int idType = accesLocal.getIdTypeFromString(nomType);
                        idSeance = getIntent().getIntExtra("ID_SEANCE", -1);
                        idExercice = getIntent().getIntExtra("ID_EXERCICE", -1);
                        Exercice exercice;


                        //creation des exercices à jours
                        Double tempsReposDbl = 0D;
                        String tempsReposStr = tempsRepos.getText().toString();

                        //Pour ne pas insert un null  => Double.parseDouble("") ne peut pas marcher
                        if (tempsReposStr.equals("")) {
                            exercice = new Exercice(0.0, notes.getText().toString(), idSeance, idType);
                        } else {

                            tempsReposDbl = Double.parseDouble(tempsReposStr);
                            exercice = new Exercice(tempsReposDbl, notes.getText().toString(), idSeance, idType);
                        }
                        //mise a jour de l'exercice
                        accesLocal.miseAJourExercice(exercice,idExercice);
                    }
                    //Mise a jour des séries

                    TextView Et_NbSeries = findViewById(R.id.ET_NbSeries);
                    TextView Et_poid = findViewById(R.id.ET_Poid);
                    TextView Et_nbRepet = findViewById(R.id.ET_NbRepet);

                    Integer nbSeries = Integer.parseInt(Et_NbSeries.getText().toString());
                    Integer nbRepet = Integer.parseInt(Et_nbRepet.getText().toString());
                    Double poid = Double.parseDouble(Et_poid.getText().toString());
                    //Mise a jour dans la BD
                    for (int i = 0; i < nbSeries; i++) {
                        Serie s = new Serie(nbRepet, poid, idExercice);
                        accesLocal.miseAJourSeries(s, idExercice);
                    }


                    //On retourne a la liste des exercices.
                    Intent intent = new Intent(creer_modifier_Exercice.this, ajout_exercice.class);
                    intent.putExtra("ID_SEANCE", idSeance);
                    startActivity(intent);

                }
            }
        }
    }
}
