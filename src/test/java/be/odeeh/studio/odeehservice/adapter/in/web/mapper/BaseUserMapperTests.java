package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.BaseUserRequest;
import be.odeeh.studio.odeehservice.adapter.in.web.dto.BaseUserResponse;
import be.odeeh.studio.odeehservice.application.model.BaseUser;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import be.odeeh.studio.odeehservice.domain.exception.OdeehBadRequestException;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseUserMapperTests {

    private final BaseUserMapper mapper = Mappers.getMapper(BaseUserMapper.class);

    @Test
    void map_shouldReturnCorrectObject() {
        // Arrange
        UUID id = UUID.randomUUID();
        String username = "Odeeh";
        UUID friendshipCode = UUID.randomUUID();

        BaseUserEntity src = BaseUserEntity.builder()
                .id(id)
                .username(username)
                .friendshipCode(friendshipCode)
                .build();

        // Act
        BaseUserResponse actual = mapper.map(src);

        // Assert
        assertThat(actual.id()).isEqualTo(id);
        assertThat(actual.username()).isEqualTo(username);
        assertThat(actual.friendshipCode()).isEqualTo(friendshipCode);
    }

    @Test
    void mapRequestToDomain_shouldReturnCorrectObject() {
        // Arrange
        String username = "  Odeeh  ";

        BaseUserRequest src = BaseUserRequest.builder()
                .username(username)
                .build();

        // Act
        BaseUser actual = mapper.mapRequestToDomain(src);

        // Assert
        assertThat(actual.username()).isEqualTo(username.trim());
        assertThat(actual.friendshipCode()).isNotNull();
    }

    @Test
    void mapRequestToDomain_shouldThrowOdeehBadRequestException_whenUsernameIsNull() {
        // Arrange
        BaseUserRequest src = BaseUserRequest.builder().build();

        // Act
        OdeehBadRequestException exception = assertThrows(OdeehBadRequestException.class, () -> mapper.mapRequestToDomain(src));

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }
}
