package be.odeeh.studio.odeehservice.adapter.in.web.mapper;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.VenueResponse;
import be.odeeh.studio.odeehservice.domain.entity.VenueEntity;
import be.odeeh.studio.odeehservice.domain.entity.VenueType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class VenueMapperTests {

    private final VenueMapper mapper = Mappers.getMapper(VenueMapper.class);

    @Test
    void map_shouldReturnCorrectObject() {
        // Arrange
        VenueEntity src = VenueEntity.builder()
                .id(UUID.randomUUID())
                .type(VenueType.FESTIVAL)
                .name("Name")
                .address("Address")
                .city("City")
                .country("BE")
                .build();

        // Act
        VenueResponse actual = mapper.map(src);

        // Assert
        assertThat(actual.id()).isEqualTo(src.getId());
        assertThat(actual.name()).isEqualTo(src.getName());
        assertThat(actual.city()).isEqualTo(src.getCity());
        assertThat(actual.country()).isEqualTo(src.getCountry());
    }
}
