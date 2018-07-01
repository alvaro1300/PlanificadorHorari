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
    String entrada, salida, noEfect, efect, limitHorasSemana, tipus;

    boolean correcte = true;

    Calendar tempsBase;
    String tempsBaseString;

    int horaPantallaInt;

    Boolean tempsPantallaCorrecte = true; //Sirve para poder detectar si hay que habilitar o no el boton de "OK" en funcion de si el tiempo introducido tiene logica



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

        super.onSaveInstanceState(savedInstanceState);
        //Almacenar los strings que hayamos introducido en pantalla para que al girar la vista se vuelvan a cargar
        savedInstanceState.putString(STATE_HORAMINUTS, horaStringModificat);
        savedInstanceState.putString(STATE_TEMPS_SETMANAL, totalSemanaModificat);
        savedInstanceState.putBoolean(STATE_TEMPS_NOEFECT_VALIDACIO, tempsPantallaCorrecte);
        savedInstanceState.putString(STATE_DIFERENCIA, tvDifTemps.getText().toString());



    }

    @Override
    protected void onRestoreInstanceState(Bundle recuperaEstado){
        super.onRestoreInstanceState(recuperaEstado);
        horaStringModificat = recuperaEstado.getString(STATE_HORAMINUTS);
        totalSemanaModificat = recuperaEstado.getString(STATE_TEMPS_SETMANAL);
        tempsPantallaCorrecte= recuperaEstado.getBoolean(STATE_TEMPS_NOEFECT_VALIDACIO);
        tvDifTemps.setText(recuperaEstado.getString(STATE_DIFERENCIA));

        ompleHoraMinuts(horaStringModificat);
        ompleTotalSetmana(totalSemanaModificat);
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
        horaStringOriginal = bundle.getString("horaStringOriginal");
        totalSemanaOriginal = bundle.getString("totalSemanaStringOriginal");
        entrada = bundle.getString("entrada");
        salida = bundle.getString("salida");
        noEfect = bundle.getString("noEfect");
        efect =  bundle.getString("efect");
        limitHorasSemana =  bundle.getString("limitHorasSemana");
        tipus =  bundle.getString("tipus");

        if (tipus.equals(Constantes.TIPUS_ENTRADA)){
            this.setTitle(getString(R.string.title_hores_activity_entrada));
        } else if (tipus.equals(Constantes.TIPUS_SALIDA)){
            this.setTitle(getString(R.string.title_hores_activity_salida));
        }


        ompleHoraMinuts(horaStringOriginal);
        ompleTotalSetmana(totalSemanaOriginal);

        horaStringModificat = horaStringOriginal;
        totalSemanaModificat = totalSemanaOriginal;


        tempsBaseString = determinarTempsBase();

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

        String diferencia = calcularDiferencia(totalSemanaModificat);
        tvDifTemps.setText(diferencia);

    }

    public void calcularTot() {
        horaStringModificat= tvHores.getText().toString()+":"+tvMinuts.getText().toString();
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

    /**
     * Metodo para transformar un String con formato hh:mm a hh.mm y asi poderlo convertir a double sin problemas
     * @param horaConFormatDosPunts
     * @return
     */
    public String convertirStringDosPuntsAStringPunt (String horaConFormatDosPunts){

        String hora;
        String min ;
        String [] parts = horaConFormatDosPunts.split(":");
        hora = parts[0];
        min= parts[1];

        return hora + "." + min;

    }

    public String calcularTotalSetmana(){

        String ret=Constantes.ERROR;

        tempsBase = Operacions.stringToCalendar(tempsBaseString, Constantes.TIME_FORMAT2);

        String minString = tvMinuts.getText().toString();
        String horaString= tvHores.getText().toString();

        int minPantalla = Integer.parseInt(minString);
        int horaPantalla = Integer.parseInt(horaString);

        int dias=1;
        int horas=1;
        String minutes = "1";

/*
        Calendar entradaCal = Operacions.stringToCalendar(entrada, Constantes.HOUR_FORMAT);
        Calendar diferenciaEntreEntSort = Operacions.stringToCalendar(tempsBaseString, Constantes.TIME_FORMAT2);
        diferenciaEntreEntSort.add(Calendar.MINUTE, -entradaCal.get(Calendar.MINUTE));
        diferenciaEntreEntSort.add(Calendar.HOUR_OF_DAY, -entradaCal.get(Calendar.HOUR_OF_DAY));
*/
        Calendar diferenciaEntreEntSort = null;


        if (tipus.equals(Constantes.TIPUS_ENTRADA)) {

            //Si estamos estableciendo la hora Entrada, comprovamos que la hora de pantalla no sea mayor que la hora de Salida
            Double tempsHoraEntrada = Double.parseDouble(tvHores.getText().toString() + "." + tvMinuts.getText().toString());
            Double tempsHoraSalida = Double.parseDouble(convertirStringDosPuntsAStringPunt(salida));
            if (tempsHoraEntrada < tempsHoraSalida) {
                tempsPantallaCorrecte = true;
                btOk.setEnabled(true);
                Calendar entradaCal = Operacions.stringToCalendar(tvHores.getText().toString() + ":" + tvMinuts.getText().toString(), Constantes.HOUR_FORMAT);
                diferenciaEntreEntSort = Operacions.stringToCalendar(salida, Constantes.HOUR_FORMAT);
                diferenciaEntreEntSort.add(Calendar.MINUTE, -entradaCal.get(Calendar.MINUTE));
                diferenciaEntreEntSort.add(Calendar.HOUR_OF_DAY, -entradaCal.get(Calendar.HOUR_OF_DAY));

            } else {
                tempsPantallaCorrecte = false;
                btOk.setEnabled(false);
                ret = Constantes.ERROR;
            }
        }

        if (tipus.equals(Constantes.TIPUS_SALIDA)) {

            //Si estamos estableciendo la entrada, comprovamos que la hora de pantalla no sea mayor que la hora de salida
            Double tempsHoraSalida = Double.parseDouble(tvHores.getText().toString() + "." + tvMinuts.getText().toString());
            Double tempsHoraEntrada = Double.parseDouble(convertirStringDosPuntsAStringPunt(entrada));
            if (tempsHoraSalida > tempsHoraEntrada) {
                tempsPantallaCorrecte = true;
                btOk.setEnabled(true);
                Calendar entradaCal = Operacions.stringToCalendar(entrada, Constantes.HOUR_FORMAT);
                String pantalla = tvHores.getText().toString() + ":" + tvMinuts.getText().toString();
                diferenciaEntreEntSort = Operacions.stringToCalendar(pantalla, Constantes.HOUR_FORMAT);
                diferenciaEntreEntSort.add(Calendar.MINUTE, -entradaCal.get(Calendar.MINUTE));
                diferenciaEntreEntSort.add(Calendar.HOUR_OF_DAY, -entradaCal.get(Calendar.HOUR_OF_DAY));



            } else {
                tempsPantallaCorrecte = false;
                btOk.setEnabled(false);
                ret = Constantes.ERROR;
            }
        }

        //Si la hora introducida es correcta, entonces hacemos los calculos
        if(tempsPantallaCorrecte) {

            tempsBase.add(Calendar.MINUTE, diferenciaEntreEntSort.get(Calendar.MINUTE));
            tempsBase.add(Calendar.HOUR_OF_DAY,diferenciaEntreEntSort.get(Calendar.HOUR_OF_DAY));

            /*
            tempsBase.add(Calendar.MINUTE, minPantalla);
            tempsBase.add(Calendar.HOUR_OF_DAY,horaPantalla);
*/
            dias = tempsBase.get(Calendar.DAY_OF_MONTH);
            horas = tempsBase.get(Calendar.HOUR_OF_DAY);
            minutes = Operacions.calendarToString(tempsBase, Constantes.MINUTE_FORMAT);

            ret = 24*(dias-1) + horas+"."+minutes;
        }
        return ret;
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





    /**
     * Encontramos el total de la semana base al que le sumaremos posteriormente las horas de ese dia
     * @return
     */
    public String determinarTempsBase(){
        String ret;

        tempsBase = Operacions.stringToCalendar(totalSemanaOriginal, Constantes.TIME_FORMAT2);


        int dias=1;
        int horas=1;
        String minutes = "1";

        //Encuentro la diferencia de tiempo en Calendar entre la hora de entrada y la hora de salida
        Calendar entradaCal = Operacions.stringToCalendar(entrada, Constantes.HOUR_FORMAT);

        Calendar diferenciaEntreEntSort = Operacions.stringToCalendar(salida, Constantes.HOUR_FORMAT);
        int minut = entradaCal.get(Calendar.MINUTE);
        int hor = entradaCal.get(Calendar.HOUR_OF_DAY);
        diferenciaEntreEntSort.add(Calendar.MINUTE, -entradaCal.get(Calendar.MINUTE));
        diferenciaEntreEntSort.add(Calendar.HOUR_OF_DAY, -entradaCal.get(Calendar.HOUR_OF_DAY));

        //Resto la diferencia de tiempo en Calendar al tempsBase para encontrar el tempBase de verdad
        tempsBase.add(Calendar.MINUTE, -diferenciaEntreEntSort.get(Calendar.MINUTE));
        tempsBase.add(Calendar.HOUR_OF_DAY, -diferenciaEntreEntSort.get(Calendar.HOUR_OF_DAY));

        //Obtengo los dias, horas i minutos del tiempo base
        dias = tempsBase.get(Calendar.DAY_OF_MONTH);
        horas = tempsBase.get(Calendar.HOUR_OF_DAY);
        minutes = Operacions.calendarToString(tempsBase, Constantes.MINUTE_FORMAT);

        ret = 24*(dias-1) + horas+"."+minutes;


        return ret;
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

        String dataRetorn= tvHores.getText().toString()+":"+tvMinuts.getText().toString();


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


    public void button1 (View v){
        String num = "1";
        accionButton(num);
        //horaStringModificat= tvHores.getText().toString()+":"+tvMinuts.getText().toString();
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
