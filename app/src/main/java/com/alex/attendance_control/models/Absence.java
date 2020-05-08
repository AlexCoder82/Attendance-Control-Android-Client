package com.alex.attendance_control.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Absence {

    private int id;
    private int type;
    private String date;
    private  Schedule schedule;
    private Subject subject;

    public Absence(int id, int type, String date, Schedule schedule, Subject subject) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.schedule = schedule;
        this.subject = subject;
    }

    public Absence(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
