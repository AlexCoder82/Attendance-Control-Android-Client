package com.alex.attendance_control.httpTasks;

import android.os.AsyncTask;

import com.alex.attendance_control.activities.CallListActivity;
import com.alex.attendance_control.R;
import com.alex.attendance_control.callbacks.CallListAsyncCallback;
import com.alex.attendance_control.models.ApiResponse;
import com.alex.attendance_control.models.SchoolClassStudent;
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

/*
    Peticion http de envio del listado
 */
public class SendCallListTask extends AsyncTask<List<SchoolClassStudent>, Void, ApiResponse> {

    private CallListActivity callListActivity;
    private CallListAsyncCallback delegate;

    public SendCallListTask(CallListActivity activity, CallListAsyncCallback delegate) {
        this.callListActivity = activity;
        this.delegate = delegate;
    }

    @Override
    protected ApiResponse doInBackground(List<SchoolClassStudent>... lists) {

        //http://192.168.0.102:5000/api/call-list
        String host = this.callListActivity.getResources().getString(R.string.host);
        int port = Integer
                .parseInt(this.callListActivity.getResources().getString(R.string.port));
        String scheme = this.callListActivity.getResources().getString(R.string.scheme);
        String api = this.callListActivity.getResources().getString(R.string.api);
        String call_list = this.callListActivity.getResources().getString(R.string.call_list);

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme(scheme)
                .host(host)
                .port(port)
                .addPathSegment(api)
                .addPathSegment(call_list)
                .build();

        //BODY
        List<SchoolClassStudent> callList = lists[0];
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

            this.callListActivity.runOnUiThread(()->{
                delegate.OnServerError();
            });


        }
        return apiResponse;
    }

    @Override
    protected void onPostExecute(ApiResponse apiResponse) {

        if (apiResponse != null) {

            //Si el servidor retorna un 200
            if (apiResponse.getStatusCode() == 200) {

                this.delegate.onSendCallListTaskCompleted();

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
