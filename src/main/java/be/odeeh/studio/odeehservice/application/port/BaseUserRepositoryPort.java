package be.odeeh.studio.odeehservice.application.port;

import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;

import java.util.UUID;

public interface BaseUserRepositoryPort {
    BaseUserEntity findForAuthenticatedBaseUser(String authenticatedProviderUid);

    BaseUserEntity findById(UUID id);
}
