package com.example.menutp.Views;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.menutp.Controles.Controle;
import com.example.menutp.Modele.AccesLocal;
import com.example.menutp.R;
import com.example.menutp.Modele.Seance;

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



    @SuppressLint({"ResourceAsColor", "CutPasteId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        this.controle = Controle.getInstance(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouvelle_seance);
        controleur controleur = new controleur();


        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);


        btnDate = findViewById(R.id.datePicker);
        Spinner spinner_Type = (Spinner) findViewById(R.id.spinner_Seance);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.seances, R.layout.my_spinner);
        adapter.setDropDownViewResource(R.layout.my_spinner_drop_down);
        spinner_Type.setAdapter(adapter);
        spinner_Type.setOnItemSelectedListener(this);


        btnDate.setText(new Date().toString());
        TextView libelleSeance = (TextView) findViewById(R.id.Edit_NomSeance);
        TextView Txt_Duree = (TextView) findViewById(R.id.Txt_Duree);
        TextView Txt_Notes = (TextView) findViewById(R.id.Txt_Notes);
        Button btn_valider = (Button) findViewById(R.id.valider);

        setFont(libelleSeance, "RyukExtra copy.ttf");
        setFont(btnDate, "death_font_ver1_0.ttf");
        setFont(Txt_Duree, "death_font_ver1_0.ttf");
        setFont(btn_valider, "death_font_ver1_0.ttf");
        setFont(Txt_Notes, "RyukExtra copy.ttf");
        //setFont(, "death_font_ver1_0.ttf");


        //  btnType.setOnClickListener(controleurTypeSeance);
        btnDate.setOnClickListener(controleur);
        btn_valider.setOnClickListener(controleur);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

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

    public static int countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }


    class controleur implements View.OnClickListener {
        private AccesLocal accesLocal;


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {


            if (v instanceof Button) {
                Button button = (Button) v;


                if (button.getText().equals(getString(R.string.txtBtnCommencer))) {

                    this.accesLocal = new AccesLocal(NouvelleSeance.this);

                    Spinner spinner_Type = (Spinner) findViewById(R.id.spinner_Seance);
                    TextView libelleSeance = (TextView) findViewById(R.id.Edit_NomSeance);
                    TextView duree = (TextView) findViewById(R.id.dureeSeance);
                    TextView Txt_Notes = (TextView) findViewById(R.id.Txt_Notes);


                    String dateSeance = btnDate.getText().toString();

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



                    Seance seance = controle.getLastSeance(NouvelleSeance.this);



                    NouvelleSeance.controle.creerSeance(nomSeance, TypeSeance, dateSeance, dureeSeance, notes, NouvelleSeance.this);
                    seance = new Seance(nomSeance, TypeSeance, dateSeance, dureeSeance, notes);

                    accesLocal.addSeance(seance);
                } else {
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


