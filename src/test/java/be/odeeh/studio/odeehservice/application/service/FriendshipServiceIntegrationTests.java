package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.BaseUserJpaRepository;
import be.odeeh.studio.odeehservice.adapter.out.repository.FriendshipRequestJpaRepository;
import be.odeeh.studio.odeehservice.application.model.Friendship;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import be.odeeh.studio.odeehservice.domain.entity.FriendshipEntity;
import be.odeeh.studio.odeehservice.domain.exception.OdeehDuplicateException;
import be.odeeh.studio.odeehservice.domain.model.FriendshipEntityQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FriendshipServiceIntegrationTests extends IntegrationTestBase {

    @Autowired
    private FriendshipService service;

    @Autowired
    private FriendshipRequestJpaRepository repository;

    @Autowired
    private BaseUserJpaRepository baseUserRepository;

    @BeforeEach
    void setUp() {
        // Clear repository before each test
        repository.deleteAll();
        baseUserRepository.deleteAll();
    }

    @Test
    void connect_shouldThrowOdeehDuplicateException_WhenFriendshipRelationAlreadyPresent() {
        // Arrange
        var requester = buildAndSaveBaseUserEntity();
        var receiver = buildAndSaveBaseUserEntity();

        var request = Friendship.builder()
                .uid(receiver.getProviderUid())
                .build();

        buildAndSaveFriendshipRequestEntity(requester, receiver);

        // Act & Assert
        var exception = assertThrows(OdeehDuplicateException.class, () -> service.connect(requester.getProviderUid(), request));

        var expectedStatus = HttpStatus.CONFLICT;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void connect_shouldSaveFriendShip() {
        // Arrange
        var requester = buildAndSaveBaseUserEntity();
        var receiver = buildAndSaveBaseUserEntity();

        var request = Friendship.builder()
                .uid(receiver.getProviderUid())
                .build();

        // Act
        service.connect(requester.getProviderUid(), request);

        // Assert
        var optional = repository.findAll().stream().findFirst();
        assertThat(optional).isPresent();
        FriendshipEntity actual = optional.get();

        assertThat(actual.getRequesterId()).isEqualTo(requester.getId());
        assertThat(actual.getReceiverId()).isEqualTo(receiver.getId());
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
    }

    @Test
    void fetchFriendships_shouldReturnFriendshipEntityQueryList() {
        // Arrange
        var authenticatedUser = buildAndSaveBaseUserEntity();
        var receiver = buildAndSaveBaseUserEntity();
        var requester = buildAndSaveBaseUserEntity();

        buildAndSaveFriendshipRequestEntity(authenticatedUser, receiver);
        buildAndSaveFriendshipRequestEntity(requester, authenticatedUser);

        // Act
        var actual = service.fetchFriendships(authenticatedUser.getProviderUid());

        // Assert
        assertThat(actual).hasSize(2);

        assertThat(actual)
                .extracting(FriendshipEntityQuery::id)
                .containsExactlyInAnyOrder(receiver.getId(), requester.getId())
                .doesNotContain(authenticatedUser.getId());

        assertThat(actual)
                .extracting(FriendshipEntityQuery::username)
                .containsExactlyInAnyOrder(receiver.getUsername(), requester.getUsername())
                .doesNotContain(authenticatedUser.getUsername());
    }

    private BaseUserEntity buildAndSaveBaseUserEntity() {
        var entity = BaseUserEntity.builder()
                .username(UUID.randomUUID().toString())
                .providerUid(UUID.randomUUID().toString())
                .build();

        return baseUserRepository.save(entity);
    }

    private FriendshipEntity buildAndSaveFriendshipRequestEntity(
            BaseUserEntity requester,
            BaseUserEntity receiver
    ) {
        var existingEntity = FriendshipEntity.builder()
                .requesterId(requester.getId())
                .receiverId(receiver.getId())
                .build();

        return repository.save(existingEntity);
    }
}
