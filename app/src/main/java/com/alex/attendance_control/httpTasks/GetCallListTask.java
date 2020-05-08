package com.alex.attendance_control.httpTasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.alex.attendance_control.CallListActivity;
import com.alex.attendance_control.callbacks.AsyncResponse;
import com.alex.attendance_control.models.ApiResponse;
import com.alex.attendance_control.models.SchoolClassStudent;
import com.alex.attendance_control.toasts.ApiErrorMessageToast;
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
    public AsyncResponse delegate=null;
    public GetCallListTask(CallListActivity activity,AsyncResponse delegate) {

        this.callListActivity = activity;
        this.delegate = delegate;

    }

    @Override
    protected void onPreExecute() {


    }

    @Override
    protected ApiResponse doInBackground(int[]... params) {

        int[] schoolClassIds = params[0];

        //http://192.168.0.102:5000/api/call-list
        HttpUrl.Builder builder = new HttpUrl.Builder().scheme("http")
                .host("192.168.0.102")
                .port(5000)
                .addPathSegment("api")
                .addPathSegment("call-list");

        //Agrega el array de ids a los parametros de url
        for (int id : schoolClassIds) {
            builder.addQueryParameter("schoolClassIds", id + "");
        }

        HttpUrl httpUrl = builder.build();

        //Autorización
        String role = SessionStorage.role;
        String token = SessionStorage.token;

        //Peticion HTTP GET
        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Role", role)
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

            //En caso de erro,se muestra un toast con un mensaje de error
            this.callListActivity.runOnUiThread(() -> {

                ApiErrorMessageToast.Show(callListActivity);



            });
        }

        return apiResponse;

    }


    @Override
    protected void onPostExecute(ApiResponse apiResponse) {



        if (apiResponse != null) {

            //Si el servidor retorna un 200
            if (apiResponse.getStatusCode() == 200) {

                //Guardo la respuesta en una lista de tipo SchoolClassStudent
                Type listType = new TypeToken<ArrayList<SchoolClassStudent>>() {
                }.getType();

                List<SchoolClassStudent> callList = new Gson()
                        .fromJson(apiResponse.getData(), listType);

                Log.d("PRUEBA", callList.get(0).getSchoolClassId()+"");
                this.delegate.onGetCallListTaskCompleted(callList);

            } //Cualquier otro codigo de error no esperado
            else {

                //Muestra un mensaje de error
                ApiErrorMessageToast.Show(callListActivity);

            }

        }
    }

}