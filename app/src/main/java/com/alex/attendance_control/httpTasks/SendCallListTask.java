package com.alex.attendance_control.httpTasks;

import android.os.AsyncTask;

import com.alex.attendance_control.CallListActivity;
import com.alex.attendance_control.callbacks.AsyncResponse;
import com.alex.attendance_control.models.ApiResponse;
import com.alex.attendance_control.models.SchoolClassStudent;
import com.alex.attendance_control.toasts.ApiErrorMessageToast;
import com.alex.attendance_control.utils.SessionStorage;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendCallListTask extends AsyncTask<List<SchoolClassStudent>, Void, ApiResponse> {

    private CallListActivity callListActivity;
    private AsyncResponse delegate;

    public SendCallListTask(CallListActivity activity, AsyncResponse delegate) {
        this.callListActivity = activity;
        this.delegate = delegate;
    }


    @Override
    protected ApiResponse doInBackground(List<SchoolClassStudent>... lists) {

        List<SchoolClassStudent> callList = lists[0];

        //http://192.168.0.102:5000/api/call-list
        HttpUrl httpUrl = new HttpUrl.Builder().scheme("http")
                .host("192.168.0.102")
                .port(5000)
                .addPathSegment("api")
                .addPathSegment("call-list")
                .build();

        //BODY

        String json = new Gson().toJson(callList);

        MediaType type = MediaType.parse("Application/json");
        RequestBody body = RequestBody.create(json, type);

        //Autorización
        String role = SessionStorage.role;
        String token = SessionStorage.token;

        //Peticion HTTP POST
        Request request = new Request.Builder()
                .url(httpUrl)
                .post(body)
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

            //En caso de error,se muestra un toast con un mensaje de error
            this.callListActivity.runOnUiThread(() -> {

                ApiErrorMessageToast.Show(callListActivity);

            });

        }
        return apiResponse;
    }

        @Override
        protected void onPostExecute (ApiResponse apiResponse){


            if (apiResponse != null) {

                //Si el servidor retorna un 200
                if (apiResponse.getStatusCode() == 200) {

                    this.delegate.onSendCallListTaskCompleted(true);

                } //Cualquier otro codigo de error no esperado
                else {

                    //Muestra un mensaje de error
                    ApiErrorMessageToast.Show(callListActivity);

                }

            }


        }
    }
