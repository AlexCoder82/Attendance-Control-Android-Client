package com.alex.attendance_control.toasts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/*
    Toast mostrado cuando se pide el listado de alumnos de una clase que aun no ha empezado
 */
public class SchoolClassHasNotStartedYetToast {

    public static void show(Context context){

        Toast toast = Toast.makeText(context,
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
