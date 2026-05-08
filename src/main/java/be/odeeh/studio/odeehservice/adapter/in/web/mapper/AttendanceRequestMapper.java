package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.AttendanceRequest;
import be.odeeh.studio.odeehservice.application.model.Attendance;
import be.odeeh.studio.odeehservice.domain.exception.OdeehBadRequestException;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AttendanceRequestMapper extends ModelMapper {

    @BeforeMapping
    default void validate(AttendanceRequest src) {
        if (src.eventId() == null) throw new OdeehBadRequestException();
        if (src.score() == null) throw new OdeehBadRequestException();

        validateScore(src);
    }

    default void validateScore(AttendanceRequest src) {
        BigDecimal score = src.score();

        if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(BigDecimal.TEN) > 0) throw new OdeehBadRequestException();

        BigDecimal divisor = new BigDecimal("0.25");
        BigDecimal remainder = score.remainder(divisor);
        if (remainder.compareTo(BigDecimal.ZERO) != 0) throw new OdeehBadRequestException();
    }

    @Mapping(source = "eventId", target = "eventId")
    @Mapping(source = "score", target = "score")
    @Mapping(source = "description", target = "description", qualifiedByName = "trim")
    @Mapping(source = "friends", target = "friends", qualifiedByName = "sanitizeUUIDs")
    Attendance toDomain(AttendanceRequest src);

    @Named("sanitizeUUIDs")
    default List<UUID> sanitizeUUIDs(List<UUID> src) {
        return src != null ? src.stream().distinct().toList() : List.of();
    }
}
