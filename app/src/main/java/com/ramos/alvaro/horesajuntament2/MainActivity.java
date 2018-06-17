package com.ramos.alvaro.horesajuntament2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    String colorAplicado = Constantes.COLOR_ERROR;

    private TextView tvDlEnt, tvDlSort, tvDlNoEfec, tvDlEfec;
    private TextView tvDmEnt, tvDmSort, tvDmNoEfec, tvDmEfec;
    private TextView tvDcEnt, tvDcSort, tvDcNoEfec, tvDcEfec;
    private TextView tvDjEnt, tvDjSort, tvDjNoEfec, tvDjEfec;
    private TextView tvDvEnt, tvDvSort, tvDvNoEfec, tvDvEfec;
    private TextView tvDsEnt, tvDsSort, tvDsNoEfec, tvDsEfec;
    private TextView tvDgEnt, tvDgSort, tvDgNoEfec, tvDgEfec;
    private TextView tvResult;
    private TextView tvDiferencia;
    private TableRow trDs, trDg;
    
    private CheckBox cbDl, cbDm, cbDc, cbDj, cbDv, cbDs, cbDg;
    private CheckBox cbSeleccionat;
    
    private List<TextView> listaDl;
    private List<TextView> listaDm;
    private List<TextView> listaDc;
    private List<TextView> listaDj;
    private List<TextView> listaDv;
    private List<TextView> listaDs;
    private List<TextView> listaDg;

    private List<TextView> listaHoresDiaEfec;
    private List<List<TextView>> listaSetmana;
    private List<TextView> listaResDia;
    private List<CheckBox> listaCheckBoxFestius;
    List <String[]> listDiaSetmana; //Lista con 7 posiciones para guardar 3 String en cada posicion con el nombre de un campo para los archivos sharedPreferences

    String hora;
    String tempsNoEfect;
    String tempsEfect = "7.21";
    int id;
    TextView itemSel;
    int limitDies;
    boolean verFinde = false;
    //String[] arrayCampsArxiuDades;


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

        super.onSaveInstanceState(savedInstanceState);
        //Almacenar la id del view selecionado cuando se toca sobre un numero de la pantalla
        //para editarlo
        savedInstanceState.putInt(Constantes.STATE_ID_VIEW, id);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            id = savedInstanceState.getInt(Constantes.STATE_ID_VIEW);
        }


        setContentView(R.layout.activity_main);

        listaResDia = new ArrayList<>();
        crearListDl();
        crearListDm();
        crearListDc();
        crearListDj();
        crearListDv();
        crearListDs();
        crearListDg();
        crearListCheckBoxFestius();
        crearListDiesSetmana();

        listaSetmana = new ArrayList<>();
        listaSetmana.add(listaDl);
        listaSetmana.add(listaDm);
        listaSetmana.add(listaDc);
        listaSetmana.add(listaDj);
        listaSetmana.add(listaDv);
        listaSetmana.add(listaDs);
        listaSetmana.add(listaDg);
        //arrayCampsArxiuDades = arrayCampsArxiuDades();




        //Instanciamos para poder ocultarlas luego
        trDs = (TableRow)findViewById(R.id.trDs);
        trDg = (TableRow)findViewById(R.id.trDg);

        tvResult = (TextView)findViewById(R.id.tvResultat);
        tvDiferencia = (TextView)findViewById(R.id.tvDiferencia);


        carregarDades(Constantes.ARXIU_DADES1);
        visivilidadFinde();
        calcularTot();
        cambiarColores ();


    }

    public void calcularTot(){

        /*
        if(trDs.getVisibility()==View.VISIBLE){
            limitDies=7;
        } else {
            limitDies=5;
        }
*/

        //Recorremos la listaSetmana que contiene los 7 dias de la semana con 4 valores por cada dia
        //Se limita el recorrido dependiendo si se esta mostrando el finde o no.
        for (int diaSetmana=0; diaSetmana<limitDies; diaSetmana++){
            List<TextView> listaValorsDia = listaSetmana.get(diaSetmana);

            //TextView resDia = listaResDia.get(3);
            TextView resDia = listaValorsDia.get(Constantes.TOTAL_DIA); //Obtenemos el TextView del resultado del dia

            String ent = listaValorsDia.get(Constantes.ENTRADA).getText().toString();
            String sort = listaValorsDia.get(Constantes.SORTIDA).getText().toString();
            String noEfect = listaValorsDia.get(Constantes.TEMPS_NOEFEC).getText().toString();
            //String efect = listaValorsDia.get(3).getText().toString();

            String resultat = calcularDia(ent, sort, noEfect, diaSetmana);
            resDia.setText(resultat);
        }

        String resTotalSetString =  calcularTotalSetmana();
        tvResult.setText(resTotalSetString);

        if (resTotalSetString.equalsIgnoreCase(Constantes.ERROR)){
            tvDiferencia.setText("");
            colorAplicado = Constantes.COLOR_ERROR;

        }else {
            String diferencia = calcularDiferencia(resTotalSetString);
            tvDiferencia.setText(diferencia);

        }

        cambiarColores ();
        visivilidadFinde();

    }

    public String calcularDia (String ent, String sort, String noEfect, int diaSetmana){
        String ret= Constantes.ERROR;

        //Obtenemos el objeto CheckBox en funcion del dia de la semana
        CheckBox checkBoxDelDia = listaCheckBoxFestius.get(diaSetmana);
        boolean checked = false;
        checked = checkBoxDelDia.isChecked();



        //Cambiamos el "-" que muestra el textview noEfect por el valor "0.00" en caso que sea necesario
        if(noEfect.equals(Constantes.NOVALUE)){
           noEfect = Constantes.TIME_VALUE_0;
        }

        if(checked){

            ret= Constantes.TEMPS_EFECTIU_DIA_FESTIU;

        }else {

            if (ent.equals(Constantes.NOVALUE) || sort.equals(Constantes.NOVALUE)){



                ret=listaSetmana.get(diaSetmana).get(Constantes.TOTAL_DIA).getText().toString(); //Este valor es el que retorna el intent del activity que se
                // abre para cambiar directamente el valor del temps Efectiu del Dia

            }else{
                Calendar calEnt= Operacions.stringToCalendar(ent, Constantes.HOUR_FORMAT);
                Calendar calSort= Operacions.stringToCalendar(sort, Constantes.HOUR_FORMAT);
                //Calendar calNoEfect= stringToCalendar(noEfect, TIME_FORMAT);

                int minEnt = calEnt.get(Calendar.MINUTE);
                int horaEnt = calEnt.get(Calendar.HOUR_OF_DAY);

                String[] arrayNoEfec = noEfect.split(Pattern.quote("."));
                int minNoEfect = Integer.parseInt(arrayNoEfec[1]);
                int horaNoEfect = Integer.parseInt(arrayNoEfec[0]);

                int comparacio = calSort.compareTo(calEnt);

                if (comparacio >= 0) {
                    calSort.add(Calendar.MINUTE, -minEnt);
                    calSort.add(Calendar.HOUR_OF_DAY, -horaEnt);

                    calSort.add(Calendar.MINUTE, -minNoEfect);
                    calSort.add(Calendar.HOUR_OF_DAY, -horaNoEfect);

                    ret = Operacions.calendarToString(calSort, Constantes.TIME_FORMAT);

                    //System.out.println("La hora resultat es: " + res);


                } else {
                    //System.out.println("La hora sortida no pot ser mes petita que hora entrada");

                    Toast.makeText(this,"La hora de salida no se puede producir antes que la hora de entrada",Toast.LENGTH_LONG).show();
                }
            }
        }

        return ret;
    }



    public String calcularTotalSetmana(){

        String resTotal = Constantes.ERROR;
        boolean allDataOk = true;
        Calendar calRes = Operacions.stringToCalendar("00.00", Constantes.TIME_FORMAT2);


        for (int i = 0; i < limitDies; i++) {
            String resDia= listaResDia.get(i).getText().toString();
            if (resDia.equalsIgnoreCase(Constantes.ERROR)){
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
            String minutes = Operacions.calendarToString(calRes, Constantes.MINUTE_FORMAT);

            resTotal = 24*(dias-1) + horas+"."+minutes;
        }

        return resTotal;
    }


    public String calcularDiferencia (String resTotal){
        String diferencia;
        Calendar calLimSetmana = Calendar.getInstance();
        Calendar calTotalSetmana = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(Constantes.TIME_FORMAT2);
        try{
            calLimSetmana.setTime(sdf.parse(Constantes.LIMIT_HORES_SETMANA));
            calTotalSetmana.setTime(sdf.parse(resTotal));
        } catch (ParseException ex) {

            //System.out.println("Error al convertir de string a calendar: " + data);
        }

        diferencia = convDifCalToTemps(calLimSetmana, calTotalSetmana);


        return diferencia;
    }



    public String convDifCalToTemps (Calendar calLimSetmana, Calendar calTotalSetmana){
        String temps;

        System.out.println("Alvaro calLimSetmana: "+calLimSetmana.toString());
        System.out.println("Alvaro calTotalSetmana: "+calTotalSetmana.toString());

        long millisLimSet = calLimSetmana.getTimeInMillis();
        long millisTotSet = calTotalSetmana.getTimeInMillis();

        Calendar difCal = Calendar.getInstance();
        /*
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT2);
        try{
        difCal.setTime(sdf.parse("00.00"));
        } catch (ParseException ex) {
            //System.out.println("Error al convertir de string a calendar: " + data);
        }
        */
        int comparacio = calTotalSetmana.compareTo(calLimSetmana);



        long diff = millisTotSet - millisLimSet;
        System.out.println("Alvaro diff: "+diff);

        difCal.setTimeInMillis(Math.abs(diff));
        long millisdifCal = difCal.getTimeInMillis();
        System.out.println("Alvaro millisdifCal: "+millisdifCal);

        difCal.add(Calendar.HOUR_OF_DAY,-1);
        System.out.println("Alvaro difCal: " +difCal.toString());

        int dia = difCal.get(Calendar.DAY_OF_MONTH);
        System.out.println("Alvaro dias: "+dia);
        int hora = difCal.get(Calendar.HOUR_OF_DAY);
        System.out.println("Alvaro hora: "+hora);
        String minutes = Operacions.calendarToString(difCal, Constantes.MINUTE_FORMAT);
        System.out.println("Alvaro minutes: "+minutes);
        int horasDif = 24*(dia-1) + hora;
        System.out.println("Alvaro horasDif: "+horasDif);

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
        Drawable background = tvResult.getBackground();


        if (background instanceof GradientDrawable) {
            // cast to 'GradientDrawable'
            GradientDrawable gradientDrawable = (GradientDrawable) background;
            gradientDrawable.setColor(color);
        }

        for (int i=0; i<7; i++){
            List listaSet = listaSetmana.get(i);
            TextView tv =(TextView) listaSet.get(Constantes.TEMPS_NOEFEC);
            String tvString = tv.getText().toString();
            if(tvString.equals("0.00")){
                tv.setTextColor(Color.parseColor(Constantes.COLOR_TEXTO_CLARO));
            } else {
                tv.setTextColor(Color.GRAY);
            }

        }

    }

    public void visivilidadFinde(){
        if(verFinde){
            mostrarFinde();
        } else {
            ocultarFinde();
        }

    }

    public void mostrarFinde(){
        trDs.setVisibility(View.VISIBLE);
        trDg.setVisibility(View.VISIBLE);
        limitDies=7;
    }

    public void ocultarFinde(){
        trDs.setVisibility(View.GONE);
        trDg.setVisibility(View.GONE);
        limitDies=5;
    }





    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
        guardarDades (Constantes.ARXIU_DADES1);

    }






    /**
     * Compara el id del View que se guardó antes de cambiar de pantalla con la lista de TextView
     * de cada dia de la semana. Cuando lo encuentra guarda el TextView en la variable itemSel
     * para poder realizar los cambios que ha devuelto el intent
     */
    public void recuperarTextViewPerId(){
        //ArrayList<TextView> listDay;

        boolean sortir = false;
        for(List<TextView> listDay: listaSetmana){

            for (TextView item: listDay){
                if(item.getId()==id){
                    itemSel = item;
                    sortir = true;
                    break;
                }
            }
            if (sortir){
                break;
            }


        }
    }



    public int trobarDiaSetPerTextViewClicat (View v){
        int diaSetmana=0;
        for (int i=0; i<limitDies;i++){
            List<TextView> listaValoresDia = listaSetmana.get(i);
            for (TextView itemSel: listaValoresDia){
                if(v.getId()==itemSel.getId()){
                    diaSetmana = i;
                    i = limitDies;
                    break;
                }
            }
        }

        return diaSetmana;
    }

    public int trobarDiaSetPerTextViewClicat (int id){
        int diaSetmana=0;
        for (int i=0; i<limitDies;i++){
            List<TextView> listaValoresDia = listaSetmana.get(i);
            for (TextView itemSel: listaValoresDia){
                if(id==itemSel.getId()){
                    diaSetmana = i;
                    i = limitDies;
                    break;
                }
            }
        }

        return diaSetmana;
    }



    /* ----------------------------BOTONES-----------------------------------------*/

    public void bAccionEntSort(View v){


        //Obtenemos el dia de la semana para poder encontrar su CheckBox correspondiente
        int diaSetmana = trobarDiaSetPerTextViewClicat(v);

        //Obtenemos el objeto CheckBox en funcion del dia de la semana
        CheckBox checkBoxDelDia = listaCheckBoxFestius.get(diaSetmana);
        boolean checked;
        checked = checkBoxDelDia.isChecked();



        TextView tvSel = (TextView)v;
        String tvSelString = tvSel.getText().toString();
        String totalSemana = tvResult.getText().toString();

        if (!checked){
            if(!tvSelString.equals(Constantes.NOVALUE)){

                id = v.getId(); //Guardamos la id del view clickado para que al volver a esta activity
                //se sepa que boton lanzó el intent . Esto se harà gracias al onSaveInstanceState

                Intent i = new Intent(this, HoresActivity.class);
                i.putExtra("horaStringOriginal", tvSelString);
                i.putExtra("totalSemanaStringOriginal", totalSemana);

                startActivityForResult(i,1);
            } else {
                List<TextView> listaValorsDia = listaSetmana.get(diaSetmana);
                listaValorsDia.get(Constantes.ENTRADA).setText(Constantes.HORA_ENT_DEFECTE);
                listaValorsDia.get(Constantes.SORTIDA).setText(Constantes.HORA_SORT_DEFECTE);
                listaValorsDia.get(Constantes.TEMPS_NOEFEC).setText(Constantes.NOVALUE);
                calcularTot();


            }

        }



    }

    public void bAccionNoEfect(View v){

        //Obtenemos el dia de la semana para poder encontrar su CheckBox correspondiente
        int diaSetmana = trobarDiaSetPerTextViewClicat(v);

        //Obtenemos el objeto CheckBox en funcion del dia de la semana
        CheckBox checkBoxDelDia = listaCheckBoxFestius.get(diaSetmana);
        boolean checked;
        checked = checkBoxDelDia.isChecked();

        TextView tvSel = (TextView)v;
        String tvSelString = tvSel.getText().toString();

        String totalSemana = tvResult.getText().toString();

        if(!checked){
            if(!tvSelString.equals(Constantes.NOVALUE)){
                id = v.getId();

                Intent i = new Intent(this, TempsActivity.class);
                i.putExtra("tempsStringOriginal", tvSelString);
                i.putExtra("totalSemanaStringOriginal", totalSemana);

                startActivityForResult(i,2);

            } else{
                id = v.getId();

                List<TextView> listaValorsDia = listaSetmana.get(diaSetmana);
                String stringEnt = listaValorsDia.get(Constantes.ENTRADA).getText().toString();
                String stringSort = listaValorsDia.get(Constantes.SORTIDA).getText().toString();

                if(stringEnt.equals(Constantes.NOVALUE) || stringSort.equals(Constantes.NOVALUE)){
                    listaValorsDia.get(Constantes.ENTRADA).setText(Constantes.HORA_ENT_DEFECTE);
                    listaValorsDia.get(Constantes.SORTIDA).setText(Constantes.HORA_SORT_DEFECTE);
                    calcularTot();

                } else {
                    Intent i = new Intent(this, TempsActivity.class);
                    i.putExtra("tempsStringOriginal", Constantes.TIME_VALUE_0);
                    i.putExtra("totalSemanaStringOriginal", totalSemana);
                    startActivityForResult(i,2);
                }

            }
        }
    }


    public void bAccionEfect(View v){

        //Obtenemos el dia de la semana para poder encontrar su CheckBox correspondiente
        int diaSetmana = trobarDiaSetPerTextViewClicat(v);

        //Obtenemos el objeto CheckBox en funcion del dia de la semana
        CheckBox checkBoxDelDia = listaCheckBoxFestius.get(diaSetmana);
        boolean checked;
        checked = checkBoxDelDia.isChecked();

        TextView tvSel = (TextView)v;
        String tvSelString = tvSel.getText().toString();

        String totalSemana = tvResult.getText().toString();

        if(!checked){

            id = v.getId();
            if(tvSelString.equals(Constantes.ERROR)){

                tvSel.setText(Constantes.TEMPS_EFECTIU_DIA_DEFECTE);
                listaSetmana.get(diaSetmana).get(Constantes.ENTRADA).setText(Constantes.NOVALUE);
                listaSetmana.get(diaSetmana).get(Constantes.SORTIDA).setText(Constantes.NOVALUE);
                listaSetmana.get(diaSetmana).get(Constantes.TEMPS_NOEFEC).setText(Constantes.NOVALUE);
                calcularTot();
                totalSemana = tvResult.getText().toString();
                tvSelString = tvSel.getText().toString();
            }

            Intent i = new Intent(this, TempsActivity.class);
            i.putExtra("tempsStringOriginal", tvSelString);
            i.putExtra("totalSemanaStringOriginal", totalSemana);

            startActivityForResult(i,3);

        }
    }




    public void bAccionOcultarFinde(View v){
        if (trDs.getVisibility()==View.GONE){
            mostrarFinde();
            verFinde = true;

        } else {
            ocultarFinde();
            verFinde = false;

        }
        calcularTot();
    }


    public void bAccionGuardarDadesModel(View v){
        guardarDades(Constantes.ARXIU_DADES_MODEL);
    }

    public void bAccionCarregarDadesModel(View v){

        carregarDades (Constantes.ARXIU_DADES_MODEL);
        calcularTot();
    }


   // public int obtenirDiaSetmana

    public void bAccionFestiu (View v){


        //comprovar si esta checked
        cbSeleccionat = (CheckBox)v;
        boolean checked = cbSeleccionat.isChecked();

        int cbId = cbSeleccionat.getId();

        for (int i=0;i<listaCheckBoxFestius.size();i++){
            if(cbId == listaCheckBoxFestius.get(i).getId()){
                List<TextView> diaSelecionat = listaSetmana.get(i);
                if(checked){
                    //Guardamos los datos si no esta checked para poder recuperarlos al desmarcar
                    guardarUnDia(Constantes.ARXIU_DADES_FESTIU,i);

                    diaSelecionat.get(Constantes.ENTRADA).setText(Constantes.NOVALUE);
                    diaSelecionat.get(Constantes.SORTIDA).setText(Constantes.NOVALUE);
                    diaSelecionat.get(Constantes.TEMPS_NOEFEC).setText(Constantes.NOVALUE);



                }else {
                    carregarUnDia(Constantes.ARXIU_DADES_FESTIU,i);

                    /*
                    SharedPreferences prefe = getSharedPreferences(ARXIU_DADES_FESTIU, Context.MODE_PRIVATE);

                    String[] dia = listDiaSetmana.get(i);


                    diaSelecionat.get(ENTRADA).setText(prefe.getString(dia[0], "09:00"));
                    diaSelecionat.get(SORTIDA).setText(prefe.getString(dia[1], "15:25"));
                    diaSelecionat.get(TEMPS_NOEFEC).setText(prefe.getString(dia[2], "0.00"));
*/

                }
                break;
            }


        }

        calcularTot();

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        recuperarTextViewPerId();
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                hora=data.getStringExtra("temps");
                fillTextValues(hora,itemSel);

            }

        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {

                tempsNoEfect = data.getStringExtra("temps");

                //Si retorna "0.00" se mostrara en pantalla "-"
                if(tempsNoEfect.equals(Constantes.TIME_VALUE_0)){
                    fillTextValues(Constantes.NOVALUE, itemSel);

                }else{
                    fillTextValues(tempsNoEfect, itemSel);
                }
            }

        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {

                tempsEfect = data.getStringExtra("temps");
                fillTextValues(tempsEfect, itemSel);
                int diaSetmana = trobarDiaSetPerTextViewClicat(itemSel);
                listaSetmana.get(diaSetmana).get(Constantes.ENTRADA).setText(Constantes.NOVALUE);
                listaSetmana.get(diaSetmana).get(Constantes.SORTIDA).setText(Constantes.NOVALUE);
                listaSetmana.get(diaSetmana).get(Constantes.TEMPS_NOEFEC).setText(Constantes.NOVALUE);


            } else if (resultCode == RESULT_CANCELED){


            }

        }

        calcularTot();
    }


    public void fillTextValues (String data, TextView tv){
        if (data.equals("")){
            //tv.setText(getString(R.string.no_value));
        }
        else {
            tv.setText(data);
        }
    }


    /*--------------------------CREAR LISTAS de VIEWs------------------------*/

    public void crearListDl(){
        listaDl = new ArrayList<>();
        tvDlEnt = (TextView)findViewById(R.id.tvDlEnt);
        tvDlSort = (TextView)findViewById(R.id.tvDlSort);
        tvDlNoEfec = (TextView)findViewById(R.id.tvDlNoEfec);
        tvDlEfec = (TextView)findViewById(R.id.tvDlEfect);
        listaDl.add(tvDlEnt);
        listaDl.add(tvDlSort);
        listaDl.add(tvDlNoEfec);
        listaDl.add(tvDlEfec);

        //Añadimos tambien el tiempoEfectivo de este dia a otra List
        listaResDia.add(tvDlEfec);

    }

    public void crearListDm(){
        listaDm = new ArrayList<>();
        tvDmEnt = (TextView)findViewById(R.id.tvDmEnt);
        tvDmSort = (TextView)findViewById(R.id.tvDmSort);
        tvDmNoEfec = (TextView)findViewById(R.id.tvDmNoEfec);
        tvDmEfec = (TextView)findViewById(R.id.tvDmEfect);
        listaDm.add(tvDmEnt);
        listaDm.add(tvDmSort);
        listaDm.add(tvDmNoEfec);
        listaDm.add(tvDmEfec);

        //Añadimos tambien el tiempoEfectivo de este dia a otra List
        listaResDia.add(tvDmEfec);
    }

    public void crearListDc(){
        listaDc = new ArrayList<>();
        tvDcEnt = (TextView)findViewById(R.id.tvDcEnt);
        tvDcSort = (TextView)findViewById(R.id.tvDcSort);
        tvDcNoEfec = (TextView)findViewById(R.id.tvDcNoEfec);
        tvDcEfec = (TextView)findViewById(R.id.tvDcEfect);
        listaDc.add(tvDcEnt);
        listaDc.add(tvDcSort);
        listaDc.add(tvDcNoEfec);
        listaDc.add(tvDcEfec);

        //Añadimos tambien el tiempoEfectivo de este dia a otra List
        listaResDia.add(tvDcEfec);

    }

    public void crearListDj(){
        listaDj = new ArrayList<>();
        tvDjEnt = (TextView)findViewById(R.id.tvDjEnt);
        tvDjSort = (TextView)findViewById(R.id.tvDjSort);
        tvDjNoEfec = (TextView)findViewById(R.id.tvDjNoEfec);
        tvDjEfec = (TextView)findViewById(R.id.tvDjEfect);
        listaDj.add(tvDjEnt);
        listaDj.add(tvDjSort);
        listaDj.add(tvDjNoEfec);
        listaDj.add(tvDjEfec);

        //Añadimos tambien el tiempoEfectivo de este dia a otra List
        listaResDia.add(tvDjEfec);

    }

    public void crearListDv(){
        listaDv = new ArrayList<>();
        tvDvEnt = (TextView)findViewById(R.id.tvDvEnt);
        tvDvSort = (TextView)findViewById(R.id.tvDvSort);
        tvDvNoEfec = (TextView)findViewById(R.id.tvDvNoEfec);
        tvDvEfec = (TextView)findViewById(R.id.tvDvEfect);
        listaDv.add(tvDvEnt);
        listaDv.add(tvDvSort);
        listaDv.add(tvDvNoEfec);
        listaDv.add(tvDvEfec);

        //Añadimos tambien el tiempoEfectivo de este dia a otra List
        listaResDia.add(tvDvEfec);

    }

    public void crearListDs(){
        listaDs = new ArrayList<>();
        tvDsEnt = (TextView)findViewById(R.id.tvDsEnt);
        tvDsSort = (TextView)findViewById(R.id.tvDsSort);
        tvDsNoEfec = (TextView)findViewById(R.id.tvDsNoEfec);
        tvDsEfec = (TextView)findViewById(R.id.tvDsEfect);
        listaDs.add(tvDsEnt);
        listaDs.add(tvDsSort);
        listaDs.add(tvDsNoEfec);
        listaDs.add(tvDsEfec);

        //Añadimos tambien el tiempoEfectivo de este dia a otra List
        listaResDia.add(tvDsEfec);

    }

    public void crearListDg(){
        listaDg = new ArrayList<>();
        tvDgEnt = (TextView)findViewById(R.id.tvDgEnt);
        tvDgSort = (TextView)findViewById(R.id.tvDgSort);
        tvDgNoEfec = (TextView)findViewById(R.id.tvDgNoEfec);
        tvDgEfec = (TextView)findViewById(R.id.tvDgEfect);
        listaDg.add(tvDgEnt);
        listaDg.add(tvDgSort);
        listaDg.add(tvDgNoEfec);
        listaDg.add(tvDgEfec);

        //Añadimos tambien el tiempoEfectivo de este dia a otra List
        listaResDia.add(tvDgEfec);

    }
    
    public void crearListCheckBoxFestius(){
        listaCheckBoxFestius = new ArrayList<>();
        cbDl = (CheckBox)findViewById(R.id.checkDlFest);
        cbDm = (CheckBox)findViewById(R.id.checkDmFest);
        cbDc = (CheckBox)findViewById(R.id.checkDcFest);
        cbDj = (CheckBox)findViewById(R.id.checkDjFest);
        cbDv = (CheckBox)findViewById(R.id.checkDvFest);
        cbDs = (CheckBox)findViewById(R.id.checkDsFest);
        cbDg = (CheckBox)findViewById(R.id.checkDgFest);
        listaCheckBoxFestius.add(cbDl);
        listaCheckBoxFestius.add(cbDm);
        listaCheckBoxFestius.add(cbDc);
        listaCheckBoxFestius.add(cbDj);
        listaCheckBoxFestius.add(cbDv);
        listaCheckBoxFestius.add(cbDs);
        listaCheckBoxFestius.add(cbDg);
        
    }

    public void crearListDiesSetmana(){
        listDiaSetmana = new ArrayList<>();

        String[] arrayDlNomCamps = {"DlEnt", "DlSort","DlNoEfec"};
        String[] arrayDmNomCamps = {"DmEnt", "DmSort","DmNoEfec"};
        String[] arrayDcNomCamps = {"DcEnt", "DcSort","DcNoEfec"};
        String[] arrayDjNomCamps = {"DjEnt", "DjSort","DjNoEfec"};
        String[] arrayDvNomCamps = {"DvEnt", "DvSort","DvNoEfec"};
        String[] arrayDsNomCamps = {"DsEnt", "DsSort","DsNoEfec"};
        String[] arrayDgNomCamps = {"DgEnt", "DgSort","DgNoEfec"};

        listDiaSetmana.add(arrayDlNomCamps);
        listDiaSetmana.add(arrayDmNomCamps);
        listDiaSetmana.add(arrayDcNomCamps);
        listDiaSetmana.add(arrayDjNomCamps);
        listDiaSetmana.add(arrayDvNomCamps);
        listDiaSetmana.add(arrayDsNomCamps);
        listDiaSetmana.add(arrayDgNomCamps);



    }


    public String[] arrayCampsArxiuDades (){
        String[] ret = {"DlEnt", "DlSort", "DlNoEfec",
                "DmEnt", "DmSort","DmNoEfec",
                "DcEnt", "DcSort", "DcNoEfec",
                "DjEnt", "DjSort","DjNoEfec",
                "DvEnt", "DvSort","DvNoEfec",
                "DsEnt", "DsSort","DsNoEfec",
                "DgEnt", "DgSort","DgNoEfec"};

        return ret;
    }


    /*---------------------------- CARGAR DATOS -------------------------*/




    public void carregarDades (String arxiu){
        SharedPreferences prefe = getSharedPreferences(arxiu, Context.MODE_PRIVATE);

        tvDlEnt.setText(prefe.getString("DlEnt", "08:00"));
        tvDlSort.setText(prefe.getString("DlSort", "15:30"));
        tvDlNoEfec.setText(prefe.getString("DlNoEfec", "0.00"));
        tvDlEfec.setText(prefe.getString("DlEfec", "7.30"));

        tvDmEnt.setText(prefe.getString("DmEnt", "08:00"));
        tvDmSort.setText(prefe.getString("DmSort", "15:30"));
        tvDmNoEfec.setText(prefe.getString("DmNoEfec", "0.30"));
        tvDmEfec.setText(prefe.getString("DmEfec", "7.30"));

        tvDcEnt.setText(prefe.getString("DcEnt", "08:00"));
        tvDcSort.setText(prefe.getString("DcSort", "15:30"));
        tvDcNoEfec.setText(prefe.getString("DcNoEfec", "0.00"));
        tvDcEfec.setText(prefe.getString("DcEfec", "7.30"));

        tvDjEnt.setText(prefe.getString("DjEnt", "08:00"));
        tvDjSort.setText(prefe.getString("DjSort", "15:30"));
        tvDjNoEfec.setText(prefe.getString("DjNoEfec", "0.30"));
        tvDjEfec.setText(prefe.getString("DjEfec", "7.30"));

        tvDvEnt.setText(prefe.getString("DvEnt", "08:00"));
        tvDvSort.setText(prefe.getString("DvSort", "15:30"));
        tvDvNoEfec.setText(prefe.getString("DvNoEfec", "0.00"));
        tvDvEfec.setText(prefe.getString("DvEfec", "7.30"));

        tvDsEnt.setText(prefe.getString("DsEnt", "08:00"));
        tvDsSort.setText(prefe.getString("DsSort", "15:30"));
        tvDsNoEfec.setText(prefe.getString("DsNoEfec", "0.00"));
        tvDsEfec.setText(prefe.getString("DsEfec", "7.30"));

        tvDgEnt.setText(prefe.getString("DgEnt", "08:00"));
        tvDgSort.setText(prefe.getString("DgSort", "15:30"));
        tvDgNoEfec.setText(prefe.getString("DgNoEfec", "0.00"));
        tvDgEfec.setText(prefe.getString("DgEfec", "7.30"));

        verFinde = prefe.getBoolean("verFinde",false);

        cbDl.setChecked(prefe.getBoolean("checkBoxDl", false));
        cbDm.setChecked(prefe.getBoolean("checkBoxDm", false));
        cbDc.setChecked(prefe.getBoolean("checkBoxDc", false));
        cbDj.setChecked(prefe.getBoolean("checkBoxDj", false));
        cbDv.setChecked(prefe.getBoolean("checkBoxDv", false));
        cbDs.setChecked(prefe.getBoolean("checkBoxDs", false));
        cbDg.setChecked(prefe.getBoolean("checkBoxDg", false));



        //calcularTot();
    }

    public void carregarUnDia (String arxiu, int diaSetmana) {
        SharedPreferences prefe = getSharedPreferences(arxiu, Context.MODE_PRIVATE);

        List<TextView> diaSelecionat = listaSetmana.get(diaSetmana);//Esto es para los TExtVIew de pantalla

        String[] dia = listDiaSetmana.get(diaSetmana); //Esto es para poder indicar el String que hace referencia al dato que queremos recuperar del archivo

        diaSelecionat.get(Constantes.ENTRADA).setText(prefe.getString(dia[0], "09:00"));
        diaSelecionat.get(Constantes.SORTIDA).setText(prefe.getString(dia[1], "15:25"));
        diaSelecionat.get(Constantes.TEMPS_NOEFEC).setText(prefe.getString(dia[2], "0.00"));


    }




    /*--------------------------- GUARDAR DATOS -----------------------------*/



    public void guardarDades (String arxiu){
        SharedPreferences prefe = getSharedPreferences(arxiu, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor=prefe.edit();

        editor.putString("DlEnt",tvDlEnt.getText().toString());
        editor.putString("DlSort",tvDlSort.getText().toString());
        editor.putString("DlNoEfec",tvDlNoEfec.getText().toString());
        editor.putString("DlEfec",tvDlEfec.getText().toString());

        editor.putString("DmEnt",tvDmEnt.getText().toString());
        editor.putString("DmSort",tvDmSort.getText().toString());
        editor.putString("DmNoEfec",tvDmNoEfec.getText().toString());
        editor.putString("DmEfec",tvDmEfec.getText().toString());

        editor.putString("DcEnt",tvDcEnt.getText().toString());
        editor.putString("DcSort",tvDcSort.getText().toString());
        editor.putString("DcNoEfec",tvDcNoEfec.getText().toString());
        editor.putString("DcEfec",tvDcEfec.getText().toString());

        editor.putString("DjEnt",tvDjEnt.getText().toString());
        editor.putString("DjSort",tvDjSort.getText().toString());
        editor.putString("DjNoEfec",tvDjNoEfec.getText().toString());
        editor.putString("DjEfec",tvDjEfec.getText().toString());

        editor.putString("DvEnt",tvDvEnt.getText().toString());
        editor.putString("DvSort",tvDvSort.getText().toString());
        editor.putString("DvNoEfec",tvDvNoEfec.getText().toString());
        editor.putString("DvEfec",tvDvEfec.getText().toString());

        editor.putString("DsEnt",tvDsEnt.getText().toString());
        editor.putString("DsSort",tvDsSort.getText().toString());
        editor.putString("DsNoEfec",tvDsNoEfec.getText().toString());
        editor.putString("DsEfec",tvDsEfec.getText().toString());

        editor.putString("DgEnt",tvDgEnt.getText().toString());
        editor.putString("DgSort",tvDgSort.getText().toString());
        editor.putString("DgNoEfec",tvDgNoEfec.getText().toString());
        editor.putString("DgEfec",tvDgEfec.getText().toString());

        editor.putBoolean("verFinde", verFinde);

        editor.putBoolean("checkBoxDl", cbDl.isChecked());
        editor.putBoolean("checkBoxDm", cbDm.isChecked());
        editor.putBoolean("checkBoxDc", cbDc.isChecked());
        editor.putBoolean("checkBoxDj", cbDj.isChecked());
        editor.putBoolean("checkBoxDv", cbDv.isChecked());
        editor.putBoolean("checkBoxDs", cbDs.isChecked());
        editor.putBoolean("checkBoxDg", cbDg.isChecked());




        editor.apply();

    }

    public void guardarUnDia (String arxiu, int diaSetmana) {
        SharedPreferences prefe = getSharedPreferences(arxiu, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor=prefe.edit();

        List<TextView> diaSelecionat = listaSetmana.get(diaSetmana);

        String[] dia = listDiaSetmana.get(diaSetmana);

        editor.putString(dia[0],diaSelecionat.get(Constantes.ENTRADA).getText().toString());
        editor.putString(dia[1],diaSelecionat.get(Constantes.SORTIDA).getText().toString());
        editor.putString(dia[2],diaSelecionat.get(Constantes.TEMPS_NOEFEC).getText().toString());

        editor.apply();



    }



    public double tempsStringToInt (String temps){
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
    public Calendar stringToCalendar(String data, String format) {
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
    public String calendarToString(Calendar cal, String format) {
        String data = null;
        if (cal != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            data = sdf.format(cal.getTime());
        }

        return data;

    }


     /*
    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
        guardarDades ();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
        guardarDades ();
    }

*/


    /*
    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
        carregarDades();
    }
*/




}
