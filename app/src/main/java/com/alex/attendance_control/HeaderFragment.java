package com.alex.attendance_control;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alex.attendance_control.utils.SessionStorage;


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


    @Override
    public void onClick(View v) {

        if(v.equals(labelSession)){
            SessionStorage.delete();
            labelSession.setVisibility(View.INVISIBLE);
            startActivity(new Intent(this.getContext(), SignInActivity.class));
        }
    }
}
