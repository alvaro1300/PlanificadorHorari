package com.ramos.alvaro.horesajuntament2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Alvaro on 02/06/2018.
 */

public class HoresActivity extends AppCompatActivity {

    public static final int HORES = 0;
    public static final int MINUTS = 1;
    public static final int UNITAT = 1;
    public static final int DECIMAL = 0;

    public static final String STATE_HORAMINUTS = "horaMinuts";
    public static final String STATE_TEMPS_SETMANAL = "tempsSetmanal";



    private TextView tvHores, tvMinuts, tvDosPunts;
    private TextView tvTotalSemana;
    private Button btCancel, btOk, bt1, bt2,bt3, bt4 ,bt5, bt6, bt7, bt8, bt9;
    private TextView tvFocus = tvHores;
    private List<TextView> itemTime = new ArrayList<TextView>();
    private int position;
    private int posInterna;
    boolean posHores;
    boolean posMinuts;
    String valueHores;
    String valueMin;
    String value;
    int numero;

    String horaStringOriginal;
    String horaStringModificat=null;
    String totalSemanaOriginal;
    String totalSemanaModificat=null;
    String entrada, salida, noEfect, efect, limitHorasSem, tipus;

    boolean correcte = true;



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

        super.onSaveInstanceState(savedInstanceState);
        //Almacenar los strings que hayamos introducido en pantalla para que al girar la vista se vuelvan a cargar
        savedInstanceState.putString(STATE_HORAMINUTS, horaStringModificat);
        savedInstanceState.putString(STATE_TEMPS_SETMANAL, totalSemanaModificat);



    }

    @Override
    protected void onRestoreInstanceState(Bundle recuperaEstado){
        super.onRestoreInstanceState(recuperaEstado);
        horaStringModificat = recuperaEstado.getString(STATE_HORAMINUTS);
        totalSemanaModificat = recuperaEstado.getString(STATE_TEMPS_SETMANAL);

        ompleHoraMinuts(horaStringModificat);
        //ompleTotalSetmana(totalSemanaModificat);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* es lo mismo que el metodo onRestoreInstanceState(Bundle recuperaEstado). Este metodo se ejecuta despues del onCreate()
        //Cargamos los datos del savedInstance si existen
        if (savedInstanceState != null) {
            horaStringModificat = savedInstanceState.getString(STATE_HORAMINUTS);
            totalSemanaModificat = savedInstanceState.getString(STATE_TEMPS_SETMANAL);
        }
*/
        setContentView(R.layout.activity_introhores);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvHores=(TextView)findViewById(R.id.tvHores);
        tvMinuts=(TextView)findViewById(R.id.tvMinuts);
        tvDosPunts=(TextView)findViewById(R.id.tvDosPunts);
        tvTotalSemana=(TextView)findViewById(R.id.tvTotalSemana);

        btOk=(Button)findViewById(R.id.btOk);
        btCancel=(Button)findViewById(R.id.btCancel);
        bt1=(Button)findViewById(R.id.bt1);
        bt2=(Button)findViewById(R.id.bt2);
        bt3=(Button)findViewById(R.id.bt3);
        bt4=(Button)findViewById(R.id.bt4);
        bt5=(Button)findViewById(R.id.bt5);
        bt6=(Button)findViewById(R.id.bt6);
        bt7=(Button)findViewById(R.id.bt7);
        bt8=(Button)findViewById(R.id.bt8);
        bt9=(Button)findViewById(R.id.bt9);


        Bundle bundle= getIntent().getExtras();

        horaStringOriginal = bundle.getString("horaStringOriginal");
        totalSemanaOriginal = bundle.getString("totalSemanaStringOriginal");
        entrada = bundle.getString("entrada");
        salida = bundle.getString("salida");
        noEfect = bundle.getString("noEfect");
        efect =  bundle.getString("efect");
        limitHorasSem =  bundle.getString("limitHorasSemana");
        tipus =  bundle.getString("tipus");



        /*if (horaStringModificat!=null){
            ompleHoraMinuts(horaStringModificat);
            ompleTotalSetmana(totalSemanaModificat);

        } else {
        */
            ompleHoraMinuts(horaStringOriginal);
            ompleTotalSetmana(totalSemanaOriginal);

            horaStringModificat = horaStringOriginal;
            totalSemanaModificat = totalSemanaOriginal;
       // }

        //horaStringModificat = horaStringOriginal;
        //totalSemanaModificat = totalSemanaOriginal;
        //ompleHoraMinuts(horaStringModificat);
        //ompleTotalSetmana(totalSemanaModificat);


        posHores = true;
        posMinuts = true;

        posInterna = 1;


        itemTime.add(tvHores);
        itemTime.add(tvMinuts);
        position = 0;
        tvHores.setTextColor(Color.BLUE);

        valueHores = "00";
        valueMin = "00";
        value = "00";

    }

    public void ompleHoraMinuts (String valor){
        String hora;
        String min ;
        tvDosPunts.setText(":");
        String [] parts = valor.split(":");

        hora = parts[0];
        min= parts[1];

        tvHores.setText(hora);
        tvMinuts.setText(min);


    }

    public void ompleTotalSetmana(String valor){
        tvTotalSemana.setText(valor);
    }

    /*
    public String calcularTotalSetmana(){

        String resTotal = ERROR;
        boolean allDataOk = true;
        Calendar calRes = Operacions.stringToCalendar("00.00", TIME_FORMAT2);


        for (int i = 0; i < limitDies; i++) {
            String resDia= listaResDia.get(i).getText().toString();
            if (resDia.equalsIgnoreCase(ERROR)){
                allDataOk = false;
            } else {
                String[] arrayResDia = resDia.split(Pattern.quote("."));
                int minResDia = Integer.parseInt(arrayResDia[1]);
                int horaResDia = Integer.parseInt(arrayResDia[0]);

                calRes.add(Calendar.MINUTE, minResDia);
                calRes.add(Calendar.HOUR_OF_DAY, horaResDia);
            }
        }

        if (allDataOk){
            int dias = calRes.get(Calendar.DAY_OF_MONTH);
            int horas = calRes.get(Calendar.HOUR_OF_DAY);
            String minutes = Operacions.calendarToString(calRes, MINUTE_FORMAT);

            resTotal = 24*(dias-1) + horas+"."+minutes;
        }

        return resTotal;
    }*/



    public void comprobarEntrSalida(){

        String entrada = ;
        String salida;




    }

    public void fillEditText (String data, TextView tv){
        tv.setText(data);

    }

    public void pressHores (View v){

        //position = 0;
        focusHores();
        posInterna = 1;


    }

    public void pressMinuts (View v){

        //position = 1;
        focusMinuts();
        posInterna = 1;


    }

    public void focusHores(){

        position = 0;
        tvHores.setTextColor(Color.BLUE);
        tvMinuts.setTextColor(Color.BLACK);


    }

    public void focusMinuts(){

        position = 1;
        tvHores.setTextColor(Color.BLACK);
        tvMinuts.setTextColor(Color.BLUE);


    }

    public void bAccept(View v){

        String dataRetorn= tvHores.getText().toString()+":"+tvMinuts.getText().toString();


        Intent intent=new Intent();
        intent.putExtra("temps", dataRetorn);

        setResult(RESULT_OK, intent);
        finish();



    }

    public void cancel (View v){
        String num1= "1";




    }


    public void button1 (View v){
        String num = "1";
        accionButton(num);
        horaStringModificat= tvHores.getText().toString()+":"+tvMinuts.getText().toString();

    }


    public void button2 (View v){
        String num = "2";
        accionButton(num);
        horaStringModificat= tvHores.getText().toString()+":"+tvMinuts.getText().toString();
    }

    public void button3 (View v){
        String num = "3";
        accionButton(num);
        horaStringModificat= tvHores.getText().toString()+":"+tvMinuts.getText().toString();
    }

    public void button4 (View v){
        String num = "4";
        accionButton(num);
        horaStringModificat= tvHores.getText().toString()+":"+tvMinuts.getText().toString();
    }

    public void button5 (View v){
        String num = "5";
        accionButton(num);
        horaStringModificat= tvHores.getText().toString()+":"+tvMinuts.getText().toString();
    }

    public void button6 (View v){
        String num = "6";
        accionButton(num);
        horaStringModificat= tvHores.getText().toString()+":"+tvMinuts.getText().toString();
    }

    public void button7 (View v){
        String num = "7";
        accionButton(num);
        horaStringModificat= tvHores.getText().toString()+":"+tvMinuts.getText().toString();
    }

    public void button8 (View v){
        String num = "8";
        accionButton(num);
        horaStringModificat= tvHores.getText().toString()+":"+tvMinuts.getText().toString();
    }

    public void button9 (View v){
        String num = "9";
        accionButton(num);
        horaStringModificat= tvHores.getText().toString()+":"+tvMinuts.getText().toString();
    }

    public void button0 (View v){
        String num = "0";
        accionButton(num);
        horaStringModificat= tvHores.getText().toString()+":"+tvMinuts.getText().toString();
    }




    public void accionButton (String numStr){
        String tempsString;
        String tempUnitat;
        String tempDecimal;

        //int temps;
        tempsString = itemTime.get(position).getText().toString();
        //temps = Integer.parseInt(tempsString);

        if (posInterna == UNITAT){
            value = "0"+numStr;
        } else {
            tempUnitat = tempsString.substring(1);
            value = tempUnitat + numStr;
        }


        /*
        value = tempUnitat+ numStr;

        if (position ==HORES){
            tempDecimal = tempsString.substring(0);
            if (tempDecimal>2 ){
                value = "0"+numStr;

            }
        }
*/

        comprovaHores();
        itemTime.get(position).setText(value);

        if (posInterna == DECIMAL){
            switchPosition ();
        }
        switchPosicioInterna ();

    }



    public void comprovaHores(){
        //String ret = null;
        int maxMinuts = 59;
        int maxHores = 23;

        //Convierte a String a int
        numero = Integer.parseInt(value);

        if (position == HORES){
            if (numero>maxHores){
                //ret = value;
                value = "23";
            }

        } else {
            if (numero>maxMinuts){
                value = "59";
            }

        }

    }
/*
    public void comprovaHores(String num){
        //String ret = null;
        int maxMinuts = 59;
        int maxHores = 23;

        //Convierte a String a int
        numero = Integer.parseInt(value);

        if (position == HORES){
            if (numero<=maxHores){
                //ret = value;
            } else {
               // value = Integer.toString(maxHores);
                value = "00";
            }

        } else {
            if (numero<=maxMinuts){
                //ret = value;
            } else {
                //value = Integer.toString(maxMinuts);
                //value = "5"+num;
                value = "59";
            }

        }
        //return ret;
    }
*/


    public void switchPosition (){
        if (position == HORES){
            position = MINUTS;
            focusMinuts();

        } else {
            position = HORES;
            focusHores();
        }
    }

    public void switchPosicioInterna (){
        if (posInterna == UNITAT){
            posInterna = DECIMAL;


        } else {
            posInterna = UNITAT;

        }
    }




}
