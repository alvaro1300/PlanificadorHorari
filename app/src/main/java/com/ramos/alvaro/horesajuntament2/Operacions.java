package com.ramos.alvaro.horesajuntament2;


import android.support.v7.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Alvaro on 16/06/2018.
 */

public class Operacions extends AppCompatActivity {


    public static double tempsStringToInt (String temps){
        double tempsInt;

        tempsInt = Double.parseDouble(temps);


        return tempsInt;
    }

    /**
     * Mètode per convertir un data en format String en un objecte Calendar
     *
     * @param data Data en format String
     * @return Objecte Calendar
     */
    public static  Calendar stringToCalendar(String data, String format) {
        Calendar cal = null;

        try {

            cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            cal.setTime(sdf.parse(data));
            System.out.println("La hora es: " + cal.toString());

        } catch (ParseException ex) {
            //ex.printStackTrace(System.out);
            //throw new RuntimeException(ex.getMessage());
            System.out.println("Error al convertir de string a calendar: " + data);
        }

        return cal;

    }

    /**
     * Mètode per convertir un objecte Calendar en una data en format String
     *
     * @param cal Objecte Calendar a convertir a String
     * @return Data en format String
     */
    public static String calendarToString(Calendar cal, String format) {
        String data = null;
        if (cal != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            data = sdf.format(cal.getTime());
        }

        return data;

    }





}
