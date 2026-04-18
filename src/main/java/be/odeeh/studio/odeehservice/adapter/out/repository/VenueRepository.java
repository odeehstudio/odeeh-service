package be.odeeh.studio.odeehservice.adapter.out.repository;

import be.odeeh.studio.odeehservice.application.port.VenueRepositoryPort;
import be.odeeh.studio.odeehservice.domain.entity.VenueEntity;
import be.odeeh.studio.odeehservice.domain.exception.OdeehNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class VenueRepository implements VenueRepositoryPort {

    private final VenueJpaRepository repository;

    @Override
    public VenueEntity findById(UUID id) {
        return repository.findById(id).orElseThrow(OdeehNotFoundException::new);
    }
}
