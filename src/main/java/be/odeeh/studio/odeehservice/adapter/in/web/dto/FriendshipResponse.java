package be.odeeh.studio.odeehservice.adapter.in.web.dto;

import java.util.UUID;

public record FriendshipResponse(
        UUID id,
        String username
) {
}
