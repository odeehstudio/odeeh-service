package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.BaseUserJpaRepository;
import be.odeeh.studio.odeehservice.application.model.Username;
import be.odeeh.studio.odeehservice.application.port.BaseUserServicePort;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import be.odeeh.studio.odeehservice.domain.exception.OdeehDuplicateException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BaseUserService implements BaseUserServicePort {

    private final BaseUserJpaRepository repository;

    @Override
    public Boolean isUserEnrolled(String uid) {
        return repository.findByProviderUid(uid).isPresent();
    }

    @Override
    public Boolean isUsernameAvailable(Username request) {
        return !repository.existsByUsername(request.value());
    }

    @Override
    public BaseUserEntity enroll(String uid, Username request) {
        if (repository.existsByUsernameOrProviderUid(request.value(), uid)) {
            throw new OdeehDuplicateException();
        }

        BaseUserEntity entity = BaseUserEntity.builder()
                .providerUid(uid)
                .username(request.value())
                .build();

        return repository.save(entity);
    }
}
