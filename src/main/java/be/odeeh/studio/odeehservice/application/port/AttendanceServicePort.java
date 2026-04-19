package be.odeeh.studio.odeehservice.application.port;

import be.odeeh.studio.odeehservice.application.model.Attendance;
import be.odeeh.studio.odeehservice.domain.entity.AttendanceEntity;

public interface AttendanceServicePort {
    AttendanceEntity createAttendance(String authenticatedProviderUid, Attendance attendance);
}
