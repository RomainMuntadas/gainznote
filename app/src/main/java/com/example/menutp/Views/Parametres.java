package com.example.menutp.Views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.example.menutp.Modele.AccesLocal;
import com.example.menutp.Modele.Utilisateur;
import com.example.menutp.R;

public class Parametres extends AppCompatActivity {

    AccesLocal accesLocal = new AccesLocal(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.Param));

        /*
        Initialisation des boutons radio
         */

        //accesLocal.initialiserUtilisateur(this);
        //String langue = accesLocal.getUtilisateur().getLangue();
        String langue = "Français";
        RadioButton radio;
        if(langue == "Arabe")
        {
            radio = findViewById(R.id.radioButton4);
            radio.setChecked(true);
        }
        else if(langue == "Français")
        {
            radio = findViewById(R.id.radioButton3);
            radio.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void LangueRadioClicked(View view)
    {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId())
        {
            case R.id.radioButton3:
                if(checked)
                    break;
                accesLocal.getUtilisateur().setLangue("Français");
                accesLocal.sauvegarderUtilisateur();
            case R.id.radioButton4:
                if(checked)
                    break;
                accesLocal.getUtilisateur().setLangue("Arabe");
                accesLocal.sauvegarderUtilisateur();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
