package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.AttendanceJpaRepository;
import be.odeeh.studio.odeehservice.application.model.Attendance;
import be.odeeh.studio.odeehservice.application.port.AttendanceServicePort;
import be.odeeh.studio.odeehservice.application.port.BaseUserRepositoryPort;
import be.odeeh.studio.odeehservice.application.port.EventRepositoryPort;
import be.odeeh.studio.odeehservice.domain.entity.AttendanceEntity;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import be.odeeh.studio.odeehservice.domain.entity.EventEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AttendanceService implements AttendanceServicePort {

    private final AttendanceJpaRepository repository;
    private final BaseUserRepositoryPort baseUserRepository;
    private final EventRepositoryPort eventRepository;

    @Override
    public AttendanceEntity createAttendance(String authenticatedProviderUid, Attendance attendance) {
        BaseUserEntity authenticatedUser = baseUserRepository.findForAuthenticatedBaseUser(authenticatedProviderUid);
        EventEntity eventEntity = eventRepository.findById(attendance.eventId());

        AttendanceEntity entity = AttendanceEntity.builder()
                .eventId(eventEntity.getId())
                .baseUserId(authenticatedUser.getId())
                .score(attendance.score())
                .hasPictures(Boolean.FALSE)
                .description(attendance.description())
                .build();

        return repository.save(entity);
    }
}
