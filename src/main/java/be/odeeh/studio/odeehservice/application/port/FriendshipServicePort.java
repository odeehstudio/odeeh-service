package be.odeeh.studio.odeehservice.application.port;

import be.odeeh.studio.odeehservice.domain.entity.FriendshipRequestEntity;

import java.util.List;
import java.util.UUID;

public interface FriendshipServicePort {
    void sendFriendshipRequest(String authenticatedProviderUid, UUID receiverId);

    void dismissFriendshipRequest(UUID requestId, String authenticatedProviderUid);

    void acceptFriendshipRequest(UUID requestId, String authenticatedProviderUid);

    void denyFriendshipRequest(UUID requestId, String authenticatedProviderUid);

    List<FriendshipRequestEntity> listReceivedFriendshipRequests(String authenticatedProviderUid);

    List<FriendshipRequestEntity> listAcceptedFriendshipRequests(String authenticatedProviderUid);
}
