package com.alex.attendance_control.toasts;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class WrongDniMessageToast {

    public static void Show(Context context, String message){

        Toast toast= Toast.makeText(context,
                message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        // text.setTextColor(Color.BLACK);
        toast.show();

    }
}
