package be.odeeh.studio.odeehservice.application.model;

import lombok.Builder;

import java.util.UUID;

@Builder
public record BaseUser(
        String username,
        UUID friendshipCode
) {
}
