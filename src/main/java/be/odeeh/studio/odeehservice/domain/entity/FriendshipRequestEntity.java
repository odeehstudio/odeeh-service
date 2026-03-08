package be.odeeh.studio.odeehservice.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "friendship_request")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendshipRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID requesterId;

    @Column(nullable = false)
    private UUID receiverId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FriendshipRequestStatus status;

    @Column
    private LocalDateTime respondedAt;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void acceptFriendshipRequest() {
        this.status = FriendshipRequestStatus.ACCEPTED;
        this.respondedAt = LocalDateTime.now();
    }
}
