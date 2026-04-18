package be.odeeh.studio.odeehservice.application.port;

import be.odeeh.studio.odeehservice.domain.entity.VenueEntity;

import java.util.UUID;

public interface VenueRepositoryPort {
    VenueEntity findById(UUID id);
}
