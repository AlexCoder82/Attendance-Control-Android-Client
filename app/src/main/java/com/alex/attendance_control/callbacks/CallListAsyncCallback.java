package com.alex.attendance_control.callbacks;

import com.alex.attendance_control.models.SchoolClassStudent;
import java.util.List;

public interface CallListAsyncCallback {

      void onGetCallListTaskCompleted(List<SchoolClassStudent> list);
      void onSendCallListTaskCompleted();
      void OnServerError();
      void OnSessionExpiredError();
}
