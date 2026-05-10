package be.odeeh.studio.odeehservice.application.port;

import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;

import java.util.List;
import java.util.UUID;

public interface BaseUserRepositoryPort {
    BaseUserEntity findByProviderUid(String uid);

    BaseUserEntity findById(UUID id);

    List<BaseUserEntity> findAllById(List<UUID> ids);
}
