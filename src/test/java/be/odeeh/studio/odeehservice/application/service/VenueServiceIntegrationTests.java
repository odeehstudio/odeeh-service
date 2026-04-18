package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.VenueJpaRepository;
import be.odeeh.studio.odeehservice.domain.entity.VenueEntity;
import be.odeeh.studio.odeehservice.domain.entity.VenueType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

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
        String query = "QUERY";
        Integer page = 0;

        VenueEntity foundVenue = buildAndSaveVenueEntity(query);
        VenueEntity notFoundVenue = buildAndSaveVenueEntity("Not Found");

        // Act
        Page<VenueEntity> result = service.search(query, page);
        VenueEntity actual = result.getContent().get(0);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(actual.getId()).isEqualTo(foundVenue.getId());
    }

    private VenueEntity buildAndSaveVenueEntity(String name) {
        VenueEntity entity = VenueEntity.builder()
                .type(VenueType.FESTIVAL)
                .name(name)
                .address("Address")
                .city("City")
                .country("BE")
                .build();

        return repository.save(entity);
    }
}
