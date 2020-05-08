package com.alex.attendance_control.toasts;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.attendance_control.R;

public class SendSuccesToast {

    //Crea un toast con un mensaje de exito de envio de la lista de alumnos
    public static void Show(Context context){

        String message = "La lista de ausencias ha sido enviada.";
        Toast toast= Toast.makeText(context,
                message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        View view = toast.getView();
        TextView text =  view.findViewById(android.R.id.message);
        text.setTextColor(context.getResources().getColor(R.color.optima));
        text.setTypeface(null, Typeface.BOLD);
        text.setTextSize(20);
        text.setShadowLayer(0,0,0,0);
        toast.show();

    }
}
