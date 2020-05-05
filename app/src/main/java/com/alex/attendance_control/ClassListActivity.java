package com.alex.attendance_control;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.alex.attendance_control.models.SchoolClass;
import com.alex.attendance_control.utils.SessionStorage;

import java.util.ArrayList;

public class ClassListActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<SchoolClass> schoolClasses;
    private TextView labelSession;
    private TableLayout tableLayout;
    private TextView labelMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_list);

        labelMessage = findViewById(R.id.labelMessage);

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        this.schoolClasses = (ArrayList<SchoolClass>) bundle
                .getSerializable("schoolClassList");

        String message = "Hola "+SessionStorage.firstName;

        if(this.schoolClasses.size()>0){
            message +=", hoy impartes "+ this.schoolClasses.size()+ " clases.";
            this.PopulateListView();
        }else{
            message +=", hoy no impartes ninguna clase.";
        }
        labelMessage.setText(message);
    }

    private void PopulateListView() {

        ConstraintLayout constraintLayout = findViewById(R.id.table_container_layout);
        tableLayout = new TableLayout(this);
        TableLayout.LayoutParams params = new TableLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        tableLayout.setLayoutParams(params);


        //Contenido
        for (int i = 0 ; i<this.schoolClasses.size();i++) {

            //Fila
            TableRow row = new TableRow(this);
            row.setGravity(Gravity.CENTER_VERTICAL);

            //Primera columna sin width
            TextView textViewSubject = new TextView(this);
            //textViewSubject.setText(this.schoolClasses.get(i).getSubject().getName());
            textViewSubject.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            textViewSubject.setTypeface(null, Typeface.BOLD);
            textViewSubject.setTextColor(Color.BLACK);
            textViewSubject.setText(this.schoolClasses.get(i).getSubject().getName());
            textViewSubject.setGravity(Gravity.LEFT);
            row.addView(textViewSubject);

            //Segunda columna
            TextView textViewSchedules = new TextView(this);
            textViewSchedules.setText(this.schoolClasses.get(i).getSchedule().getStart() + " - "
                    +this.schoolClasses.get(i).getSchedule().getEnd());
            textViewSchedules.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            textViewSchedules.setTypeface(null, Typeface.BOLD);
            textViewSchedules.setTextColor(Color.BLACK);
            textViewSchedules.setPadding(2,0,10,0);
            textViewSchedules.setGravity(Gravity.CENTER);
            row.addView(textViewSchedules, new TableRow.LayoutParams(200, 140, 1f));

            //Tercera columna
            TextView button = new TextView(this);
            button.setText(R.string.list);
            button.setTextColor(getResources().getColor(R.color.optima));
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            button.setTypeface(null, Typeface.BOLD);
            button.setGravity(Gravity.CENTER);
            button.setPaintFlags(button.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            row.addView(button, new TableRow.LayoutParams(270, 140, 1f));

            //Alterna los colores de fondo de las filas
            if(i%2==0)
                row.setBackgroundColor(getResources().getColor(R.color.row));

            tableLayout.addView(row);
        }

        ViewGroup parent = (ViewGroup) tableLayout.getParent();
        if (parent != null) {
            parent.removeView(tableLayout);
        }

        //El width de la columna de las asignaturas se adapta
        tableLayout.setColumnStretchable(0,true);

        constraintLayout.addView(tableLayout);

    }

    @Override
    public void onClick(View v) {

        if(v.equals(labelSession)){
            SessionStorage.delete();
            this.finish();
        }

    }

    //Si se pulsa hacia atras, no ocurre nada
    @Override
    public void onBackPressed() {

    }
}
