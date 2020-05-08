package com.alex.attendance_control.callbacks;

import com.alex.attendance_control.models.SchoolClassStudent;

import java.util.List;

public interface AsyncResponse {

    void onGetCallListTaskCompleted(List<SchoolClassStudent> list);
    void onSendCallListTaskCompleted(boolean result);
}
