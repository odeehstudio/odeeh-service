package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.AttendanceJpaRepository;
import be.odeeh.studio.odeehservice.application.model.Attendance;
import be.odeeh.studio.odeehservice.application.port.AttendanceServicePort;
import be.odeeh.studio.odeehservice.application.port.BaseUserRepositoryPort;
import be.odeeh.studio.odeehservice.application.port.EventRepositoryPort;
import be.odeeh.studio.odeehservice.domain.entity.AttendanceEntity;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import be.odeeh.studio.odeehservice.domain.entity.EventEntity;
import be.odeeh.studio.odeehservice.domain.exception.OdeehBadRequestException;
import be.odeeh.studio.odeehservice.domain.exception.OdeehDuplicateException;
import be.odeeh.studio.odeehservice.domain.exception.OdeehNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class AttendanceService implements AttendanceServicePort {

    private final AttendanceJpaRepository repository;
    private final BaseUserRepositoryPort baseUserRepository;
    private final EventRepositoryPort eventRepository;

    @Override
    public AttendanceEntity createAttendance(String authenticatedProviderUid, Attendance attendance) {
        BaseUserEntity authenticatedUser = baseUserRepository.findByProviderUid(authenticatedProviderUid);
        EventEntity eventEntity = eventRepository.findById(attendance.eventId());

        if (repository.existsByEventIdAndBaseUserId(eventEntity.getId(), authenticatedUser.getId())) {
            throw new OdeehDuplicateException();
        }

        AttendanceEntity entity = AttendanceEntity.builder()
                .eventId(eventEntity.getId())
                .baseUserId(authenticatedUser.getId())
                .score(attendance.score())
                .hasPictures(Boolean.FALSE)
                .description(attendance.description())
                .friends(map(attendance))
                .build();

        return repository.save(entity);
    }

    @Override
    public AttendanceEntity updateAttendance(String authenticatedProviderUid, UUID id, Attendance attendance) {
        BaseUserEntity authenticatedUser = baseUserRepository.findByProviderUid(authenticatedProviderUid);
        AttendanceEntity entity = repository.findById(id).orElseThrow(OdeehNotFoundException::new);

        if (!entity.getBaseUserId().equals(authenticatedUser.getId())) throw new OdeehBadRequestException();

        entity.setScore(attendance.score());
        entity.setDescription(attendance.description());
        entity.setFriends(map(attendance));

        return repository.save(entity);
    }

    @Override
    public void deleteAttendance(String authenticatedProviderUid, UUID id) {
        BaseUserEntity authenticatedUser = baseUserRepository.findByProviderUid(authenticatedProviderUid);
        AttendanceEntity entity = repository.findById(id).orElseThrow(OdeehNotFoundException::new);

        if (!entity.getBaseUserId().equals(authenticatedUser.getId())) throw new OdeehBadRequestException();

        repository.delete(entity);
    }

    private String map(Attendance attendance) {
        return attendance.friends()
                .stream()
                .map(baseUserRepository::findById)
                .map(e -> e.getId().toString())
                .collect(Collectors.joining(","));
    }
}
