package be.odeeh.studio.odeehservice.domain.event.attendance;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@RequiredArgsConstructor
public class AttendanceCreatedEvent {
    private final UUID baseUserId;
    private final UUID attendanceId;
}
