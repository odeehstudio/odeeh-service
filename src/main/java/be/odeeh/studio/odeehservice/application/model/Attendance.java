package be.odeeh.studio.odeehservice.application.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record Attendance(
        UUID eventId,
        BigDecimal score,
        String description
) {
}
