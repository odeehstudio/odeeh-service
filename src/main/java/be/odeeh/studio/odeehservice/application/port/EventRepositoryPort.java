package be.odeeh.studio.odeehservice.application.port;

import be.odeeh.studio.odeehservice.domain.entity.EventEntity;

import java.util.UUID;

public interface EventRepositoryPort {
    EventEntity findById(UUID id);
}
