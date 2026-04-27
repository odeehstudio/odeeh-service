package be.odeeh.studio.odeehservice.adapter.in.web.dto;

import java.util.UUID;

public record BaseUserResponse(
        UUID id,
        String username,
        UUID friendshipCode
) {
}
