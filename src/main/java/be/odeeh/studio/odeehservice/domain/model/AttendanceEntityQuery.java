package be.odeeh.studio.odeehservice.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public record AttendanceEntityQuery(
        UUID id,
        UUID baseUserId,
        String username,
        BigDecimal score,
        String description,
        String venueName,
        String artistName
) {
}
