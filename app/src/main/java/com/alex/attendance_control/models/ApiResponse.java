package com.alex.attendance_control.models;

public class ApiResponse {

    private String data;
    private int statusCode;

    public ApiResponse(String data, int statusCode) {
        this.data = data;
        this.statusCode = statusCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
