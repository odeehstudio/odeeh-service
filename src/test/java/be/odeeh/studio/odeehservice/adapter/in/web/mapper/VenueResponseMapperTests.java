package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.domain.entity.VenueEntity;
import be.odeeh.studio.odeehservice.domain.entity.VenueType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class VenueResponseMapperTests {

    private final VenueResponseMapper mapper = Mappers.getMapper(VenueResponseMapper.class);

    @Test
    void toResponse_shouldReturnCorrectObject() {
        // Arrange
        var src = VenueEntity.builder()
                .id(UUID.randomUUID())
                .type(VenueType.FESTIVAL)
                .name("Name")
                .address("Address")
                .city("City")
                .country("BE")
                .build();

        // Act
        var actual = mapper.toResponse(src);

        // Assert
        assertThat(actual.id()).isEqualTo(src.getId());
        assertThat(actual.name()).isEqualTo(src.getName());
        assertThat(actual.city()).isEqualTo(src.getCity());
        assertThat(actual.country()).isEqualTo(src.getCountry());
    }
}
