package be.odeeh.studio.odeehservice.adapter.in.web.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record AttendanceRequest(
        UUID eventId,
        BigDecimal score,
        String description
) {
}
