package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.BaseUserJpaRepository;
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
    public BaseUserEntity createBaseUser(String email, String uid) {
        if (repository.existsByEmailOrProviderUid(email, uid)) {
            throw new OdeehDuplicateException();
        }

        BaseUserEntity entity = BaseUserEntity.builder()
                .email(email)
                .providerUid(uid)
                .build();

        return repository.save(entity);
    }
}
