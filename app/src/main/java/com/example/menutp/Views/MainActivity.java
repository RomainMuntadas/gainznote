package com.example.menutp.Views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.menutp.Controles.Controle;
import com.example.menutp.Modele.AccesLocal;
import com.example.menutp.R;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
    Controle controle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        controle = Controle.getInstance(this);

        AccesLocal accesLocal = new AccesLocal(this);

        //Mise en place de la toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //VIDAGE DE LA BDD POUR TESTS
        this.deleteDatabase("bdGainzNote.sqlite");

        //region Récupération des textviews
        TextView version = (TextView) findViewById(R.id.version);
        TextView edition = (TextView) findViewById(R.id.edition);
        TextView gainz = (TextView) findViewById(R.id.gainz);
        TextView score_gainz = (TextView) findViewById(R.id.nb_gainz);
        TextView score_faible = (TextView) findViewById(R.id.nb_faible);
        TextView faible = (TextView) findViewById(R.id.faible);
        //endregion

        //region Récupération des boutons
        Button btn_nouveau = (Button) findViewById(R.id.nouvelleSeance);
        Button btn_historique = (Button) findViewById(R.id.historique);
        //endregion

        //region SetFonts
        setFont(version, "CloisterBlack copy.ttf");
        setFont(score_faible, "death_font_ver1_0.ttf");
        setFont(score_gainz, "death_font_ver1_0.ttf");
        setFont(edition, "RyukExtra copy.ttf");
        setFont(btn_nouveau, "RyuksHandwriting copy.ttf");
        setFont(btn_historique, "RyuksHandwriting copy.ttf");
        setFont(gainz, "RyuksHandwriting copy.ttf");
        setFont(faible, "RyuksHandwriting copy.ttf");
        //endregion


        //Passage à la création d'une nouvelle seance
        btn_nouveau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof Button) {
                    Intent intent = new Intent(MainActivity.this, NouvelleSeance.class);
                    startActivity(intent);
                }
            }
        });

        //Passage à la view de l'historique des séances
        btn_historique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof Button) {
                    Intent intent = new Intent(MainActivity.this, historique.class);
                    startActivity(intent);
                }
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.toString() == getResources().getString(R.string.Param)) {
            Intent i = new Intent(MainActivity.this, Parametres.class);
            startActivity(i);
        }
        else if (item.getTitle() == getResources().getString(R.string.stats))
        {
            Intent i = new Intent(MainActivity.this, Stats.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


}
