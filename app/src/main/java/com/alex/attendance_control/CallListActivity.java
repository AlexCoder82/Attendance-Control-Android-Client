package com.alex.attendance_control;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.alex.attendance_control.callbacks.AsyncResponse;
import com.alex.attendance_control.httpTasks.GetCallListTask;
import com.alex.attendance_control.httpTasks.SendCallListTask;
import com.alex.attendance_control.models.Absence;
import com.alex.attendance_control.models.AbsenceType;
import com.alex.attendance_control.models.SchoolClassStudent;
import com.alex.attendance_control.toasts.SendSuccesToast;

import java.util.List;


public class CallListActivity extends AppCompatActivity
        implements AsyncResponse, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private int[] schoolClassIds;
    private List<SchoolClassStudent> callList;
    private ProgressBar getCallListSpinner;
    private ProgressBar sendCallListSpinner;
    private TableLayout tableLayout;
    private TextView labelMessage;
    private Button buttonSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_list);

        this.setTitle("Listado De Alumnos");//Titulo de la actividad

        getCallListSpinner = findViewById(R.id.loading_spinner);
        sendCallListSpinner = findViewById(R.id.send_loading_spinner);
        labelMessage = findViewById(R.id.labelMessage);
        buttonSend = findViewById(R.id.buttonSend);


        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        this.schoolClassIds = (int[]) bundle
                .getSerializable("schoolClassIds");
        String subjectName = bundle.getString("subjectName");
        String startAt = bundle.getString("startAt");

        //Mensaje
        labelMessage.setText("Clase de " + subjectName + " prevista a las " + startAt + ".");

        //Muestra la rueda de carga
        getCallListSpinner.setVisibility(View.VISIBLE);

        //Peticion http del listado de alumno
        new GetCallListTask(this, this).execute(this.schoolClassIds);


    }

    //Callback de la peticion http
    @Override
    public void onGetCallListTaskCompleted(List<SchoolClassStudent> list) {

        //Oculta la rueda de carga
        getCallListSpinner.setVisibility(View.INVISIBLE);

        if (list != null) {
            this.callList = list;

            this.buttonSend.setOnClickListener(this);
            this.buttonSend.setVisibility(View.VISIBLE);
            this.setTableLayout();
        }

    }


    //Crea y rellena la tabla
    private void setTableLayout() {

        ConstraintLayout constraintLayout = findViewById(R.id.table_container_layout);
        tableLayout = new TableLayout(this);
        TableLayout.LayoutParams params = new TableLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        tableLayout.setLayoutParams(params);

        //Cabecera
        TableRow headers = new TableRow(this);
        //headers.setGravity(Gravity.CENTER);
        headers.setBackgroundColor(Color.WHITE);

        //Columna dni
        TextView textViewDniHeader = new TextView(this);
        //textViewSubject.setText(this.schoolClasses.get(i).getSubject().getName());
        textViewDniHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textViewDniHeader.setTypeface(null, Typeface.BOLD);
        textViewDniHeader.setTextColor(getResources().getColor(R.color.optima));
        textViewDniHeader.setText("Dni");
        textViewDniHeader.setGravity(Gravity.LEFT | Gravity.CENTER);
        headers.addView(textViewDniHeader, new TableRow.LayoutParams(140, 120, 1f));

        //Columna nombre
        TextView textViewNameHeader = new TextView(this);
        //textViewSubject.setText(this.schoolClasses.get(i).getSubject().getName());
        textViewNameHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textViewNameHeader.setTypeface(null, Typeface.BOLD);
        textViewNameHeader.setTextColor(getResources().getColor(R.color.optima));
        textViewNameHeader.setText("Nombre");
        textViewNameHeader.setGravity(Gravity.LEFT | Gravity.CENTER);
        headers.addView(textViewNameHeader, new TableRow.LayoutParams(350, 120, 1f));

        //Columna ausencia
        TextView textViewAbsenceHeader = new TextView(this);
        //textViewSubject.setText(this.schoolClasses.get(i).getSubject().getName());
        textViewAbsenceHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textViewAbsenceHeader.setTypeface(null, Typeface.BOLD);
        textViewAbsenceHeader.setTextColor(getResources().getColor(R.color.optima));
        textViewAbsenceHeader.setText("Ausencia");
        //textViewAbsenceHeader.setGravity(Gravity.CENTER);
        headers.addView(textViewAbsenceHeader, new TableRow.LayoutParams(100, 120, 1f));

        //Columna retraso
        TextView textViewDelayHeader = new TextView(this);
        //textViewSubject.setText(this.schoolClasses.get(i).getSubject().getName());
        textViewDelayHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textViewDelayHeader.setTypeface(null, Typeface.BOLD);
        textViewDelayHeader.setTextColor(getResources().getColor(R.color.optima));
        textViewDelayHeader.setText("Retraso");
        textViewDelayHeader.setGravity(Gravity.CENTER);
        headers.addView(textViewDelayHeader, new TableRow.LayoutParams(100, 120, 1f));

        tableLayout.addView(headers);
        //Contenido
        for (int i = 0; i < this.callList.size(); i++) {

            //Fila
            TableRow row = new TableRow(this);
            //row.setGravity(Gravity.CENTER);


            //Primera columna 
            TextView textViewDni = new TextView(this);
            textViewDni.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            textViewDni.setTypeface(null, Typeface.BOLD);
            textViewDni.setTextColor(Color.BLACK);
            textViewDni.setGravity(Gravity.LEFT | Gravity.CENTER);
            textViewDni.setText(this.callList.get(i).getStudent().getDni());
            row.addView(textViewDni, new TableRow.LayoutParams(140, 100, 1f));

            //Segunda columna
            TextView textViewName = new TextView(this);
            String name = this.callList.get(i).getStudent().getFirstName() + " " +
                    this.callList.get(i).getStudent().getLastName1() + " " +
                    this.callList.get(i).getStudent().getLastName2();
            textViewName.setText(name);
            textViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            textViewName.setTypeface(null, Typeface.BOLD);
            textViewName.setGravity(Gravity.LEFT | Gravity.CENTER);
            textViewName.setTextColor(Color.BLACK);
            row.addView(textViewName, new TableRow.LayoutParams(350, 100, 1f));

            //Tercera columna
            CheckBox checkBoxAbsence = new CheckBox(this);
            checkBoxAbsence.setTextColor(getResources().getColor(R.color.optima));
            checkBoxAbsence.setTypeface(null, Typeface.BOLD);
            checkBoxAbsence.setGravity(Gravity.CENTER);

            if (this.callList.get(i).getAbsence() != null
                    && this.callList.get(i).getAbsence().getType() == 0) {
                checkBoxAbsence.setChecked(true);
            }
            checkBoxAbsence.setOnCheckedChangeListener(this);
            //Para centar el checkbox, lo pongo dentro de un LinearLayout
            //y centro el LinearLayout en la celda de la fila
            LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.addView(checkBoxAbsence);
            linearLayout1.setGravity(Gravity.CENTER_HORIZONTAL);
            row.addView(linearLayout1, new TableRow.LayoutParams(100, 100, 1));


            //Cuarta columna
            CheckBox checkBoxDelay = new CheckBox(this);
            checkBoxDelay.setTextColor(getResources().getColor(R.color.optima));
            checkBoxDelay.setTypeface(null, Typeface.BOLD);
            checkBoxDelay.setGravity(Gravity.CENTER | Gravity.CENTER);
            if (this.callList.get(i).getAbsence() != null
                    && this.callList.get(i).getAbsence().getType() == 1)
                checkBoxDelay.setChecked(true);
            checkBoxDelay.setOnCheckedChangeListener(this);
            //Para centar el checkbox, lo pongo dentro de un LinearLayout
            //y centro el LinearLayout en la celda de la fila
            LinearLayout linearLayout2 = new LinearLayout(this);
            linearLayout2.addView(checkBoxDelay);
            linearLayout2.setGravity(Gravity.CENTER_HORIZONTAL);
            row.addView(linearLayout2, new TableRow.LayoutParams(100, 100, 1));
            //Alterna los colores de fondo de las filas
            if (i % 2 == 0)
                row.setBackgroundColor(getResources().getColor(R.color.row));

            tableLayout.addView(row);
        }

        ViewGroup parent = (ViewGroup) tableLayout.getParent();
        if (parent != null) {
            parent.removeView(tableLayout);
        }

        //Agrega la tabla a la vista
        constraintLayout.addView(tableLayout);

    }

    /*
        Eventos de los checkbox de la tabla
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        //Recupera la fila
        TableRow row = (TableRow) buttonView.getParent().getParent();

        //Recupera el dni del alumno de la fila
        TextView textDni = (TextView) (row.getChildAt(0));
        String dni = textDni.getText().toString();

        //obtiene el alumno correspondiente en la lista
        SchoolClassStudent schoolClassStudent = this.callList
                .stream()
                .filter(sc -> sc.getStudent().getDni() == dni)
                .findFirst()
                .get();

        //Si es el checkbox de ausencia(tercera columna)
        if (buttonView.getParent() == row.getChildAt(2)) {

            if (isChecked) {

                //El checkbox de retraso se encuentra dentro de un linearLayout
                //que a su vez se encuentra en el TableRow en el indice 3
                LinearLayout linearLayout = (LinearLayout) row.getChildAt(3);
                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(0);
                //Si el alummo es marcado como ausente , no puede tener retraso
                checkBox.setChecked(false);

                //Color de fondo

                //Si la ausencia no existe
                if (schoolClassStudent.getAbsence() == null) {
                    schoolClassStudent.setAbsence(
                            new Absence(AbsenceType.TOTAL)
                    );
                } else {
                    schoolClassStudent.getAbsence().setType(AbsenceType.TOTAL);
                }
                Log.d("CLICK", dni);

            } else {
                schoolClassStudent.getAbsence().setType(AbsenceType.CANCELLED);
            }

        }

        //Si es el checkbox de retraso(cuarta columna)
        if (buttonView.getParent() == row.getChildAt(3)) {
            if (isChecked) {

                //El checkbox de ausencia se encuentra dentro de un linearLayout
                //que a su vez se encuentra en el TableRow en el indice 2
                LinearLayout linearLayout = (LinearLayout) row.getChildAt(2);
                CheckBox checkBox = (CheckBox) linearLayout.getChildAt(0);
                //Si el alummo es marcado con retraso , no puede tener ausencia
                checkBox.setChecked(false);


                //Si la ausencia no existe
                if (schoolClassStudent.getAbsence() == null) {
                    schoolClassStudent.setAbsence(
                            new Absence(AbsenceType.DELAY) //Nueva ausencia de tipo retraso
                    );
                } else {
                    schoolClassStudent.getAbsence().setType(AbsenceType.DELAY);//Tipo retraso
                }
                Log.d("CLICK", dni);

            } else {
                schoolClassStudent.getAbsence().setType(AbsenceType.CANCELLED);//Ausencia cancelada
            }

        }

    }

    /*
        Eventos de los botones
     */
    @Override
    public void onClick(View v) {

        //Boton Enviar
        if (v.equals(this.buttonSend)) {

            this.buttonSend.setVisibility(View.INVISIBLE);
            this.sendCallListSpinner.setVisibility(View.VISIBLE);
            for(SchoolClassStudent sc : this.callList){

                Log.d("PRUEBA", "DNI "+ sc.getStudent().getDni()+ " Alumno " +sc.getStudent().getDni());
               if(sc.getAbsence() != null)
                Log.d("PRUEBA", "Ausencia " +sc.getAbsence().getType());
                Log.d("PRUEBA", "Clase " +sc.getSchoolClassId());
            }
            new SendCallListTask(this, this).execute(this.callList);
        }
    }

    @Override
    public void onSendCallListTaskCompleted(boolean result) {

        this.sendCallListSpinner.setVisibility(View.INVISIBLE);
        if (result) {

            SendSuccesToast.Show(this);

            //Cierra la actividad a los 3.5 segundos(lo que dura el toast)
            new Handler().postDelayed(() -> {

                CallListActivity.this.finish();//Vuelve a la actividad de clases

            }, 3500);
        }


    }


}
