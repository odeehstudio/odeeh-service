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
        String requesterEmail = "requester@mail.com";
        String receiverEmail = "receiver@mail.com";
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterEmail, requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverEmail, receiverProviderUid);

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
        String requesterEmail = "requester@mail.com";
        String receiverEmail = "receiver@mail.com";
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterEmail, requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverEmail, receiverProviderUid);

        buildAndSaveFriendshipRequestEntity(requester, receiver);

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
        String requesterEmail = "requester@mail.com";
        String receiverEmail = "receiver@mail.com";
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterEmail, requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverEmail, receiverProviderUid);

        FriendshipRequestEntity existingEntity = buildAndSaveFriendshipRequestEntity(requester, receiver);

        // Act
        service.dismissFriendshipRequest(existingEntity.getId(), requester.getProviderUid());

        // Assert
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void dismissFriendshipRequest_shouldThrowExceptionWhenAuthenticatedUserIsNotRequester() {
        // Arrange
        String requesterEmail = "requester@mail.com";
        String receiverEmail = "receiver@mail.com";
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterEmail, requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverEmail, receiverProviderUid);

        FriendshipRequestEntity existingEntity = buildAndSaveFriendshipRequestEntity(requester, receiver);

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
        String requesterEmail = "requester@mail.com";
        String receiverEmail = "receiver@mail.com";
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterEmail, requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverEmail, receiverProviderUid);

        FriendshipRequestEntity existingEntity = buildAndSaveFriendshipRequestEntity(requester, receiver);

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
        String requesterEmail = "requester@mail.com";
        String receiverEmail = "receiver@mail.com";
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterEmail, requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverEmail, receiverProviderUid);

        FriendshipRequestEntity existingEntity = buildAndSaveFriendshipRequestEntity(requester, receiver);

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
        String requesterEmail = "requester@mail.com";
        String receiverEmail = "receiver@mail.com";
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterEmail, requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverEmail, receiverProviderUid);

        FriendshipRequestEntity existingEntity = buildAndSaveFriendshipRequestEntity(requester, receiver);

        // Act
        service.denyFriendshipRequest(existingEntity.getId(), receiver.getProviderUid());

        // Assert
        assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void denyFriendshipRequest_shouldThrowExceptionWhenAuthenticatedUserIsNotReceiver() {
        // Arrange
        String requesterEmail = "requester@mail.com";
        String receiverEmail = "receiver@mail.com";
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterEmail, requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverEmail, receiverProviderUid);

        FriendshipRequestEntity existingEntity = buildAndSaveFriendshipRequestEntity(requester, receiver);

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
        String requesterEmail = "requester@mail.com";
        String receiverEmail = "receiver@mail.com";
        String requesterProviderUid = UUID.randomUUID().toString();
        String receiverProviderUid = UUID.randomUUID().toString();

        BaseUserEntity requester = buildAndSaveBaseUserEntity(requesterEmail, requesterProviderUid);
        BaseUserEntity receiver = buildAndSaveBaseUserEntity(receiverEmail, receiverProviderUid);

        FriendshipRequestEntity sendFriendshipRequest = buildAndSaveFriendshipRequestEntity(requester, receiver);
        FriendshipRequestEntity receivedFriendshipRequest = buildAndSaveFriendshipRequestEntity(receiver, requester);

        // Act
        List<FriendshipRequestEntity> receivedFriendshipRequests = service.listReceivedFriendshipRequests(requesterProviderUid);
        FriendshipRequestEntity actual = receivedFriendshipRequests.get(0);

        // Assert
        assertThat(receivedFriendshipRequests).hasSize(1);
        assertThat(actual.getId()).isEqualTo(receivedFriendshipRequest.getId());
        assertThat(actual.getRequesterId()).isEqualTo(receiver.getId());
        assertThat(actual.getReceiverId()).isEqualTo(requester.getId());
        assertThat(actual.getStatus()).isEqualTo(FriendshipRequestStatus.PENDING);
    }

    private BaseUserEntity buildAndSaveBaseUserEntity(
            String email,
            String providerUid
    ) {
        BaseUserEntity entity = BaseUserEntity.builder()
                .email(email)
                .providerUid(providerUid)
                .build();

        return baseUserRepository.save(entity);
    }

    private FriendshipRequestEntity buildAndSaveFriendshipRequestEntity(
            BaseUserEntity requester,
            BaseUserEntity receiver
    ) {
        FriendshipRequestEntity existingEntity = FriendshipRequestEntity.builder()
                .requesterId(requester.getId())
                .receiverId(receiver.getId())
                .status(FriendshipRequestStatus.PENDING)
                .build();

        return repository.save(existingEntity);
    }
}
