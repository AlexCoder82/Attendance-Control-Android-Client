package com.alex.attendance_control.models;

import java.io.Serializable;

public class SchoolClass implements Serializable {

    private int id;
    private int dayOfWeek;
    private Subject subject;
    private Schedule schedule;

    public SchoolClass(int id, int dayOfWeek, Subject subject, Schedule schedule) {
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.subject = subject;
        this.schedule = schedule;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }
}
