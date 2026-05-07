package be.odeeh.studio.odeehservice.application.port;

import be.odeeh.studio.odeehservice.application.model.Friendship;
import be.odeeh.studio.odeehservice.domain.model.FriendshipEntityQuery;

import java.util.List;

public interface FriendshipServicePort {

    void connect(String uid, Friendship request);

    List<FriendshipEntityQuery> fetchFriendships(String uid);
}
