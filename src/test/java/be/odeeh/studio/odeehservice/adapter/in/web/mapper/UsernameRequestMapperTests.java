package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.UsernameRequest;
import be.odeeh.studio.odeehservice.domain.exception.OdeehBadRequestException;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UsernameRequestMapperTests {

    private final UsernameRequestMapper mapper = Mappers.getMapper(UsernameRequestMapper.class);

    @Test
    void toDomain_shouldReturnDomainObject() {
        // Arrange
        var value = "  Odeeh  ";
        var src = UsernameRequest.builder()
                .value(value)
                .build();

        // Act
        var actual = mapper.toDomain(src);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.value()).isEqualTo(value.trim());
    }

    @Test
    void toDomain_shouldThrowOdeehBadRequestException_whenNullValue() {
        // Arrange
        var src = UsernameRequest.builder()
                .value(null)
                .build();

        // Act & Assert
        var exception = assertThrows(OdeehBadRequestException.class, () -> mapper.toDomain(src));

        var expectedStatus = HttpStatus.BAD_REQUEST;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void toDomain_shouldThrowOdeehBadRequestException_whenBlankValue() {
        // Arrange
        var src = UsernameRequest.builder()
                .value("")
                .build();

        // Act & Assert
        var exception = assertThrows(OdeehBadRequestException.class, () -> mapper.toDomain(src));

        var expectedStatus = HttpStatus.BAD_REQUEST;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }
}
