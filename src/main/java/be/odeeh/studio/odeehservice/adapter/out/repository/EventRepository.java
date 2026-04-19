package be.odeeh.studio.odeehservice.adapter.out.repository;

import be.odeeh.studio.odeehservice.application.port.EventRepositoryPort;
import be.odeeh.studio.odeehservice.domain.entity.EventEntity;
import be.odeeh.studio.odeehservice.domain.exception.OdeehNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class EventRepository implements EventRepositoryPort {

    private final EventJpaRepository repository;

    @Override
    public EventEntity findById(UUID id) {
        return repository.findById(id).orElseThrow(OdeehNotFoundException::new);
    }
}
