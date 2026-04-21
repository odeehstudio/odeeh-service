package be.odeeh.studio.odeehservice.adapter.in.web.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record VenueResponse(
        UUID id,
        String name,
        String city,
        String country
) {
}
