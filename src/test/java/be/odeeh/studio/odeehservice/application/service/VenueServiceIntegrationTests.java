package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.VenueJpaRepository;
import be.odeeh.studio.odeehservice.domain.entity.VenueEntity;
import be.odeeh.studio.odeehservice.domain.entity.VenueType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class VenueServiceIntegrationTests extends IntegrationTestBase {

    @Autowired
    private VenueService service;

    @Autowired
    private VenueJpaRepository repository;

    @BeforeEach
    void setUp() {
        // Clear repository before each test
        repository.deleteAll();
    }

    @Test
    void search_shouldReturnMatchingVenues() {
        // Arrange
        var query = "QUERY";
        var page = 0;

        var foundVenue = buildAndSaveVenueEntity(query);
        var notFoundVenue = buildAndSaveVenueEntity("Not Found");

        // Act
        var result = service.search(query, page);
        var actual = result.getContent().get(0);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(actual.getId()).isEqualTo(foundVenue.getId());
    }

    private VenueEntity buildAndSaveVenueEntity(String name) {
        var entity = VenueEntity.builder()
                .type(VenueType.FESTIVAL)
                .name(name)
                .address("Address")
                .city("City")
                .country("BE")
                .build();

        return repository.save(entity);
    }
}
