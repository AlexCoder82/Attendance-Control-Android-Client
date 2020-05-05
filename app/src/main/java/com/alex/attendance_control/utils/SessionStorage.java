package com.alex.attendance_control.utils;

public class SessionStorage {

    public static String token = "";
    public static int teacherId ;
    public static String firstName ;
    public static String role ;

    public static void delete(){

        token = "";
        teacherId = 0;
        firstName ="";
        role = "";

    }
}
