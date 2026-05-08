package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.ArtistJpaRepository;
import be.odeeh.studio.odeehservice.adapter.out.repository.EventJpaRepository;
import be.odeeh.studio.odeehservice.adapter.out.repository.VenueJpaRepository;
import be.odeeh.studio.odeehservice.domain.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class VenueEventServiceIntegrationTests extends IntegrationTestBase {

    @Autowired
    private VenueEventService service;

    @Autowired
    private EventJpaRepository repository;

    @Autowired
    private VenueJpaRepository venueRepository;

    @Autowired
    private ArtistJpaRepository artistRepository;

    @BeforeEach
    void setUp() {
        // Clear repository before each test
        repository.deleteAll();
        venueRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    void search_shouldReturnMatchingEvents() {
        // Arrange
        var query = "QUERY";
        var page = 0;

        var foundVenue = buildAndSaveVenueEntity();
        var notFoundVenue = buildAndSaveVenueEntity();

        var foundArtist = buildAndSaveArtistEntity(query);
        var notFoundArtist = buildAndSaveArtistEntity("Not Found");

        var foundVenueEvent = buildAndSaveEventEntity(foundVenue.getId(), foundArtist.getId(), LocalDateTime.now());
        var notFoundVenueEventThisYear = buildAndSaveEventEntity(foundVenue.getId(), foundArtist.getId(), LocalDateTime.now().plusYears(1));
        var notFoundVenueEvent = buildAndSaveEventEntity(foundVenue.getId(), notFoundArtist.getId(), LocalDateTime.now());
        var notFoundVenueAndEvent = buildAndSaveEventEntity(notFoundVenue.getId(), foundArtist.getId(), LocalDateTime.now());

        // Act
        var result = service.search(foundVenue.getId(), query, page);
        var actual = result.getContent().get(0);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(actual.id()).isEqualTo(foundVenueEvent.getId());
        assertThat(actual.artistName()).isEqualTo(foundArtist.getName());
    }

    private ArtistEntity buildAndSaveArtistEntity(String name) {
        var entity = ArtistEntity.builder()
                .name(name)
                .type(ArtistType.GROUP)
                .country("BE")
                .build();

        return artistRepository.save(entity);
    }

    private VenueEntity buildAndSaveVenueEntity() {
        var entity = VenueEntity.builder()
                .type(VenueType.FESTIVAL)
                .name(UUID.randomUUID().toString())
                .address("Address")
                .city("City")
                .country("BE")
                .build();

        return venueRepository.save(entity);
    }

    private EventEntity buildAndSaveEventEntity(
            UUID venueId,
            UUID artistId,
            LocalDateTime startTime
    ) {
        var entity = EventEntity.builder()
                .venueId(venueId)
                .artistId(artistId)
                .startTime(startTime)
                .endTime(startTime.plusHours(2))
                .build();

        return repository.save(entity);
    }
}
