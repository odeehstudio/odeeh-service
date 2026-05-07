package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.FriendshipRequestJpaRepository;
import be.odeeh.studio.odeehservice.application.model.Friendship;
import be.odeeh.studio.odeehservice.application.port.BaseUserRepositoryPort;
import be.odeeh.studio.odeehservice.application.port.FriendshipServicePort;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import be.odeeh.studio.odeehservice.domain.entity.FriendshipEntity;
import be.odeeh.studio.odeehservice.domain.exception.OdeehDuplicateException;
import be.odeeh.studio.odeehservice.domain.model.FriendshipEntityQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FriendshipService implements FriendshipServicePort {

    private final FriendshipRequestJpaRepository repository;
    private final BaseUserRepositoryPort baseUserRepository;

    @Override
    public void connect(String uid, Friendship request) {
        BaseUserEntity requester = baseUserRepository.findByProviderUid(uid);
        BaseUserEntity receiver = baseUserRepository.findByProviderUid(request.uid());

        if (repository.existsByRequesterIdAndReceiverId(requester.getId(), receiver.getId())) {
            log.error("FRIENDSHIP RELATION ALREADY PRESENT");
            throw new OdeehDuplicateException();
        }

        FriendshipEntity entity = FriendshipEntity.builder()
                .requesterId(requester.getId())
                .receiverId(receiver.getId())
                .build();

        repository.save(entity);
    }

    @Override
    public List<FriendshipEntityQuery> fetchFriendships(String uid) {
        BaseUserEntity authenticatedUser = baseUserRepository.findByProviderUid(uid);

        return repository.findByUser(authenticatedUser.getId());
    }
}
