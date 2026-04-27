package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.BaseUserJpaRepository;
import be.odeeh.studio.odeehservice.application.model.BaseUser;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEnrollmentStatus;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import be.odeeh.studio.odeehservice.domain.exception.OdeehDuplicateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseUserServiceIntegrationTests extends IntegrationTestBase {

    @Autowired
    private BaseUserService service;

    @Autowired
    private BaseUserJpaRepository repository;

    @BeforeEach
    void setUp() {
        // Clear repository before each test
        repository.deleteAll();
    }

    @Test
    void validateEnrollment_shouldReturnNeedsEnrollmentStatus_whenNoBaseUserEntityWithUid() {
        // Arrange
        String providerUid = UUID.randomUUID().toString();

        // Act
        BaseUserEnrollmentStatus actual = service.validateEnrollment(providerUid);

        // Arrange
        assertThat(actual).isEqualTo(BaseUserEnrollmentStatus.NEEDS_ENROLLMENT);
    }

    @Test
    void validateEnrollment_shouldReturnEnrolled_whenBaseUserEntityWithUid() {
        // Arrange
        String providerUid = UUID.randomUUID().toString();
        BaseUserEntity existingEntity = BaseUserEntity.builder()
                .username(UUID.randomUUID().toString())
                .providerUid(providerUid)
                .friendshipCode(UUID.randomUUID())
                .build();

        repository.save(existingEntity);

        // Act
        BaseUserEnrollmentStatus actual = service.validateEnrollment(providerUid);

        // Assert
        assertThat(actual).isEqualTo(BaseUserEnrollmentStatus.ENROLLED);
    }

    @Test
    void createBaseUser_shouldSaveAndReturnNewBaseUserEntity() {
        // Arrange
        String username = "Odeeh";
        String providerUid = UUID.randomUUID().toString();
        BaseUser baseUser = BaseUser.builder()
                .username(username)
                .friendshipCode(UUID.randomUUID())
                .build();

        // Act
        BaseUserEntity actual = service.createBaseUser(baseUser, providerUid);

        // Assert
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getUsername()).isEqualTo(username);
        assertThat(actual.getProviderUid()).isEqualTo(providerUid);
        assertThat(actual.getFriendshipCode()).isNotNull();
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
    }

    @Test
    void createBaseUser_withExistingBaseUser_shouldThrowException() {
        // Arrange
        String username = "Odeeh";
        String providerUid = UUID.randomUUID().toString();
        UUID friendshipCode = UUID.randomUUID();
        BaseUser baseUser = BaseUser.builder()
                .username(username)
                .friendshipCode(friendshipCode)
                .build();

        BaseUserEntity existingEntity = BaseUserEntity.builder()
                .username(username)
                .providerUid(providerUid)
                .friendshipCode(friendshipCode)
                .build();

        repository.save(existingEntity);

        // Act & Assert
        OdeehDuplicateException exception = assertThrows(OdeehDuplicateException.class, () -> service.createBaseUser(baseUser, providerUid));

        HttpStatus expectedStatus = HttpStatus.CONFLICT;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }
}
