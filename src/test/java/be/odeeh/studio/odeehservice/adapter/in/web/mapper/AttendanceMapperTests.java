package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.AttendanceRequest;
import be.odeeh.studio.odeehservice.application.model.Attendance;
import be.odeeh.studio.odeehservice.domain.exception.OdeehBadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AttendanceMapperTests {

    private final AttendanceMapper mapper = Mappers.getMapper(AttendanceMapper.class);

    @Test
    void map_shouldMapRequestToApplicationModel() {
        // Arrange
        String description = "  Test description  ";

        AttendanceRequest src = AttendanceRequest.builder()
                .eventId(UUID.randomUUID())
                .score(BigDecimal.TEN)
                .description(description)
                .build();

        // Act
        Attendance attendance = mapper.map(src);

        // Assert
        assertThat(attendance.eventId()).isEqualTo(src.eventId());
        assertThat(attendance.score()).isEqualTo(src.score());
        assertThat(attendance.description()).isEqualTo(description.trim());
    }

    @Test
    void map_shouldMapRequestToApplicationModel_withoutOptionals() {
        // Arrange
        AttendanceRequest src = AttendanceRequest.builder()
                .eventId(UUID.randomUUID())
                .score(BigDecimal.TEN)
                .description(null)
                .build();

        // Act
        Attendance attendance = mapper.map(src);

        // Assert
        assertThat(attendance.eventId()).isEqualTo(src.eventId());
        assertThat(attendance.score()).isEqualTo(src.score());
        assertThat(attendance.description()).isNull();
    }

    @Test
    void map_shouldThrowOdeehBadRequestException_whenEventIdIsNull() {
        // Arrange
        AttendanceRequest src = AttendanceRequest.builder()
                .eventId(null)
                .score(BigDecimal.TEN)
                .build();

        // Act & Assert
        OdeehBadRequestException exception = assertThrows(OdeehBadRequestException.class, () -> mapper.map(src));

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void map_shouldThrowOdeehBadRequestException_whenScoreIsNull() {
        // Arrange
        AttendanceRequest src = AttendanceRequest.builder()
                .eventId(UUID.randomUUID())
                .score(null)
                .build();

        // Act & Assert
        OdeehBadRequestException exception = assertThrows(OdeehBadRequestException.class, () -> mapper.map(src));

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @ParameterizedTest
    @CsvSource({
            "-0.25",
            "10.25",
            "5.1"
    })
    void map_shouldThrowOdeehBadRequestException_whenNoValidScore(BigDecimal score) {
        // Arrange
        AttendanceRequest src = AttendanceRequest.builder()
                .eventId(UUID.randomUUID())
                .score(score)
                .build();

        // Act & Assert
        OdeehBadRequestException exception = assertThrows(OdeehBadRequestException.class, () -> mapper.map(src));

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }
}
