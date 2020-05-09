package com.alex.attendance_control.models;

/*
    Objeto Alumno
 */
public class Student {

    private int id;
    private String dni;
    private String firstName;
    private String lastName1;
    private String lastName2;

    public Student(int id, String dni, String firstName, String lastName1, String lastName2) {
        this.id = id;
        this.dni = dni;
        this.firstName = firstName;
        this.lastName1 = lastName1;
        this.lastName2 = lastName2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName1() {
        return lastName1;
    }

    public void setLastName1(String lastName1) {
        this.lastName1 = lastName1;
    }

    public String getLastName2() {
        return lastName2;
    }

    public void setLastName2(String lastName2) {
        this.lastName2 = lastName2;
    }
}
