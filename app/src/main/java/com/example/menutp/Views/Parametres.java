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
import com.example.menutp.Modele.Utilisateur;
import com.example.menutp.Outils.FileOperation;
import com.example.menutp.Outils.MySQLiteOpenHelper;
import com.example.menutp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

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
                    Log.i("valider", "ok");
                }
                else {
                    Toast.makeText(Parametres.this, "Veuillez selectionner au moins un jour d'entrainement", Toast.LENGTH_LONG).show();
                    Log.i("Valider", "pas ok");
                }
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
