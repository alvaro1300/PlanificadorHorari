package com.ramos.alvaro.horesajuntament2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Alvaro on 24/06/2018.
 */

public class Configuracion extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_config);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




    }
}
