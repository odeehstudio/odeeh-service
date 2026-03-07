package be.odeeh.studio.odeehservice.adapter.out.repository;

import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BaseUserRepository extends JpaRepository<BaseUserEntity, UUID> {

    boolean existsByEmailOrProviderUid(String email, String uid);
}
