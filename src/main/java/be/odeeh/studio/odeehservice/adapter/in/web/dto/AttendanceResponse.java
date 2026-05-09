package be.odeeh.studio.odeehservice.adapter.in.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record AttendanceResponse(
        UUID id,
        UUID baseUserId,
        String username,
        BigDecimal score,
        String description,
        String venueName,
        String artistName
) {
}
