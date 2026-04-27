package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.BaseUserResponse;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

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
}
