package be.odeeh.studio.odeehservice.adapter.out.repository;

import be.odeeh.studio.odeehservice.domain.entity.FriendshipRequestEntity;
import be.odeeh.studio.odeehservice.domain.entity.FriendshipRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FriendshipRequestJpaRepository extends JpaRepository<FriendshipRequestEntity, UUID> {

    boolean existsByRequesterIdAndReceiverId(UUID requesterId, UUID receiverId);

    List<FriendshipRequestEntity> findByReceiverIdAndStatus(UUID receiverId, FriendshipRequestStatus status);

    @Query("""
    SELECT fr FROM FriendshipRequestEntity fr
    WHERE (fr.receiverId = :userId OR fr.requesterId = :userId)
    AND fr.status = :status
    """)
    List<FriendshipRequestEntity> findByUserAndStatus(
            @Param("userId") UUID userId,
            @Param("status") FriendshipRequestStatus status
    );
}
