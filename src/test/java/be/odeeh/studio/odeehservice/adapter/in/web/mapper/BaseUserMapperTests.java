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
        String email = "test@mail.com";
        String providerUid = UUID.randomUUID().toString();

        BaseUserEntity src = BaseUserEntity.builder()
                .id(id)
                .email(email)
                .providerUid(providerUid)
                .build();

        // Act
        BaseUserResponse actual = mapper.map(src);

        // Assert
        assertThat(actual.id()).isEqualTo(id);
        assertThat(actual.email()).isEqualTo(email);
        assertThat(actual.providerUid()).isEqualTo(providerUid);
    }
}
