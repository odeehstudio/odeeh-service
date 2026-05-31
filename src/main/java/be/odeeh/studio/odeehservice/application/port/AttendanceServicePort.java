package be.odeeh.studio.odeehservice.application.port;

import be.odeeh.studio.odeehservice.application.model.Attendance;
import be.odeeh.studio.odeehservice.domain.entity.AttendanceEntity;
import be.odeeh.studio.odeehservice.domain.model.AttendanceEntityQuery;

import java.util.List;
import java.util.UUID;

public interface AttendanceServicePort {
    AttendanceEntity createAttendance(String uid, Attendance attendance);

    AttendanceEntity updateAttendance(String uid, UUID id, Attendance attendance);

    void deleteAttendance(String uid, UUID id);

    List<AttendanceEntityQuery> fetchAttendances(String uid, Integer page);
}
