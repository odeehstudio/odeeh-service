package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.BaseUserJpaRepository;
import be.odeeh.studio.odeehservice.application.model.Username;
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
    void isUserEnrolled_shouldReturnTrue() {
        // Arrange
        var uid = UUID.randomUUID().toString();
        var entity = BaseUserEntity.builder()
                .providerUid(uid)
                .username("Odeeh")
                .build();

        repository.save(entity);

        // Act
        var actual = service.isUserEnrolled(uid);

        // Assert
        assertThat(actual).isTrue();
    }

    @Test
    void isUserEnrolled_shouldReturnFalse() {
        // Arrange
        var uid = UUID.randomUUID().toString();

        // Act
        var actual = service.isUserEnrolled(uid);

        // Assert
        assertThat(actual).isFalse();
    }

    @Test
    void isUsernameAvailable_shouldReturnTrue() {
        // Arrange
        var value = "Odeeh";
        var request = Username.builder()
                .value(value)
                .build();

        // Act
        var actual = service.isUsernameAvailable(request);

        // Assert
        assertThat(actual).isTrue();
    }

    @Test
    void isUsernameAvailable_shouldReturnFalse() {
        // Arrange
        var value = "Odeeh";
        var request = Username.builder()
                .value(value)
                .build();

        var entity = BaseUserEntity.builder()
                .providerUid(UUID.randomUUID().toString())
                .username(value)
                .build();

        repository.save(entity);

        // Act
        var actual = service.isUsernameAvailable(request);

        // Assert
        assertThat(actual).isFalse();
    }

    @Test
    void enroll_shouldThrowOdeehDuplicateException_whenUsernameExists() {
        // Arrange
        var uid = UUID.randomUUID().toString();
        var value = "Odeeh";
        var request = Username.builder()
                .value(value)
                .build();

        var entity = BaseUserEntity.builder()
                .providerUid(UUID.randomUUID().toString())
                .username(value)
                .build();

        repository.save(entity);

        // Act & Assert
        var exception = assertThrows(OdeehDuplicateException.class, () -> service.enroll(uid, request));

        var expectedStatus = HttpStatus.CONFLICT;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void enroll_shouldThrowOdeehDuplicateException_whenUidExists() {
        // Arrange
        var uid = UUID.randomUUID().toString();
        var value = "Odeeh";
        var request = Username.builder()
                .value(value)
                .build();

        var entity = BaseUserEntity.builder()
                .providerUid(uid)
                .username(UUID.randomUUID().toString())
                .build();

        repository.save(entity);

        // Act & Assert
        var exception = assertThrows(OdeehDuplicateException.class, () -> service.enroll(uid, request));

        var expectedStatus = HttpStatus.CONFLICT;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void enroll_shouldReturnBaseUserEntity() {
        // Arrange
        var uid = UUID.randomUUID().toString();
        var value = "Odeeh";
        var request = Username.builder()
                .value(value)
                .build();

        // Act
        var actual = service.enroll(uid, request);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getProviderUid()).isEqualTo(uid);
        assertThat(actual.getUsername()).isEqualTo(value);
    }
}
