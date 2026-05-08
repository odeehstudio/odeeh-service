package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.domain.model.VenueEventEntityQuery;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class EventResponseMapperTests {

    private final EventResponseMapper mapper = Mappers.getMapper(EventResponseMapper.class);

    @Test
    void toResponse_shouldReturnCorrectObject() {
        // Arrange
        var src = VenueEventEntityQuery.builder()
                .id(UUID.randomUUID())
                .artistName("name")
                .build();

        // Act
        var actual = mapper.toResponse(src);

        // Assert
        assertThat(actual.id()).isEqualTo(src.id());
        assertThat(actual.artistName()).isEqualTo(src.artistName());
    }
}
