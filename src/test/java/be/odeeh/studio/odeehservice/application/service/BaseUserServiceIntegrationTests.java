package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.BaseUserJpaRepository;
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
    void createBaseUser_shouldSaveAndReturnNewBaseUserEntity() {
        // Arrange
        String email = "test@mail.com";
        String providerUid = UUID.randomUUID().toString();

        // Act
        BaseUserEntity actual = service.createBaseUser(email, providerUid);

        // Assert
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getEmail()).isEqualTo(email);
        assertThat(actual.getProviderUid()).isEqualTo(providerUid);
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
    }

    @Test
    void createBaseUser_withExistingBaseUser_shouldThrowException() {
        // Arrange
        String email = "test@mail.com";
        String providerUid = UUID.randomUUID().toString();

        BaseUserEntity existingEntity = BaseUserEntity.builder()
                .email(email)
                .providerUid(providerUid)
                .build();

        repository.save(existingEntity);

        // Act & Assert
        OdeehDuplicateException exception = assertThrows(OdeehDuplicateException.class, () -> service.createBaseUser(email, providerUid));

        HttpStatus expectedStatus = HttpStatus.CONFLICT;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }
}
