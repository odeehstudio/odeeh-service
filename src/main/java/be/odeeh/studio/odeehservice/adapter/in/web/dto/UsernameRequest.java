package be.odeeh.studio.odeehservice.adapter.in.web.dto;

import lombok.Builder;

@Builder
public record UsernameRequest(
        String value
) {
}
