package com.alex.attendance_control.models;

public class SchoolClassStudent {

    private Student student;
    private int schoolClassId;
    private Absence absence;


    public SchoolClassStudent( Student student,int schoolClassId, Absence absence) {
        this.schoolClassId = schoolClassId;
        this.absence = absence;
        this.student = student;
    }

    public int getSchoolClassId() {
        return schoolClassId;
    }

    public void setSchoolClassId(int schoolClassId) {
        this.schoolClassId = schoolClassId;
    }

    public Absence getAbsence() {
        return absence;
    }

    public void setAbsence(Absence absence) {
        this.absence = absence;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
