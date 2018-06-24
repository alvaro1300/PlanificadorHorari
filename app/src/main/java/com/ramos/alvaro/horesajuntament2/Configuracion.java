package com.ramos.alvaro.horesajuntament2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alvaro on 24/06/2018.
 */

public class Configuracion extends AppCompatActivity {

    private String limitHorasSem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_config);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cargarConfig(Constantes.ARXIU_CONFIG);


    }




    public void layoutConfigLimitSem(View v){



        Intent i = new Intent(this, LimitHorasSem.class);
        i.putExtra("horaStringOriginal", limitHorasSem);

        startActivityForResult(i,1);

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //recuperarTextViewPerId();
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                limitHorasSem=data.getStringExtra("temps");

            }

        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {


            }

        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {



            } else if (resultCode == RESULT_CANCELED){


            }

        }

        guardarConfig(Constantes.ARXIU_CONFIG);
    }


    public void cargarConfig(String arxiu) {
        SharedPreferences prefe = getSharedPreferences(arxiu, Context.MODE_PRIVATE);

        limitHorasSem = prefe.getString(Constantes.ARXIU_TITOL_LIMIT_HORAS, Constantes.LIMIT_HORES_SETMANA_DEF);


    }


    public void guardarConfig (String arxiu) {
        SharedPreferences prefe = getSharedPreferences(arxiu, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor=prefe.edit();

        editor.putString(Constantes.ARXIU_TITOL_LIMIT_HORAS,limitHorasSem);

        editor.apply();

    }




}
