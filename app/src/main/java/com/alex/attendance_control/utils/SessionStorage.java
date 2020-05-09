package com.alex.attendance_control.utils;

/*
    Objeto que contiene los datos de sesion de usuario
 */
public class SessionStorage {

    public static String token = "";
    public static int teacherId ;
    public static String firstName ;
    public static String role ;

    //Cierra la sesion
    public static void closeSession(){

        token = "";
        teacherId = 0;
        firstName ="";
        role = "";

    }

}
