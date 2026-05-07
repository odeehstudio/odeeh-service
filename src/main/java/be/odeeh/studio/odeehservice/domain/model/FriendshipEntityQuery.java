package be.odeeh.studio.odeehservice.domain.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record FriendshipEntityQuery(
        UUID id,
        String username
) {
}
