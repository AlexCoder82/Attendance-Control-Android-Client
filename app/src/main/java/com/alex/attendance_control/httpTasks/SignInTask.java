package com.alex.attendance_control.httpTasks;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import com.alex.attendance_control.R;
import com.alex.attendance_control.activities.SignInActivity;
import com.alex.attendance_control.callbacks.SignInAsyncCallback;
import com.alex.attendance_control.models.ApiResponse;
import com.alex.attendance_control.models.TeacherSignInResponse;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/*
    Petición http de conexión
 */
public class SignInTask extends AsyncTask<String, Void, ApiResponse> {

    @SuppressLint("StaticFieldLeak")
    private SignInActivity signInActivity;//Actividad que lanza el hilo
    private SignInAsyncCallback delegate;//Callback

    public SignInTask(SignInActivity activity, SignInAsyncCallback delegate) {
        this.signInActivity = activity;
        this.delegate = delegate;
    }

    @Override
    protected ApiResponse doInBackground(String... strings) {

        String host = this.signInActivity.getResources().getString(R.string.host);
        int port = Integer.parseInt(this.signInActivity.getResources().getString(R.string.port));
        String scheme = this.signInActivity.getResources().getString(R.string.scheme);
        String api = this.signInActivity.getResources().getString(R.string.api);
        String teachers = this.signInActivity.getResources().getString(R.string.teachers);
        String signIn = this.signInActivity.getResources().getString(R.string.sign_in);
        String dni = strings[0];//dni

        HttpUrl url = new HttpUrl.Builder()
                .scheme(scheme)
                .host(host)
                .port(port)
                .addPathSegment(api)
                .addPathSegment(teachers)
                .addPathSegment(signIn)
                .addPathSegment(dni) //Se pasa el dni en la url
                .build();//http://ip:5000/api/teachers/sign-in/{dni}


        //Body vacío
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

            this.signInActivity.runOnUiThread(()->{
                this.delegate.OnServerError();//callback de error no esperado
            });
        }

        return apiResponse;

    }


    @Override
    protected void onPostExecute(ApiResponse apiResponse) {

        if (apiResponse != null) {
            //El servidor retorna un 404 si no reconoce el dni del profesor
            if (apiResponse.getStatusCode() == 404) {

                //Callback de error de credenciales
                this.delegate.OnWrongCredentials(apiResponse.getData());


            } //Si el servidor retorna un 200
            else if (apiResponse.getStatusCode() == 200) {

                //Guardo la respuesta en un objeto TeacherSignInResponse
                TeacherSignInResponse data = new Gson()
                        .fromJson(apiResponse.getData(), TeacherSignInResponse.class);

                //Callback de la respuesta
                this.delegate.OnSignInTaskCompleted(data);
            }
            //Cualquier otro codigo de error no esperado
            else {

                this.delegate.OnServerError();//Callback de error no esperado

            }
        }

    }
}