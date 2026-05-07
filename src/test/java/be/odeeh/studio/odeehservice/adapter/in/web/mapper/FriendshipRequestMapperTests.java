package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.FriendshipRequest;
import be.odeeh.studio.odeehservice.domain.exception.OdeehBadRequestException;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FriendshipRequestMapperTests {

    private final FriendshipRequestMapper mapper = Mappers.getMapper(FriendshipRequestMapper.class);

    @Test
    void toDomain_shouldReturnDomainObject() {
        // Arrange
        var uid = "  UID  ";
        var src = FriendshipRequest.builder()
                .uid(uid)
                .build();

        // Act
        var actual = mapper.toDomain(src);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.uid()).isEqualTo(uid.trim());
    }

    @Test
    void toDomain_shouldThrowOdeehBadRequestException_whenNullValue() {
        // Arrange
        var src = FriendshipRequest.builder()
                .uid(null)
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
        var src = FriendshipRequest.builder()
                .uid("")
                .build();

        // Act & Assert
        var exception = assertThrows(OdeehBadRequestException.class, () -> mapper.toDomain(src));

        var expectedStatus = HttpStatus.BAD_REQUEST;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }
}
