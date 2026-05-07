package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class BaseUserMapperTests {

    private final BaseUserMapper mapper = Mappers.getMapper(BaseUserMapper.class);

    @Test
    void toResponse_shouldReturnCorrectObject() {
        // Arrange
        var id = UUID.randomUUID();
        var username = "Odeeh";
        var src = BaseUserEntity.builder()
                .id(id)
                .username(username)
                .build();

        // Act
        var actual = mapper.toResponse(src);

        // Assert
        assertThat(actual.id()).isEqualTo(id);
        assertThat(actual.username()).isEqualTo(username);
    }
}
