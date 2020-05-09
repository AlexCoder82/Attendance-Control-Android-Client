package com.alex.attendance_control.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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

import com.alex.attendance_control.R;
import com.alex.attendance_control.callbacks.CallListAsyncCallback;
import com.alex.attendance_control.httpTasks.GetCallListTask;
import com.alex.attendance_control.httpTasks.SendCallListTask;
import com.alex.attendance_control.models.Absence;
import com.alex.attendance_control.models.AbsenceType;
import com.alex.attendance_control.models.SchoolClassStudent;
import com.alex.attendance_control.toasts.ApiErrorMessageToast;
import com.alex.attendance_control.toasts.SendSuccesToast;
import com.alex.attendance_control.toasts.SessionHasExpiredToast;
import com.alex.attendance_control.utils.SessionStorage;

import java.util.List;


public class CallListActivity extends AppCompatActivity
        implements CallListAsyncCallback, CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private int[] schoolClassIds;//Ids de las clases
    private List<SchoolClassStudent> callList;//Listado de alumno
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

        //Recupera los ids de las clases del Intent
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        this.schoolClassIds = (int[]) bundle
                .getSerializable("schoolClassIds");

        //Recupera el nombre de la asignatura del Intent
        String subjectName = bundle.getString("subjectName");

        //Recupera el horario del Intent
        String startAt = bundle.getString("startAt");

        //Mensaje
        String message = "Clase de " + subjectName +
                " prevista a las " + startAt + ".";
        labelMessage.setText(message);

        //Muestra la rueda de carga
        getCallListSpinner.setVisibility(View.VISIBLE);

        //Peticion http del listado de alumno
        new GetCallListTask(this, this)
                .execute(this.schoolClassIds);

    }

    //Callback de la respuesta de la peticion http
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

    //Callback en caso de error no previsto
    @Override
    public void OnServerError() {

        //Oculta las ruedecitas
        this.sendCallListSpinner.setVisibility(View.INVISIBLE);
        this.getCallListSpinner.setVisibility(View.INVISIBLE);
        ApiErrorMessageToast.Show(this);
        //Cierra todas las actividades y vuelve a la actividad de login
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void OnSessionExpiredError() {

        SessionStorage.closeSession();
        //Cierra todas las actividades y vuelve a la actividad de login
        SessionHasExpiredToast.show(this);
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    //Crea y rellena la tabla
    private void setTableLayout() {

        ConstraintLayout tableContainerLayout = findViewById(R.id.table_container_layout);
        tableLayout = new TableLayout(this);
        TableLayout.LayoutParams params = new TableLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        tableLayout.setLayoutParams(params);

        //Fila cabecera
        TableRow headers = new TableRow(this);
        //headers.setGravity(Gravity.CENTER);
        headers.setBackgroundColor(Color.WHITE);

        //Header dni
        TextView textViewDniHeader = new TextView(this);
        textViewDniHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textViewDniHeader.setTypeface(null, Typeface.BOLD);
        textViewDniHeader.setTextColor(getResources().getColor(R.color.optima));
        textViewDniHeader.setText("Dni");
        textViewDniHeader.setGravity(Gravity.LEFT | Gravity.CENTER);
        headers.addView(textViewDniHeader, new TableRow.LayoutParams(140, 120, 1f));

        //Header nombre y apellidos
        TextView textViewNameHeader = new TextView(this);
        textViewNameHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textViewNameHeader.setTypeface(null, Typeface.BOLD);
        textViewNameHeader.setTextColor(getResources().getColor(R.color.optima));
        textViewNameHeader.setText("Nombre");
        textViewNameHeader.setGravity(Gravity.LEFT | Gravity.CENTER);
        headers.addView(textViewNameHeader, new TableRow.LayoutParams(350, 120, 1f));

        //Header ausencia
        TextView textViewAbsenceHeader = new TextView(this);
        textViewAbsenceHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textViewAbsenceHeader.setTypeface(null, Typeface.BOLD);
        textViewAbsenceHeader.setTextColor(getResources().getColor(R.color.optima));
        textViewAbsenceHeader.setText("Ausencia");
        headers.addView(textViewAbsenceHeader, new TableRow.LayoutParams(100, 120, 1f));

        //Header retraso
        TextView textViewDelayHeader = new TextView(this);
        textViewDelayHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        textViewDelayHeader.setTypeface(null, Typeface.BOLD);
        textViewDelayHeader.setTextColor(getResources().getColor(R.color.optima));
        textViewDelayHeader.setText("Retraso");
        textViewDelayHeader.setGravity(Gravity.CENTER);
        headers.addView(textViewDelayHeader, new TableRow.LayoutParams(100, 120, 1f));

        tableLayout.addView(headers);


        //Contenido de la tabla
        for (int i = 0; i < this.callList.size(); i++) {

            //Fila
            TableRow row = new TableRow(this);

            //Celda dni
            TextView textViewDni = new TextView(this);
            textViewDni.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            textViewDni.setTypeface(null, Typeface.BOLD);
            textViewDni.setTextColor(Color.BLACK);
            textViewDni.setGravity(Gravity.LEFT | Gravity.CENTER);
            textViewDni.setText(this.callList.get(i).getStudent().getDni());
            row.addView(textViewDni,
                    new TableRow.LayoutParams(140, 100, 1f));

            //Celda nombre y apellidos
            TextView textViewName = new TextView(this);
            String name = this.callList.get(i).getStudent().getFirstName() + " " +
                    this.callList.get(i).getStudent().getLastName1() + " " +
                    this.callList.get(i).getStudent().getLastName2();
            textViewName.setText(name);
            textViewName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            textViewName.setTypeface(null, Typeface.BOLD);
            textViewName.setGravity(Gravity.LEFT | Gravity.CENTER);
            textViewName.setTextColor(Color.BLACK);
            row.addView(textViewName,
                    new TableRow.LayoutParams(350, 100, 1f));

            //Celda checkbox ausencia
            CheckBox checkBoxAbsence = new CheckBox(this);
            checkBoxAbsence.setTextColor(getResources().getColor(R.color.optima));
            checkBoxAbsence.setTypeface(null, Typeface.BOLD);
            checkBoxAbsence.setGravity(Gravity.CENTER);

            if (this.callList.get(i).getAbsence() != null
                    && this.callList.get(i).getAbsence().getType() == 0) {
                checkBoxAbsence.setChecked(true);
            }
            checkBoxAbsence.setOnCheckedChangeListener(this);

            //Para centrar el checkbox, lo pongo dentro de un LinearLayout
            //y centro el LinearLayout en la celda de la fila
            LinearLayout linearLayout1 = new LinearLayout(this);
            linearLayout1.addView(checkBoxAbsence);
            linearLayout1.setGravity(Gravity.CENTER_HORIZONTAL);
            row.addView(linearLayout1, new TableRow.LayoutParams(100, 100, 1));


            //Celda checkbox retraso
            CheckBox checkBoxDelay = new CheckBox(this);
            checkBoxDelay.setTextColor(getResources().getColor(R.color.optima));
            checkBoxDelay.setTypeface(null, Typeface.BOLD);
            checkBoxDelay.setGravity(Gravity.CENTER | Gravity.CENTER);

            if (this.callList.get(i).getAbsence() != null
                    && this.callList.get(i).getAbsence().getType() == 1)
                checkBoxDelay.setChecked(true);

            checkBoxDelay.setOnCheckedChangeListener(this);

            //Para centrar el checkbox, lo pongo dentro de un LinearLayout
            //y centro el LinearLayout en la celda de la fila

            LinearLayout linearLayout2 = new LinearLayout(this);
            linearLayout2.addView(checkBoxDelay);
            linearLayout2.setGravity(Gravity.CENTER_HORIZONTAL);
            row.addView(linearLayout2,
                    new TableRow.LayoutParams(100, 100, 1));


            //Alterna los colores de fondo de las filas
            if (i % 2 == 0)
                row.setBackgroundColor(getResources().getColor(R.color.row));

            tableLayout.addView(row);
        }

        //Agrega la tabla a la vista
        tableContainerLayout.addView(tableLayout);

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

        //Obtiene el alumno correspondiente en la lista
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

                //Si la ausencia no existe, se instancia una ausencia
                if (schoolClassStudent.getAbsence() == null) {
                    schoolClassStudent.setAbsence(
                            new Absence(AbsenceType.TOTAL)
                    );
                }
                //Sino se cambia el tipo
                else {
                    schoolClassStudent.getAbsence().setType(AbsenceType.TOTAL);
                }

            } else {
                //Se cancela la ausencia
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

            } else {
                //Se cancela la ausencia
                schoolClassStudent.getAbsence().setType(AbsenceType.CANCELLED);
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
            new SendCallListTask(this, this).execute(this.callList);
        }
    }

    //Callback de la respuesta de la peticion del envio del listado de alumno
    @Override
    public void onSendCallListTaskCompleted() {

        this.sendCallListSpinner.setVisibility(View.INVISIBLE);//Oculta la ruedecita

        SendSuccesToast.Show(this);

        //Cierra la actividad a los 3.5 segundos(lo que dura el toast)
        new Handler().postDelayed(() -> {

            CallListActivity.this.finish();//Vuelve a la actividad de clases

        }, 3500);
    }

}


