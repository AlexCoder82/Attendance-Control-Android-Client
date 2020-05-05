package com.alex.attendance_control;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.attendance_control.exceptions.APIErrorException;
import com.alex.attendance_control.models.ApiResponse;
import com.alex.attendance_control.models.SchoolClass;
import com.alex.attendance_control.models.TeacherSignInResponse;
import com.alex.attendance_control.toasts.ApiErrorMessageToast;
import com.alex.attendance_control.toasts.WrongDniMessageToast;
import com.alex.attendance_control.utils.SessionStorage;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textDni;
    private Button buttonSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        textDni = findViewById(R.id.textDni);
        buttonSignIn = findViewById(R.id.buttonSignIn);
        buttonSignIn.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {

        if (v.equals(buttonSignIn)) {

            String dni = textDni.getText().toString();
            String url = getString(R.string.sign_in_url);
            new SignInTask(this).execute(url, dni);
        }

    }


    private class SignInTask extends AsyncTask<String, Void, ApiResponse> {

        private Context context;

        public SignInTask(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            buttonSignIn.setEnabled(false);

        }


        @Override
        protected ApiResponse doInBackground(String... strings) {

            String dni = strings[1];

            /* URL http://192.168.0.102:5000/api/teachers/sign-in/dni */
            String url = strings[0] + "/" + dni;

            OkHttpClient client = new OkHttpClient();
            //Body vacío
            RequestBody body = RequestBody
                    .create(null, new byte[]{});

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            ApiResponse apiResponse = null;

            try {

                Response response = client.newCall(request).execute();

                int statusCode = response.code();
                String message = response.body().string();

                apiResponse = new ApiResponse(message, statusCode);


            } catch (IOException e) {

                Log.d("ERROR", e.getMessage());

                runOnUiThread(new Runnable() {
                    public void run() {
                        ApiErrorMessageToast.Show(context);
                    }
                });

            }
            return apiResponse;
        }


        @Override
        protected void onPostExecute(ApiResponse apiResponse) {

            if (apiResponse != null) {

                if (apiResponse.getStatusCode() == 404) {

                    WrongDniMessageToast.Show(this.context, apiResponse.getData());

                } else if (apiResponse.getStatusCode() == 200) {

                    TeacherSignInResponse response = new Gson()
                            .fromJson(apiResponse.getData(), TeacherSignInResponse.class);

                    //Guardo los datos de la sesion en el objeto SessionStorage
                    SessionStorage.token = response.getToken();
                    SessionStorage.teacherId = response.getTeacherId();
                    SessionStorage.role = response.getRole();
                    SessionStorage.firstName = response.getFirstName();

                    Intent intent = new Intent(this.context, ClassListActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("schoolClassList", response
                            .getSchoolClasses());
                    intent.putExtras(bundle);

                    startActivity(intent);
                } else {

                    ApiErrorMessageToast.Show(context);

                }


            }

            buttonSignIn.setEnabled(true);

        }
    }
}
