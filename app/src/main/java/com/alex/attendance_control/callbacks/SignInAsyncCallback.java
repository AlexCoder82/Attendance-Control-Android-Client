package com.alex.attendance_control.callbacks;

import com.alex.attendance_control.models.TeacherSignInResponse;

public interface SignInAsyncCallback {

    void OnSignInTaskCompleted(TeacherSignInResponse teacherSignInResponse);
    void OnWrongCredentials(String message);
    void OnServerError();
}
