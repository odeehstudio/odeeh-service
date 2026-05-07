package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.domain.model.FriendshipEntityQuery;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FriendshipResponseMapperTests {

    private final FriendshipResponseMapper mapper = Mappers.getMapper(FriendshipResponseMapper.class);

    @Test
    void toResponse_shouldReturnFriendshipResponseList() {
        // Arrange
        var src = List.of(buildFriendshipEntityQuery(), buildFriendshipEntityQuery());

        // Act
        var actual = mapper.toResponse(src);

        // Assert
        assertThat(actual).hasSize(src.size());
    }

    @Test
    void toResponse_shouldReturnFriendshipResponse() {
        // Arrange
        var src = buildFriendshipEntityQuery();

        // Act
        var actual = mapper.toResponse(src);

        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.id()).isEqualTo(src.id());
        assertThat(actual.username()).isEqualTo(src.username());
    }

    private FriendshipEntityQuery buildFriendshipEntityQuery() {
        return FriendshipEntityQuery.builder()
                .id(UUID.randomUUID())
                .username(UUID.randomUUID().toString())
                .build();
    }
}
