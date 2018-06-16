package com.ramos.alvaro.horesajuntament2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
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




    private TextView tvHores, tvMinuts, tvDosPunts;
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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introhores);

        tvHores=(TextView)findViewById(R.id.tvHores);
        tvMinuts=(TextView)findViewById(R.id.tvMinuts);
        tvDosPunts=(TextView)findViewById(R.id.tvDosPunts);
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
        ompleHoraMinuts ();

/*
        posHores = true;
        posMinuts = true;
*/
        posInterna = UNITAT;
        position = HORES;

        itemTime.add(tvHores);
        itemTime.add(tvMinuts);

        tvHores.setTextColor(Color.BLUE);

        valueHores = "00";
        valueMin = "00";
        value = "00";

    }

    public void ompleHoraMinuts (){
        String hora;
        String min ;
        tvDosPunts.setText(".");
        String [] parts = horaStringOriginal.split(Pattern.quote("."));
        int i = parts.length;

        hora = parts[0];
        min= parts[1];

        tvHores.setText(hora);
        tvMinuts.setText(min);


    }


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

    public void fillEditText (String data, TextView tv){
        tv.setText(data);

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

    }


    public void button2 (View v){
        String num = "2";
        accionButton(num);
    }

    public void button3 (View v){
        String num = "3";
        accionButton(num);
    }

    public void button4 (View v){
        String num = "4";
        accionButton(num);
    }

    public void button5 (View v){
        String num = "5";
        accionButton(num);
    }

    public void button6 (View v){
        String num = "6";
        accionButton(num);
    }

    public void button7 (View v){
        String num = "7";
        accionButton(num);
    }

    public void button8 (View v){
        String num = "8";
        accionButton(num);
    }

    public void button9 (View v){
        String num = "9";
        accionButton(num);
    }

    public void button0 (View v){
        String num = "0";
        accionButton(num);
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
