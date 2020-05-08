package com.alex.attendance_control;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.alex.attendance_control.models.SchoolClass;
import com.alex.attendance_control.utils.SessionStorage;

import java.time.LocalTime;
import java.util.ArrayList;

/*
    Actividad de la lista de clases del día
 */
public class ClassListActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<SchoolClass> schoolClasses;
    private ArrayList<SchoolClass> teacherSchoolClasses;
    private TableLayout tableLayout;
    private TextView labelMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        this.setTitle("Lista De Clases");

        labelMessage = findViewById(R.id.labelMessage);

        //Recupera la lista de clases
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        this.schoolClasses = (ArrayList<SchoolClass>) bundle
                .getSerializable("schoolClassList");


        //Mensaje para el profesor
        String message = "Hola " + SessionStorage.firstName;


        if (this.schoolClasses.size() > 0) {
            message += ", hoy impartes " + this.schoolClasses.size() + " clases.";
            this.getTeacherClasses();
            this.setTableLayout();
        } else {
            message += ", hoy no impartes ninguna clase.";
        }
        labelMessage.setText(message);
    }

    /*
        Crea la lista de clases a mostrar juntando las clases
        que tienen el mismo horario en una sola clase.
     */
    private void getTeacherClasses() {

        this.teacherSchoolClasses = new ArrayList<SchoolClass>();

        for (SchoolClass sc : this.schoolClasses) {

            boolean found = false;

            for (int i = 0; i < this.teacherSchoolClasses.size() && !found; i++) {

                if (sc.getSchedule().getStart()
                        .equals(this.teacherSchoolClasses.get(i)
                                .getSchedule().getStart())) {
                    found = true;
                }

            }

            if (!found) {
                this.teacherSchoolClasses.add(sc);
            }
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


        //Contenido
        for (int i = 0; i < this.teacherSchoolClasses.size(); i++) {

            //Fila
            TableRow row = new TableRow(this);
            row.setGravity(Gravity.CENTER_VERTICAL);

            //Primera columna sin width
            TextView textViewSubject = new TextView(this);
            //textViewSubject.setText(this.schoolClasses.get(i).getSubject().getName());
            textViewSubject.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textViewSubject.setTypeface(null, Typeface.BOLD);
            textViewSubject.setTextColor(Color.BLACK);
            textViewSubject.setText(this.teacherSchoolClasses.get(i).getSubject().getName());
            textViewSubject.setGravity(Gravity.LEFT|Gravity.CENTER);

            row.addView(textViewSubject, new TableRow.LayoutParams(500, 165, 1f));

            //Segunda columna
            TextView textViewSchedules = new TextView(this);
            textViewSchedules.setText(this.teacherSchoolClasses.get(i).getSchedule().getStart() + " - "
                    + this.schoolClasses.get(i).getSchedule().getEnd());
            textViewSchedules.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            textViewSchedules.setTypeface(null, Typeface.BOLD);
            textViewSchedules.setTextColor(Color.BLACK);
            textViewSchedules.setPadding(2, 0, 10, 0);
            textViewSchedules.setGravity(Gravity.CENTER);
            row.addView(textViewSchedules, new TableRow.LayoutParams(180, 140, 1f));

            //Tercera columna
            TextView button = new TextView(this);
            button.setText(R.string.list);
            button.setTextColor(getResources().getColor(R.color.optima));
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            button.setTypeface(null, Typeface.BOLD);
            button.setGravity(Gravity.CENTER);
            button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            button.setOnClickListener(this);
            row.addView(button, new TableRow.LayoutParams(270, 140, 1f));

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

    @Override
    public void onClick(View v) {

        //Si se pulsa uno de los enlaces de la tabla
        if (v instanceof TextView && v.getParent() instanceof TableRow) {

            //Recupero la fila de la tabla
            TableRow row = (TableRow) v.getParent();

            //De la fila recupero el TexiView del nombre de la asignatura
            TextView subjectView = (TextView)row.getChildAt(0);
            //De la fila recupero el TextView del horario
            TextView schedulesView = (TextView) row.getChildAt(1);

            //Del textView del horario recupero la hora de inicio
            String startAt = schedulesView.getText().toString().substring(0, 5);

            //Solo se puede acceder a la lista de alumnos si la clase ya ha empezado
            if (this.schoolClassAlreadyStarted(startAt)) {

                //Array con los ids de las clases
                int[] schoolClassIds = this.getSchoolClassIds(startAt);

                //Abre la actividad de la lista de alumnos pasandole el array
                //de ids como paramatro serializado
                Intent intent = new Intent(this, CallListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("schoolClassIds", schoolClassIds);
                bundle.putString("subjectName", subjectView.getText().toString());
                bundle.putString("startAt", schedulesView.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);

            } //Si no ha empezado la clase, se muestra un mensaje
            else {
                Toast toast = Toast.makeText(this,
                        "Esta clase aún no ha empezado.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 50);
                View view = toast.getView();
                TextView text =  view.findViewById(android.R.id.message);
                text.setTextColor(Color.BLACK);
                text.setTypeface(null, Typeface.BOLD);
                text.setShadowLayer(0,0,0,0);
                toast.show();

            }
        }

    }

    //Comprueba si una clase ya ha empezado
    private boolean schoolClassAlreadyStarted(String startAt) {

        boolean result = false;

        if (LocalTime.now().isAfter(LocalTime.parse(startAt))) {
            result = true;
        }

        return result;

    }

    // Retorna un array de todos los ids de clases que empiezan
    // a la hora pasada por parametro
    private int[] getSchoolClassIds(final String startAt) {

        int[] ids = schoolClasses
                .stream()
                .filter(sc -> sc.getSchedule().getStart().equals(startAt))
                .mapToInt(sc -> sc.getId())
                .toArray();

        return ids;

    }

    //Si se pulsa hacia atras, no se vuelve a la actividad de login
    @Override
    public void onBackPressed() {

    }
}
