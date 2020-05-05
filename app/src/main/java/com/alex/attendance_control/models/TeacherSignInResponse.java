package com.alex.attendance_control.models;

import java.io.Serializable;
import java.util.ArrayList;


public class TeacherSignInResponse implements Serializable {

    private int teacherId ;
    private String firstName ;
    private String token ;
    private  String role ;
    private ArrayList<SchoolClass> schoolClasses;

    public TeacherSignInResponse(int teacherId, String firstName, String token, String role, ArrayList<SchoolClass> schoolClasses) {
        this.teacherId = teacherId;
        firstName = firstName;
        token = token;
        role = role;
        schoolClasses = schoolClasses;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ArrayList<SchoolClass> getSchoolClasses() {
        return schoolClasses;
    }

    public void setSchoolClasses(ArrayList<SchoolClass> schoolClasses) {
        this.schoolClasses = schoolClasses;
    }
}
