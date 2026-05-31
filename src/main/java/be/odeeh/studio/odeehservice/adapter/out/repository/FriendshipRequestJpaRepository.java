package be.odeeh.studio.odeehservice.adapter.out.repository;

import be.odeeh.studio.odeehservice.domain.entity.FriendshipEntity;
import be.odeeh.studio.odeehservice.domain.model.FriendshipEntityQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FriendshipRequestJpaRepository extends JpaRepository<FriendshipEntity, UUID> {

    boolean existsByRequesterIdAndReceiverId(UUID requesterId, UUID receiverId);

    List<FriendshipEntity> findByRequesterIdOrReceiverId(UUID requesterId, UUID receiverId);

    @Query("""
    SELECT new be.odeeh.studio.odeehservice.domain.model.FriendshipEntityQuery(
        CASE WHEN fr.requesterId = :userId THEN fr.receiverId ELSE fr.requesterId END,
        u.username
    )
    FROM FriendshipEntity fr
    JOIN BaseUserEntity u ON u.id = CASE
        WHEN fr.requesterId = :userId THEN fr.receiverId
        ELSE fr.requesterId
    END
    WHERE fr.receiverId = :userId OR fr.requesterId = :userId
    """)
    List<FriendshipEntityQuery> findByUser(@Param("userId") UUID userId);
}
