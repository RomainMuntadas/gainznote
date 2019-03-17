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

public class Parametres extends AppCompatActivity {

    AccesLocal accesLocal = new AccesLocal(this);
    Utilisateur user;
    String sexe;
    Boolean firstUse = false;

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
                Parametres.this.deleteDatabase("bdGainzNote.sqlite");;
            }
        });

        user = Utilisateur.getInstance(this);
        accesLocal.initialiserUtilisateur(getApplicationContext());

        final TextInputEditText nbSeances = findViewById(R.id.NB_SEANCES);
        if(!this.firstUse)
            nbSeances.setText(user.getNb_Seance().toString());
        else
            nbSeances.setText(new Integer(3).toString());

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
                int nbSeancesInt = Integer.parseInt(nbSeances.getText().toString().replaceAll("[^0-9]", ""));
                user.setNb_Seance(nbSeancesInt);
                user.setUsername(username.getText().toString());
                user.setSexe(sexe);
                accesLocal.sauvegarderUtilisateur();
                accesLocal.initialiserUtilisateur(getApplicationContext());
                Intent i = new Intent(Parametres.this, MainActivity.class);
                startActivity(i);
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

        return super.onOptionsItemSelected(item);
    }
}
