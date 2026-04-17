package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.FriendshipRequestJpaRepository;
import be.odeeh.studio.odeehservice.application.port.BaseUserRepositoryPort;
import be.odeeh.studio.odeehservice.application.port.FriendshipServicePort;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import be.odeeh.studio.odeehservice.domain.entity.FriendshipRequestEntity;
import be.odeeh.studio.odeehservice.domain.entity.FriendshipRequestStatus;
import be.odeeh.studio.odeehservice.domain.exception.OdeehBadRequestException;
import be.odeeh.studio.odeehservice.domain.exception.OdeehDuplicateException;
import be.odeeh.studio.odeehservice.domain.exception.OdeehNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class FriendshipService implements FriendshipServicePort {

    private final FriendshipRequestJpaRepository repository;
    private final BaseUserRepositoryPort baseUserRepository;

    @Override
    public void sendFriendshipRequest(String authenticatedProviderUid, UUID receiverId) {
        BaseUserEntity requester = baseUserRepository.findForAuthenticatedBaseUser(authenticatedProviderUid);
        BaseUserEntity receiver = baseUserRepository.findById(receiverId);

        if (repository.existsByRequesterIdAndReceiverId(requester.getId(), receiver.getId())) {
            log.error("FRIENDSHIP RELATION ALREADY PRESENT");
            throw new OdeehDuplicateException();
        }

        FriendshipRequestEntity entity = FriendshipRequestEntity.builder()
                .requesterId(requester.getId())
                .receiverId(receiver.getId())
                .status(FriendshipRequestStatus.PENDING)
                .build();

        repository.save(entity);
    }

    @Override
    public void dismissFriendshipRequest(UUID requestId, String authenticatedProviderUid) {
        BaseUserEntity authenticatedUser = baseUserRepository.findForAuthenticatedBaseUser(authenticatedProviderUid);
        FriendshipRequestEntity entity = findFriendshipRequest(requestId);

        if (!entity.getRequesterId().equals(authenticatedUser.getId())) {
            log.error("CAN'T DISMISS A FRIENDSHIP REQUEST WHICH WAS NOT REQUESTED BY THE AUTHENTICATED USER");
            throw new OdeehBadRequestException();
        }

        repository.delete(entity);
    }

    @Override
    public void acceptFriendshipRequest(UUID requestId, String authenticatedProviderUid) {
        BaseUserEntity authenticatedUser = baseUserRepository.findForAuthenticatedBaseUser(authenticatedProviderUid);
        FriendshipRequestEntity entity = findFriendshipRequest(requestId);

        if (!entity.getReceiverId().equals(authenticatedUser.getId())) {
            log.error("CAN'T ACCEPT A FRIENDSHIP REQUEST WHICH WAS NOT INTENDED FOR THE AUTHENTICATED USER");
            throw new OdeehBadRequestException();
        }

        entity.acceptFriendshipRequest();

        repository.save(entity);
    }

    @Override
    public void denyFriendshipRequest(UUID requestId, String authenticatedProviderUid) {
        BaseUserEntity authenticatedUser = baseUserRepository.findForAuthenticatedBaseUser(authenticatedProviderUid);
        FriendshipRequestEntity entity = findFriendshipRequest(requestId);

        if (!entity.getReceiverId().equals(authenticatedUser.getId())) {
            log.error("CAN'T DENY A FRIENDSHIP REQUEST WHICH WAS NOT INTENDED FOR THE AUTHENTICATED USER");
            throw new OdeehBadRequestException();
        }

        repository.delete(entity);
    }

    @Override
    public List<FriendshipRequestEntity> listReceivedFriendshipRequests(String authenticatedProviderUid) {
        BaseUserEntity authenticatedUser = baseUserRepository.findForAuthenticatedBaseUser(authenticatedProviderUid);

        return repository.findByReceiverIdAndStatus(authenticatedUser.getId(), FriendshipRequestStatus.PENDING);
    }

    @Override
    public List<FriendshipRequestEntity> listAcceptedFriendshipRequests(String authenticatedProviderUid) {
        BaseUserEntity authenticatedUser = baseUserRepository.findForAuthenticatedBaseUser(authenticatedProviderUid);

        return repository.findByReceiverIdAndStatus(authenticatedUser.getId(), FriendshipRequestStatus.ACCEPTED);
    }


    private FriendshipRequestEntity findFriendshipRequest(UUID id) {
        return repository.findById(id).orElseThrow(OdeehNotFoundException::new);
    }
}
