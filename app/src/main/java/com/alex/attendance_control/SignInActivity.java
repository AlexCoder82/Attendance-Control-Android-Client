package com.alex.attendance_control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.alex.attendance_control.models.ApiResponse;
import com.alex.attendance_control.models.TeacherSignInResponse;
import com.alex.attendance_control.toasts.ApiErrorMessageToast;
import com.alex.attendance_control.toasts.WrongDniMessageToast;
import com.alex.attendance_control.utils.SessionStorage;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
    Actividad de conexión
 */
public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textDni;
    private Button buttonSignIn;
    private ProgressBar loading_spinner;

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
            String url = getString(R.string.sign_in_url);

            //Ejecuta el hilo de petición de login
            new SignInTask(this).execute(url, dni);

        }

    }

    // Hilo de petición de login
    private class SignInTask extends AsyncTask<String, Void, ApiResponse> {

        private Context context;//SignInActivity

        public SignInTask(Context context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {

            //Oculta el botón y muestra la rueda de carga
            buttonSignIn.setVisibility(View.INVISIBLE);
            loading_spinner.setVisibility(View.VISIBLE);

        }

        @Override
        protected ApiResponse doInBackground(String... strings) {

            String dni = strings[1];//dni

            /* URL http://192.168.0.102:5000/api/teachers/sign-in/dni */
            String url =  strings[0] + "/" + dni;


            //Body vacío, se pasa el dni en la url
            RequestBody body = RequestBody
                    .create(null, new byte[]{});

            //Peticion HTTP POST
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            ApiResponse apiResponse = null;

            try {

                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();

                //Guardo el codigo de respuesta y la respuesta en un objeto ApiResponse
                int statusCode = response.code();
                String message = response.body().string();
                apiResponse = new ApiResponse(message, statusCode);


            } catch (IOException e) {

                //En caso de error muestro un toast con un mensaje de error
                runOnUiThread(new Runnable() {
                    public void run() {
                        ApiErrorMessageToast.Show(context);
                        //Oculta la rueda de carga y vuelve a mostrar el botón
                        buttonSignIn.setVisibility(View.VISIBLE);
                        loading_spinner.setVisibility(View.INVISIBLE);
                    }
                });

            }

            return apiResponse;

        }


        @Override
        protected void onPostExecute(ApiResponse apiResponse) {



            if (apiResponse != null) {

                //El servidor retorna un 404 si no reconoce el dni del profesor
                if (apiResponse.getStatusCode() == 404) {

                    //Muestra mensaje del servidor
                    WrongDniMessageToast.Show(this.context, apiResponse.getData());
                    //Oculta la rueda de carga y vuelve a mostrar el botón
                    buttonSignIn.setVisibility(View.VISIBLE);
                    loading_spinner.setVisibility(View.INVISIBLE);

                } //Si el servidor retorna un 200
                else if (apiResponse.getStatusCode() == 200) {

                    //Guardo la respuesta en un objeto TeacherSignInResponse
                    TeacherSignInResponse data = new Gson()
                            .fromJson(apiResponse.getData(), TeacherSignInResponse.class);

                    //Guardo los datos de la sesion en el objeto SessionStorage
                    SessionStorage.token = data.getToken();
                    SessionStorage.teacherId = data.getTeacherId();
                    SessionStorage.role = data.getRole();
                    SessionStorage.firstName = data.getFirstName();

                    //Abre la actividad de la lista de clases pasandole la lista como
                    //parametro serializado
                    Intent intent = new Intent(this.context, ClassListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("schoolClassList", data.getSchoolClasses());
                    intent.putExtras(bundle);
                    startActivity(intent);

                } //Cualquier otro codigo de error no esperado
                else {

                    //Muestra un mensaje de error
                    ApiErrorMessageToast.Show(context);

                }

            }

        }
    }
}
