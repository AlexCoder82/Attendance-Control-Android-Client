package com.alex.attendance_control.toasts;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ApiErrorMessageToast {

    private static String message = "Ocurrió un error: el servidor no responde.";

    public static void Show(Context context){

        Toast toast= Toast.makeText(context,
                message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
       // text.setTextColor(Color.BLACK);
        toast.show();
    }
}
