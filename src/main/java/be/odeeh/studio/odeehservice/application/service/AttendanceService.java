package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.AttendanceJpaRepository;
import be.odeeh.studio.odeehservice.application.model.Attendance;
import be.odeeh.studio.odeehservice.application.port.AttendanceServicePort;
import be.odeeh.studio.odeehservice.application.port.BaseUserRepositoryPort;
import be.odeeh.studio.odeehservice.application.port.EventRepositoryPort;
import be.odeeh.studio.odeehservice.domain.entity.AttendanceEntity;
import be.odeeh.studio.odeehservice.domain.entity.AttendanceTaggedBaseUserEntity;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import be.odeeh.studio.odeehservice.domain.entity.EventEntity;
import be.odeeh.studio.odeehservice.domain.exception.OdeehBadRequestException;
import be.odeeh.studio.odeehservice.domain.exception.OdeehDuplicateException;
import be.odeeh.studio.odeehservice.domain.exception.OdeehNotFoundException;
import be.odeeh.studio.odeehservice.domain.model.AttendanceEntityQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class AttendanceService implements AttendanceServicePort {

    private final AttendanceJpaRepository repository;
    private final BaseUserRepositoryPort baseUserRepository;
    private final EventRepositoryPort eventRepository;

    @Override
    public AttendanceEntity createAttendance(String uid, Attendance attendance) {
        BaseUserEntity authenticatedUser = baseUserRepository.findByProviderUid(uid);
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
                .build();

        entity.setTaggedBaseUsers(map(attendance, entity));

        return repository.save(entity);
    }

    @Override
    public AttendanceEntity updateAttendance(String uid, UUID id, Attendance attendance) {
        BaseUserEntity authenticatedUser = baseUserRepository.findByProviderUid(uid);
        AttendanceEntity entity = repository.findById(id).orElseThrow(OdeehNotFoundException::new);

        if (!entity.getBaseUserId().equals(authenticatedUser.getId())) throw new OdeehBadRequestException();

        entity.setScore(attendance.score());
        entity.setDescription(attendance.description());
        entity.setTaggedBaseUsers(map(attendance, entity));

        return repository.save(entity);
    }

    @Override
    public void deleteAttendance(String uid, UUID id) {
        BaseUserEntity authenticatedUser = baseUserRepository.findByProviderUid(uid);
        AttendanceEntity entity = repository.findById(id).orElseThrow(OdeehNotFoundException::new);

        if (!entity.getBaseUserId().equals(authenticatedUser.getId())) throw new OdeehBadRequestException();

        repository.delete(entity);
    }

    @Override
    public List<AttendanceEntityQuery> fetchAttendances(String uid) {
        BaseUserEntity authenticatedUser = baseUserRepository.findByProviderUid(uid);

        return repository.findAttendancesByUserId(authenticatedUser.getId());
    }

    private List<AttendanceTaggedBaseUserEntity> map(Attendance attendance, AttendanceEntity entity) {
        return baseUserRepository.findAllById(attendance.friends())
                .stream()
                .map(e -> AttendanceTaggedBaseUserEntity.builder().attendance(entity).baseUserId(e.getId()).build())
                .toList();
    }
}
