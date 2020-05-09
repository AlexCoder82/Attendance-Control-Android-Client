package com.alex.attendance_control.toasts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/*
    Toast mostrado cuando la sesion de usuario ha expirado
 */
public class SessionHasExpiredToast {

    private static final String MESSAGE = "La sesión de usuario ha expirado.";

    public static void show(Context context) {

        Toast toast = Toast.makeText(context,
                MESSAGE, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 150);
        View view = toast.getView();
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.RED);
        text.setTypeface(null, Typeface.BOLD);
        text.setShadowLayer(0, 0, 0, 0);
        toast.show();

    }
}
