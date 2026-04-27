package be.odeeh.studio.odeehservice.adapter.out.repository;

import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BaseUserJpaRepository extends JpaRepository<BaseUserEntity, UUID> {

    boolean existsByUsernameOrProviderUid(String username, String uid);

    boolean existsByUsername(String username);

    Optional<BaseUserEntity> findByProviderUid(String uid);
}
