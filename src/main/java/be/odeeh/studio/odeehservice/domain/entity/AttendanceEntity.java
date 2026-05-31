package be.odeeh.studio.odeehservice.domain.entity;

import be.odeeh.studio.odeehservice.domain.event.attendance.AttendanceCreatedEvent;
import be.odeeh.studio.odeehservice.domain.event.attendance.AttendanceDeletedEvent;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "attendance")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceEntity extends AbstractAggregateRoot<AttendanceEntity> {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private UUID eventId;

    @Column(nullable = false)
    private UUID baseUserId;

    @Column(nullable = false)
    private BigDecimal score;

    @Column
    private String description;

    @Column(nullable = false)
    private Boolean hasPictures;

    @OneToMany(mappedBy = "attendance", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AttendanceTaggedBaseUserEntity> taggedBaseUsers = new ArrayList<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void registerCreatedEvent() {
        registerEvent(AttendanceCreatedEvent.builder()
                .baseUserId(this.baseUserId)
                .attendanceId(this.id)
                .build()
        );

    }

    public void registerDeletedEvent() {
        registerEvent(AttendanceDeletedEvent.builder()
                .attendanceId(this.id)
                .build()
        );

    }
}
