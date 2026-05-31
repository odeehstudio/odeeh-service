package be.odeeh.studio.odeehservice.domain.event.attendance;

import be.odeeh.studio.odeehservice.adapter.out.repository.FeedActivityJpaRepository;
import be.odeeh.studio.odeehservice.adapter.out.repository.FriendshipRequestJpaRepository;
import be.odeeh.studio.odeehservice.domain.entity.FeedActivityEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AttendanceEventListener {

    private final FeedActivityJpaRepository repository;
    private final FriendshipRequestJpaRepository friendshipRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onAttendanceCreated(AttendanceCreatedEvent event) {
        List<UUID> ids = new ArrayList<>(friendshipRepository.findByRequesterIdOrReceiverId(event.getBaseUserId(), event.getBaseUserId()).stream()
                .map(e -> e.getReceiverId().equals(event.getBaseUserId()) ? e.getRequesterId() : e.getReceiverId())
                .toList());

        ids.add(event.getBaseUserId());

        List<FeedActivityEntity> entities = ids.stream()
                        .map(id -> FeedActivityEntity.builder()
                                .attendanceId(event.getAttendanceId())
                                .baseUserId(id)
                                .build())
                        .toList();

        repository.saveAll(entities);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onAttendanceDeleted(AttendanceDeletedEvent event) {
        repository.deleteAllByAttendanceId(event.getAttendanceId());
    }
}
