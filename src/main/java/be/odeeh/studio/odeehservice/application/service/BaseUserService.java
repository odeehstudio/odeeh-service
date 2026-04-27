package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.BaseUserJpaRepository;
import be.odeeh.studio.odeehservice.application.model.BaseUser;
import be.odeeh.studio.odeehservice.application.port.BaseUserServicePort;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEnrollmentStatus;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import be.odeeh.studio.odeehservice.domain.exception.OdeehDuplicateException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class BaseUserService implements BaseUserServicePort {

    private final BaseUserJpaRepository repository;

    @Override
    public BaseUserEnrollmentStatus validateEnrollment(String uid) {
        Optional<BaseUserEntity> authenticatedUser = repository.findByProviderUid(uid);

        return authenticatedUser.isPresent() ?
                BaseUserEnrollmentStatus.ENROLLED :
                BaseUserEnrollmentStatus.NEEDS_ENROLLMENT;
    }

    @Override
    public BaseUserEntity createBaseUser(BaseUser baseUser, String uid) {
        if (repository.existsByUsernameOrProviderUid(baseUser.username(), uid)) {
            throw new OdeehDuplicateException();
        }

        BaseUserEntity entity = BaseUserEntity.builder()
                .providerUid(uid)
                .username(baseUser.username())
                .friendshipCode(baseUser.friendshipCode())
                .build();

        return repository.save(entity);
    }

    @Override
    public Boolean isUsernameAvailable(String username) {
        return !repository.existsByUsername(username);
    }
}
