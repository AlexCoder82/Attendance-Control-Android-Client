package com.alex.attendance_control.toasts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/*
    Toast mostrado cuando el servidor devuelve un error no previsto (500 o 0)
 */
public class ApiErrorMessageToast {

    private static String message = "Ocurrió un error: el servidor no responde.";

    public static void Show(Context context){

        Toast toast= Toast.makeText(context,
                message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 150);
        View view = toast.getView();
        TextView text =  view.findViewById(android.R.id.message);
        text.setTextColor(Color.RED);
        text.setTypeface(null, Typeface.BOLD);
        text.setTextSize(16);
        text.setShadowLayer(0,0,0,0);
        toast.show();

    }
}
