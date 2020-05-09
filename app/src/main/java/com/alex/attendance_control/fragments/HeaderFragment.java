package com.alex.attendance_control.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alex.attendance_control.R;
import com.alex.attendance_control.activities.SignInActivity;
import com.alex.attendance_control.utils.SessionStorage;

/*
    Cabecera
 */
public class HeaderFragment extends Fragment implements View.OnClickListener {

    private TextView labelSession;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.header, container, false);

        labelSession = view.findViewById(R.id.label_session);
        labelSession.setOnClickListener(this);

        try {

            if (SessionStorage.token.isEmpty()) {

                labelSession.setVisibility(View.INVISIBLE);

            }else{

                labelSession.setVisibility(View.VISIBLE);

            }
        } catch (Exception ex) {

            Log.d("ERROR", ex.getMessage());

        }

        return view;
    }

    //Event cerrar sesión
    @Override
    public void onClick(View v) {

        if(v.equals(labelSession)){

            SessionStorage.closeSession();
            labelSession.setVisibility(View.INVISIBLE);
            //Cierra todas las actividades y abre la actividad de login
            Intent intent = new Intent(getContext().getApplicationContext(),
                    SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        }
    }
}
