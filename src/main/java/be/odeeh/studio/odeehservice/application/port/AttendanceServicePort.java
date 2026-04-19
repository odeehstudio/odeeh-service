package be.odeeh.studio.odeehservice.application.port;

import be.odeeh.studio.odeehservice.application.model.Attendance;
import be.odeeh.studio.odeehservice.domain.entity.AttendanceEntity;

import java.util.UUID;

public interface AttendanceServicePort {
    AttendanceEntity createAttendance(String authenticatedProviderUid, Attendance attendance);

    AttendanceEntity updateAttendance(String authenticatedProviderUid, UUID id, Attendance attendance);

    void deleteAttendance(String authenticatedProviderUid, UUID id);
}
