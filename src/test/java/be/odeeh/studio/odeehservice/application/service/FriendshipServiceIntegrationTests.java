package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.BaseUserJpaRepository;
import be.odeeh.studio.odeehservice.adapter.out.repository.FriendshipRequestJpaRepository;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import be.odeeh.studio.odeehservice.domain.entity.FriendshipRequestEntity;
import be.odeeh.studio.odeehservice.domain.entity.FriendshipRequestStatus;
import be.odeeh.studio.odeehservice.domain.exception.OdeehBadRequestException;
import be.odeeh.studio.odeehservice.domain.exception.OdeehDuplicateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
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
    void sendFriendshipRequest_shouldSaveFriendShipRequest() {
        // Arrange
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverProviderUid);

        // Act
        service.sendFriendshipRequest(requester.getProviderUid(), receiver.getId());

        // Assert
        Optional<FriendshipRequestEntity> optional = repository.findAll().stream().findFirst();
        assertThat(optional).isPresent();
        FriendshipRequestEntity actual = optional.get();

        assertThat(actual.getRequesterId()).isEqualTo(requester.getId());
        assertThat(actual.getReceiverId()).isEqualTo(receiver.getId());
        assertThat(actual.getStatus()).isEqualTo(FriendshipRequestStatus.PENDING);
        assertThat(actual.getRespondedAt()).isNull();
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
    }

    @Test
    void sendFriendshipRequest_shouldThrowExceptionWhenFriendshipRelationAlreadyPresent() {
        // Arrange
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverProviderUid);

        buildAndSaveFriendshipRequestEntity(requester, receiver, FriendshipRequestStatus.PENDING);

        // Act & Assert
        OdeehDuplicateException exception = assertThrows(OdeehDuplicateException.class, () ->
                service.sendFriendshipRequest(requester.getProviderUid(), receiver.getId())
        );

        HttpStatus expectedStatus = HttpStatus.CONFLICT;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void dismissFriendshipRequest_shouldDeleteFriendshipRequestEntity() {
        // Arrange
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverProviderUid);

        FriendshipRequestEntity existingEntity = buildAndSaveFriendshipRequestEntity(requester, receiver, FriendshipRequestStatus.PENDING);

        // Act
        service.dismissFriendshipRequest(existingEntity.getId(), requester.getProviderUid());

        // Assert
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void dismissFriendshipRequest_shouldThrowExceptionWhenAuthenticatedUserIsNotRequester() {
        // Arrange
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverProviderUid);

        FriendshipRequestEntity existingEntity = buildAndSaveFriendshipRequestEntity(requester, receiver, FriendshipRequestStatus.PENDING);

        // Act & Assert
        OdeehBadRequestException exception = assertThrows(OdeehBadRequestException.class, () ->
                service.dismissFriendshipRequest(existingEntity.getId(), receiver.getProviderUid())
        );

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void acceptFriendshipRequest_shouldUpdateExistingEntity() {
        // Arrange
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverProviderUid);

        FriendshipRequestEntity existingEntity = buildAndSaveFriendshipRequestEntity(requester, receiver, FriendshipRequestStatus.PENDING);

        // Act
        service.acceptFriendshipRequest(existingEntity.getId(), receiver.getProviderUid());

        // Assert
        Optional<FriendshipRequestEntity> optional = repository.findAll().stream().findFirst();
        assertThat(optional).isPresent();
        FriendshipRequestEntity actual = optional.get();

        assertThat(actual.getRequesterId()).isEqualTo(requester.getId());
        assertThat(actual.getReceiverId()).isEqualTo(receiver.getId());
        assertThat(actual.getStatus()).isEqualTo(FriendshipRequestStatus.ACCEPTED);
        assertThat(actual.getRespondedAt()).isNotNull();
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
    }

    @Test
    void acceptFriendshipRequest_shouldThrowExceptionWhenAuthenticatedUserIsNotReceiver() {
        // Arrange
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverProviderUid);

        FriendshipRequestEntity existingEntity = buildAndSaveFriendshipRequestEntity(requester, receiver, FriendshipRequestStatus.PENDING);

        // Act & Assert
        OdeehBadRequestException exception = assertThrows(OdeehBadRequestException.class, () ->
                service.acceptFriendshipRequest(existingEntity.getId(), requester.getProviderUid())
        );

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void denyFriendshipRequest_shouldDeleteFriendshipRequestEntity() {
        // Arrange
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverProviderUid);

        FriendshipRequestEntity existingEntity = buildAndSaveFriendshipRequestEntity(requester, receiver, FriendshipRequestStatus.PENDING);

        // Act
        service.denyFriendshipRequest(existingEntity.getId(), receiver.getProviderUid());

        // Assert
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void denyFriendshipRequest_shouldThrowExceptionWhenAuthenticatedUserIsNotReceiver() {
        // Arrange
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverProviderUid);

        FriendshipRequestEntity existingEntity = buildAndSaveFriendshipRequestEntity(requester, receiver, FriendshipRequestStatus.PENDING);

        // Act & Assert
        OdeehBadRequestException exception = assertThrows(OdeehBadRequestException.class, () ->
                service.denyFriendshipRequest(existingEntity.getId(), requester.getProviderUid())
        );

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void listReceivedFriendshipRequests_shouldReturnListOfReceivedFriendshipRequestEntities() {
        // Arrange
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverProviderUid);

        FriendshipRequestEntity sendFriendshipRequest = buildAndSaveFriendshipRequestEntity(requester, receiver, FriendshipRequestStatus.PENDING);
        FriendshipRequestEntity receivedFriendshipRequest = buildAndSaveFriendshipRequestEntity(receiver, requester, FriendshipRequestStatus.PENDING);

        // Act
        List<FriendshipRequestEntity> receivedFriendshipRequests = service.listReceivedFriendshipRequests(requesterProviderUid);
        FriendshipRequestEntity actual = receivedFriendshipRequests.get(0);

        // Assert
        assertThat(receivedFriendshipRequests).hasSize(1);
        assertThat(actual.getId()).isEqualTo(receivedFriendshipRequest.getId());
        assertThat(actual.getRequesterId()).isEqualTo(receivedFriendshipRequest.getRequesterId());
        assertThat(actual.getReceiverId()).isEqualTo(receivedFriendshipRequest.getReceiverId());
        assertThat(actual.getStatus()).isEqualTo(FriendshipRequestStatus.PENDING);
    }

    @Test
    void listAcceptedFriendshipRequests_shouldReturnListOfAcceptedFriendshipRequestEntities() {
        // Arrange
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverProviderUid);

        FriendshipRequestEntity pendingFriendshipRequest = buildAndSaveFriendshipRequestEntity(requester, receiver, FriendshipRequestStatus.PENDING);
        FriendshipRequestEntity acceptedFriendshipRequest = buildAndSaveFriendshipRequestEntity(receiver, requester, FriendshipRequestStatus.ACCEPTED);

        // Act
        List<FriendshipRequestEntity> receivedFriendshipRequests = service.listAcceptedFriendshipRequests(requesterProviderUid);
        FriendshipRequestEntity actual = receivedFriendshipRequests.get(0);

        // Assert
        assertThat(receivedFriendshipRequests).hasSize(1);
        assertThat(actual.getId()).isEqualTo(acceptedFriendshipRequest.getId());
        assertThat(actual.getRequesterId()).isEqualTo(acceptedFriendshipRequest.getRequesterId());
        assertThat(actual.getReceiverId()).isEqualTo(acceptedFriendshipRequest.getReceiverId());
        assertThat(actual.getStatus()).isEqualTo(FriendshipRequestStatus.ACCEPTED);
    }

    private BaseUserEntity buildAndSaveBaseUserEntity(String providerUid) {
        BaseUserEntity entity = BaseUserEntity.builder()
                .username(UUID.randomUUID().toString())
                .providerUid(providerUid)
                .build();

        return baseUserRepository.save(entity);
    }

    private FriendshipRequestEntity buildAndSaveFriendshipRequestEntity(
            BaseUserEntity requester,
            BaseUserEntity receiver,
            FriendshipRequestStatus status
    ) {
        FriendshipRequestEntity existingEntity = FriendshipRequestEntity.builder()
                .requesterId(requester.getId())
                .receiverId(receiver.getId())
                .status(status)
                .build();

        return repository.save(existingEntity);
    }
}
