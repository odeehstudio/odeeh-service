package be.odeeh.studio.odeehservice.adapter.out.repository;

import be.odeeh.studio.odeehservice.domain.entity.FriendshipRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FriendshipRequestJpaRepository extends JpaRepository<FriendshipRequestEntity, UUID> {

    boolean existsByRequesterIdAndReceiverId(UUID requesterId, UUID receiverId);

    List<FriendshipRequestEntity> findByReceiverId(UUID receiverId);
}
