package be.odeeh.studio.odeehservice.adapter.in.web.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record EventResponse(
        UUID id,
        UUID venueId,
        UUID artistId
) {
}
