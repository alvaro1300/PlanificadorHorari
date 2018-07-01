package com.ramos.alvaro.horesajuntament2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Alvaro on 06/06/2018.
 */

public class TempsActivity extends AppCompatActivity {

    public static final int HORES = 0;
    public static final int MINUTS = 1;
    public static final int UNITAT = 1;
    public static final int DECIMAL = 0;

    public static final String STATE_HORAMINUTS = "horaMinuts";
    public static final String STATE_TEMPS_SETMANAL = "tempsSetmanal";
    public static final String STATE_TEMPS_NOEFECT_VALIDACIO = "tempsPantallaCorrecte";
    public static final String STATE_DIFERENCIA = "diferencia";



    String colorAplicado = Constantes.COLOR_ERROR;

    private TextView tvHores, tvMinuts, tvDosPunts, tvDifTemps;
    private TextView tvTotalSemana;
    private Button btCancel, btOk, bt1, bt2,bt3, bt4 ,bt5, bt6, bt7, bt8, bt9;
    private TextView tvFocus = tvHores;
    private List<TextView> itemTime = new ArrayList<TextView>();
    private int position;
    private int posInterna;
    private String limitHorasSemana;
    boolean posHores;
    boolean posMinuts;
    /*
    String valueHores;
    String valueMin;
    */
    String value;

    int numero;

    String horaStringOriginal;
    String horaStringModificat=null;
    String totalSemanaOriginal;
    String totalSemanaModificat=null;

    Calendar tempsBase;
    String tempsBaseString;

    String totalDiaString;
    String intervalEntSort;
    String tipus; //Sirve para determinar si esta activity la ha llamado unos de los botones de noEfect o de efect
    //y asi poder determinar si hay que restar el tiempo de pantalla al tiempo total semanal o sumarlo

    Boolean tempsNoEfectCorrecte = true; //Sirve para poder detectar si hay que habilitar o no el boton de "OK" en funcion de si el tiempo introducido tiene logica


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

        super.onSaveInstanceState(savedInstanceState);
        //Almacenar los strings que hayamos introducido en pantalla para que al girar la vista se vuelvan a cargar
        savedInstanceState.putString(STATE_HORAMINUTS, horaStringModificat);
        savedInstanceState.putString(STATE_TEMPS_SETMANAL, totalSemanaModificat);
        savedInstanceState.putBoolean(STATE_TEMPS_NOEFECT_VALIDACIO, tempsNoEfectCorrecte);
        savedInstanceState.putString(STATE_DIFERENCIA, tvDifTemps.getText().toString());



    }

    @Override
    protected void onRestoreInstanceState(Bundle recuperaEstado){
        super.onRestoreInstanceState(recuperaEstado);
        horaStringModificat = recuperaEstado.getString(STATE_HORAMINUTS);
        totalSemanaModificat = recuperaEstado.getString(STATE_TEMPS_SETMANAL);
        tempsNoEfectCorrecte = recuperaEstado.getBoolean(STATE_TEMPS_NOEFECT_VALIDACIO);
        tvDifTemps.setText(recuperaEstado.getString(STATE_DIFERENCIA));

        ompleHoraMinuts(horaStringModificat);
        ompleTotalSetmana(totalSemanaModificat);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introhores);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvHores=(TextView)findViewById(R.id.tvHores);
        tvMinuts=(TextView)findViewById(R.id.tvMinuts);
        tvDosPunts=(TextView)findViewById(R.id.tvDosPunts);
        tvTotalSemana=(TextView)findViewById(R.id.tvTotalSemana);
        tvDifTemps=(TextView)findViewById(R.id.tvDifTemps);

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
        horaStringOriginal = bundle.getString("tempsStringOriginal");
        totalSemanaOriginal = bundle.getString("totalSemanaStringOriginal");
        limitHorasSemana = bundle.getString("limitHorasSemana");
        tipus = bundle.getString("tipus");


        ompleHoraMinuts(horaStringOriginal);
        ompleTotalSetmana(totalSemanaOriginal);

        horaStringModificat = horaStringOriginal;
        totalSemanaModificat = totalSemanaOriginal;

        tempsBaseString = determinarTempsBase();

        if(tipus.equals(Constantes.TIPUS_NOEFECT)){
            totalDiaString = bundle.getString(Constantes.TOTAL_DIA_STRING);
            intervalEntSort = determinarIntervalEntSort();
        }

/*
        posHores = true;
        posMinuts = true;
*/
        posInterna = UNITAT;
        position = HORES;

        itemTime.add(tvHores);
        itemTime.add(tvMinuts);

        tvHores.setTextColor(Color.BLUE);
/*
        valueHores = "00";
        valueMin = "00";
        */
        value = "00";

        String diferencia = calcularDiferencia(totalSemanaModificat);
        tvDifTemps.setText(diferencia);

    }

    public void ompleHoraMinuts (String valor){


        String hora;
        String min ;
        tvDosPunts.setText(".");
        String [] parts = valor.split(Pattern.quote("."));
        int i = parts.length;

        hora = parts[0];
        min= parts[1];

        tvHores.setText(hora);
        tvMinuts.setText(min);


    }

    public void ompleTotalSetmana(String valor){
        tvTotalSemana.setText(valor);
    }




    public String determinarIntervalEntSort(){
        String ret;

        Calendar intervalEntSortCal = Operacions.stringToCalendar(totalDiaString, Constantes.TIME_FORMAT2);

        String minString = tvMinuts.getText().toString();
        String horaString= tvHores.getText().toString();

        int minResDia = Integer.parseInt(minString);
        int horaResDia = Integer.parseInt(horaString);

        int dias;
        int horas;
        String minutes;

        intervalEntSortCal.add(Calendar.MINUTE, minResDia);
        intervalEntSortCal.add(Calendar.HOUR_OF_DAY, horaResDia);

        dias = intervalEntSortCal.get(Calendar.DAY_OF_MONTH);
        horas = intervalEntSortCal.get(Calendar.HOUR_OF_DAY);
        minutes = Operacions.calendarToString(intervalEntSortCal, Constantes.MINUTE_FORMAT);



        ret = 24*(dias-1) + horas+"."+minutes;


        return ret;
    }

    public String determinarTempsBase(){
        String ret;

        tempsBase = Operacions.stringToCalendar(totalSemanaOriginal, Constantes.TIME_FORMAT2);

        String minString = tvMinuts.getText().toString();
        String horaString= tvHores.getText().toString();

        int minResDia = Integer.parseInt(minString);
        int horaResDia = Integer.parseInt(horaString);

        int dias=1;
        int horas=1;
        String minutes = "1";

        if (tipus.equals(Constantes.TIPUS_EFECT)){
            tempsBase.add(Calendar.MINUTE, -minResDia);
            tempsBase.add(Calendar.HOUR_OF_DAY, -horaResDia);

            dias = tempsBase.get(Calendar.DAY_OF_MONTH);
            horas = tempsBase.get(Calendar.HOUR_OF_DAY);
            minutes = Operacions.calendarToString(tempsBase, Constantes.MINUTE_FORMAT);

        }else if (tipus.equals(Constantes.TIPUS_NOEFECT)){

            tempsBase.add(Calendar.MINUTE, minResDia);
            tempsBase.add(Calendar.HOUR_OF_DAY, horaResDia);

            dias = tempsBase.get(Calendar.DAY_OF_MONTH);
            horas = tempsBase.get(Calendar.HOUR_OF_DAY);
            minutes = Operacions.calendarToString(tempsBase, Constantes.MINUTE_FORMAT);

        }

        ret = 24*(dias-1) + horas+"."+minutes;


        return ret;
    }


    public String calcularTotalSetmana(){

        String ret=Constantes.ERROR;

        tempsBase = Operacions.stringToCalendar(tempsBaseString, Constantes.TIME_FORMAT2);

        String minString = tvMinuts.getText().toString();
        String horaString= tvHores.getText().toString();

        int minResDia = Integer.parseInt(minString);
        int horaResDia = Integer.parseInt(horaString);

        int dias=1;
        int horas=1;
        String minutes = "1";


        if (tipus.equals(Constantes.TIPUS_EFECT)){

            tempsBase.add(Calendar.MINUTE, minResDia);
            tempsBase.add(Calendar.HOUR_OF_DAY, horaResDia);

            dias = tempsBase.get(Calendar.DAY_OF_MONTH);
            horas = tempsBase.get(Calendar.HOUR_OF_DAY);
            minutes = Operacions.calendarToString(tempsBase, Constantes.MINUTE_FORMAT);

            ret = 24*(dias-1) + horas+"."+minutes;


        } else if (tipus.equals(Constantes.TIPUS_NOEFECT)){
            double tempNoEfect = Double.parseDouble(tvHores.getText().toString()+"."+tvMinuts.getText().toString());
            double tempsEfectDia = Double.parseDouble(intervalEntSort);

            if(tempsEfectDia>=tempNoEfect){
                tempsNoEfectCorrecte = true;
                //btOk.setBackgroundColor(getResources().getColor(R.color.azulBotonOk));
                //btOk.setTextColor();
                btOk.setEnabled(true);

                tempsBase.add(Calendar.MINUTE, -minResDia);
                tempsBase.add(Calendar.HOUR_OF_DAY, -horaResDia);

                dias = tempsBase.get(Calendar.DAY_OF_MONTH);
                horas = tempsBase.get(Calendar.HOUR_OF_DAY);
                minutes = Operacions.calendarToString(tempsBase, Constantes.MINUTE_FORMAT);

                ret = 24*(dias-1) + horas+"."+minutes;

            } else {
                tempsNoEfectCorrecte = false;
                //btOk.setBackgroundColor(getResources().getColor(R.color.grisDeshabilitado));
                //btOk.setTextColor(getResources().getColor(R.color.grisDeshabilitado));
                //tvTotalSemana.setText(Constantes.ERROR);
                btOk.setEnabled(false);
                ret = Constantes.ERROR;
            }
        }

        return ret;
    }


    public void fillEditText (String data, TextView tv){
        tv.setText(data);

    }

    public void calcularTot() {
        horaStringModificat= tvHores.getText().toString()+"."+tvMinuts.getText().toString();
        totalSemanaModificat=calcularTotalSetmana();
        tvTotalSemana.setText(totalSemanaModificat);


        if (totalSemanaModificat.equalsIgnoreCase(Constantes.ERROR)){
            tvDifTemps.setText("");
            colorAplicado = Constantes.COLOR_ERROR;

        }else {
            String diferencia = calcularDiferencia(totalSemanaModificat);
            tvDifTemps.setText(diferencia);

        }

        cambiarColores ();


    }



    public String calcularDiferencia (String resTotal){
        String diferencia;
        Calendar calLimSetmana = Calendar.getInstance();
        Calendar calTotalSetmana = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(Constantes.TIME_FORMAT2);
        try{
            //String limitHorasSemana = prefe.getString(Constantes.ARXIU_TITOL_LIMIT_HORAS, Constantes.LIMIT_HORES_SETMANA_DEF);
            //calLimSetmana.setTime(sdf.parse(Constantes.LIMIT_HORES_SETMANA_DEF));
            calLimSetmana.setTime(sdf.parse(limitHorasSemana));
            calTotalSetmana.setTime(sdf.parse(resTotal));
        } catch (ParseException ex) {

            //System.out.println("Error al convertir de string a calendar: " + data);
        }

        diferencia = convDifCalToTemps(calLimSetmana, calTotalSetmana);


        return diferencia;
    }

    public String convDifCalToTemps (Calendar calLimSetmana, Calendar calTotalSetmana){
        String temps;


        long millisLimSet = calLimSetmana.getTimeInMillis();
        long millisTotSet = calTotalSetmana.getTimeInMillis();

        Calendar difCal = Calendar.getInstance();

        int comparacio = calTotalSetmana.compareTo(calLimSetmana);


        long diff = millisTotSet - millisLimSet;


        difCal.setTimeInMillis(Math.abs(diff));
        long millisdifCal = difCal.getTimeInMillis();


        difCal.add(Calendar.HOUR_OF_DAY,-1);


        int dia = difCal.get(Calendar.DAY_OF_MONTH);

        int hora = difCal.get(Calendar.HOUR_OF_DAY);

        String minutes = Operacions.calendarToString(difCal, Constantes.MINUTE_FORMAT);

        int horasDif = 24*(dia-1) + hora;


        if(diff == 0L){
            temps = "0.00";
            colorAplicado = Constantes.COLOR_POSITIVO;
        } else if (diff>0L){

            colorAplicado = Constantes.COLOR_POSITIVO;
            temps = "+"+String.valueOf(horasDif)+"."+minutes;

        } else {
            colorAplicado = Constantes.COLOR_NEGATIVO;
            temps = "-"+String.valueOf(horasDif)+"."+minutes;

        }

        return temps;
    }

    public void cambiarColores (){
        //Cambiar el color del tvResult
        int color = Color.parseColor(colorAplicado);
        Drawable background = tvTotalSemana.getBackground();


        if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(color);
        }


    }




    /* ----------------------------BOTONES-----------------------------------------*/

    public void bAccept(View v){


            String dataRetorn= tvHores.getText().toString()+"."+tvMinuts.getText().toString();

            Intent intent=new Intent();
            intent.putExtra("temps", dataRetorn);

            setResult(RESULT_OK, intent);
            finish();




    }


    public void cancel (View v){

        Intent intent=new Intent();

        setResult(RESULT_CANCELED, intent);
        finish();
    }


    public void pressHores (View v){

        //position = 0;
        focusHores();
        //posInterna = UNITAT;

    }

    public void pressMinuts (View v){

        //position = 1;
        focusMinuts();


    }

    public void focusHores(){

        position = HORES;
        tvHores.setTextColor(Color.BLUE);
        tvMinuts.setTextColor(Color.BLACK);


    }

    public void focusMinuts(){

        position = MINUTS;
        posInterna = UNITAT;
        tvHores.setTextColor(Color.BLACK);
        tvMinuts.setTextColor(Color.BLUE);


    }


    public void button1 (View v){
        String num = "1";
        accionButton(num);
        /*
        horaStringModificat= tvHores.getText().toString()+"."+tvMinuts.getText().toString();
        totalSemanaModificat=calcularTotalSetmana();
        tvTotalSemana.setText(totalSemanaModificat);
        */
        calcularTot();

    }


    public void button2 (View v){
        String num = "2";
        accionButton(num);
        calcularTot();
    }

    public void button3 (View v){
        String num = "3";
        accionButton(num);
        calcularTot();
    }

    public void button4 (View v){
        String num = "4";
        accionButton(num);
        calcularTot();
    }

    public void button5 (View v){
        String num = "5";
        accionButton(num);
        calcularTot();
    }

    public void button6 (View v){
        String num = "6";
        accionButton(num);
        calcularTot();
    }

    public void button7 (View v){
        String num = "7";
        accionButton(num);
        calcularTot();
    }

    public void button8 (View v){
        String num = "8";
        accionButton(num);
        calcularTot();
    }

    public void button9 (View v){
        String num = "9";
        accionButton(num);
        calcularTot();
    }

    public void button0 (View v){
        String num = "0";
        accionButton(num);
        calcularTot();
    }




    public void accionButton (String numStr){
        String tempsString;
        String tempUnitat;
        String tempDecimal;

        //int temps;
        tempsString = itemTime.get(position).getText().toString();
        //temps = Integer.parseInt(tempsString);

        if (position == HORES){
            value = numStr;
            comprovaHores();
            itemTime.get(position).setText(value);
            //position = MINUTS;
            focusMinuts();

        } else if (position == MINUTS){
            if (posInterna == UNITAT){
                value = "0"+numStr;
                comprovaHores();
                itemTime.get(position).setText(value);
                posInterna = DECIMAL;
            } else if (posInterna == DECIMAL) {
                tempUnitat = tempsString.substring(1);
                value = tempUnitat + numStr;
                comprovaHores();
                itemTime.get(position).setText(value);
                //position = HORES;
                //posInterna = UNITAT;
                focusHores();
            }

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

        //comprovaHores();


        /*
        if (posInterna == DECIMAL || position == HORES){
            switchPosition ();
        } else if (position == MINUTS){
            switchPosicioInterna ();
        }
*/

    }



    public void comprovaHores(){
        //String ret = null;
        int maxMinuts = 59;
        int maxHores = 9;

        //Convierte a String a int
        numero = Integer.parseInt(value);

        if (position == HORES){
            if (numero>maxHores){
                //ret = value;
                value = "9";
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
