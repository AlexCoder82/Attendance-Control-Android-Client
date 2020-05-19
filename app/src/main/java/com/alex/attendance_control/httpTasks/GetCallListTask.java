package com.alex.attendance_control.httpTasks;

import android.os.AsyncTask;

import com.alex.attendance_control.activities.CallListActivity;
import com.alex.attendance_control.R;
import com.alex.attendance_control.callbacks.CallListAsyncCallback;
import com.alex.attendance_control.models.ApiResponse;
import com.alex.attendance_control.models.SchoolClassStudent;
import com.alex.attendance_control.utils.SessionStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
    Peticion http para el listado de alumnos
 */
public class GetCallListTask extends AsyncTask<int[], Void, ApiResponse> {

    private CallListActivity callListActivity;
    public CallListAsyncCallback delegate;

    public GetCallListTask(CallListActivity activity, CallListAsyncCallback delegate) {

        this.callListActivity = activity;
        this.delegate = delegate;

    }


    @Override
    protected ApiResponse doInBackground(int[]... params) {

        //http://192.168.0.102:5000/api/call-list/
        String host = this.callListActivity.getResources().getString(R.string.host);
        int port = Integer
                .parseInt(this.callListActivity.getResources().getString(R.string.port));
        String scheme = this.callListActivity.getResources().getString(R.string.scheme);
        String api = this.callListActivity.getResources().getString(R.string.api);
        String callList = this.callListActivity.getResources().getString(R.string.call_list);

        HttpUrl.Builder builder = new HttpUrl.Builder()
                .scheme(scheme)
                .host(host)
                .port(port)
                .addPathSegment(api)
                .addPathSegment(callList);

        int[] schoolClassIds = params[0];
        //Agrega el array de ids a los parametros de url
        for (int id : schoolClassIds) {
            builder.addQueryParameter("schoolClassIds", id + "");
        }

        HttpUrl httpUrl = builder.build();

        //Autorización jwt
        String token = SessionStorage.token;

        //Peticion HTTP GET
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .addHeader("Authorization", "Bearer " + token)
                .build();

        ApiResponse apiResponse = null;

        try {

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();

            //Guarda el codigo de respuesta y la respuesta en un objeto ApiResponse
            int statusCode = response.code();
            String message = response.body().string();
            apiResponse = new ApiResponse(message, statusCode);


        } catch (IOException e) {

            this.callListActivity.runOnUiThread(() -> {
                delegate.OnServerError();
            });


        }

        return apiResponse;

    }


    @Override
    protected void onPostExecute(ApiResponse apiResponse) {

        if (apiResponse != null)
        {
            //Si el servidor retorna un 200
            if (apiResponse.getStatusCode() == 200) {

                //Guardo la respuesta en una lista de tipo SchoolClassStudent
                Type listType = new TypeToken<ArrayList<SchoolClassStudent>>() {
                }.getType();

                List<SchoolClassStudent> callList = new Gson()
                        .fromJson(apiResponse.getData(), listType);

                this.delegate.onGetCallListTaskCompleted(callList);

            } else if (apiResponse.getStatusCode() == 401) {

                this.delegate.OnSessionExpiredError();

            }
            //Cualquier otro codigo de error no esperado
            else {

                delegate.OnServerError();
            }
        }

    }

}