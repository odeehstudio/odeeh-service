package be.odeeh.studio.odeehservice.adapter.out.repository;

import be.odeeh.studio.odeehservice.application.port.BaseUserRepositoryPort;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import be.odeeh.studio.odeehservice.domain.exception.OdeehNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class BaseUserRepository implements BaseUserRepositoryPort {

    private final BaseUserJpaRepository repository;

    @Override
    public BaseUserEntity findByProviderUid(String authenticatedProviderUid) {
        return repository.findByProviderUid(authenticatedProviderUid).orElseThrow(OdeehNotFoundException::new);
    }

    @Override
    public BaseUserEntity findById(UUID id) {
        return repository.findById(id).orElseThrow(OdeehNotFoundException::new);
    }

    @Override
    public List<BaseUserEntity> findAllById(List<UUID> ids) {
        return repository.findAllById(ids);
    }
}
