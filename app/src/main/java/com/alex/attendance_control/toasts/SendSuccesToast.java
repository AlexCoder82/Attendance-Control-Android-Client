package com.alex.attendance_control.toasts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.attendance_control.R;

/*
    Toast mostrado cuando se ha enviado la lista de alumnos
 */
public class SendSuccesToast {

    private static final String MESSAGE = "La lista de ausencias ha sido enviada.";

    public static void Show(Context context) {

        Toast toast = Toast.makeText(context,
                MESSAGE, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        View view = toast.getView();
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(context.getResources().getColor(R.color.optima));
        text.setTypeface(null, Typeface.BOLD);
        text.setTextSize(20);
        text.setShadowLayer(0, 0, 0, 0);
        toast.show();

    }
}
