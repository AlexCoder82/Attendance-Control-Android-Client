package com.alex.attendance_control.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.alex.attendance_control.R;
import com.alex.attendance_control.activities.ClassListActivity;
import com.alex.attendance_control.callbacks.SignInAsyncCallback;
import com.alex.attendance_control.httpTasks.SignInTask;
import com.alex.attendance_control.models.TeacherSignInResponse;
import com.alex.attendance_control.toasts.ApiErrorMessageToast;
import com.alex.attendance_control.toasts.WrongDniMessageToast;
import com.alex.attendance_control.utils.SessionStorage;

/*
    Actividad de conexión
 */
public class SignInActivity extends AppCompatActivity implements SignInAsyncCallback, View.OnClickListener {

    private EditText textDni;
    private Button buttonSignIn;
    private ProgressBar loading_spinner;//Ruedecita de carga

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        this.setTitle("Envío De Ausencias");

        textDni = findViewById(R.id.textDni);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(this);
        loading_spinner = findViewById(R.id.loading_spinner);

    }


    @Override
    public void onClick(View v) {

        //Al pulsar "Conectarse"
        if (v.equals(buttonSignIn)) {

            String dni = textDni.getText().toString();

            //Oculta el botón y muestra la rueda de carga
            buttonSignIn.setVisibility(View.INVISIBLE);
            loading_spinner.setVisibility(View.VISIBLE);

            //Ejecuta el hilo de petición de login
            new SignInTask(this,this).execute(dni);

        }

    }

    //Respuesta de la peticion
    @Override
    public void OnSignInTaskCompleted(TeacherSignInResponse data) {

        //Guardo los datos de la sesion en el objeto SessionStorage
        SessionStorage.token = data.getToken();
        SessionStorage.teacherId = data.getTeacherId();
        SessionStorage.firstName = data.getFirstName();

        //Abre la actividad de la lista de clases pasandole la lista como
        //parametro serializado
        Intent intent = new Intent(this, ClassListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("schoolClassList", data.getSchoolClasses());
        intent.putExtras(bundle);
        startActivity(intent);

    }

    //En caso de error de credenciales
    @Override
    public void OnWrongCredentials(String message) {

        //Muestra mensaje del servidor
        WrongDniMessageToast.Show(this,message);

        //Oculta la rueda de carga y vuelve a mostrar el botón
        buttonSignIn.setVisibility(View.VISIBLE);
        loading_spinner.setVisibility(View.INVISIBLE);
    }

    //En caso de error no esperado
    @Override
    public void OnServerError() {

        //Muestra mensaje de error
        ApiErrorMessageToast.Show(this);

        //Oculta la rueda de carga y vuelve a mostrar el botón
        buttonSignIn.setVisibility(View.VISIBLE);
        loading_spinner.setVisibility(View.INVISIBLE);

    }
}
