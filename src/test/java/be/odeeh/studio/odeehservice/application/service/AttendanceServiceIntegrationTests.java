package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.*;
import be.odeeh.studio.odeehservice.application.model.Attendance;
import be.odeeh.studio.odeehservice.domain.entity.*;
import be.odeeh.studio.odeehservice.domain.exception.OdeehNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AttendanceServiceIntegrationTests extends IntegrationTestBase {

    @Autowired
    private AttendanceService service;

    @Autowired
    private AttendanceJpaRepository repository;

    @Autowired
    private EventJpaRepository eventRepository;

    @Autowired
    private BaseUserJpaRepository baseUserRepository;

    @Autowired
    private VenueJpaRepository venueRepository;

    @Autowired
    private ArtistJpaRepository artistRepository;

    @BeforeEach
    void setUp() {
        // Clear repository before each test
        repository.deleteAll();
        eventRepository.deleteAll();
        baseUserRepository.deleteAll();
        venueRepository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    void createAttendance_shouldSaveAndReturnNewAttendanceEntity() {
        // Arrange
        String providerUid = UUID.randomUUID().toString();

        BaseUserEntity baseUserEntity = buildAndSaveBaseUserEntity(providerUid);
        VenueEntity venueEntity = buildAndSaveVenueEntity();
        ArtistEntity artistEntity = buildAndSaveArtistEntity();
        EventEntity eventEntity = buildAndSaveEventEntity(venueEntity.getId(), artistEntity.getId());

        Attendance attendance = Attendance.builder()
                .score(BigDecimal.TEN)
                .eventId(eventEntity.getId())
                .build();

        // Act
        AttendanceEntity actual = service.createAttendance(providerUid, attendance);

        // Assert
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getBaseUserId()).isEqualTo(baseUserEntity.getId());
        assertThat(actual.getEventId()).isEqualTo(eventEntity.getId());
        assertThat(actual.getScore()).isEqualTo(attendance.score());
        assertThat(actual.getHasPictures()).isEqualTo(Boolean.FALSE);
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
    }

    @Test
    void createAttendance_shouldThrowOdeehNotFoundException_whenNoBaseUserEntityFound() {
        // Arrange
        String providerUid = UUID.randomUUID().toString();

        Attendance attendance = Attendance.builder().build();

        // Act & Assert
        OdeehNotFoundException exception = assertThrows(OdeehNotFoundException.class, () ->
                service.createAttendance(providerUid, attendance)
        );

        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void createAttendance_shouldThrowOdeehNotFoundException_whenNoEventEntityFound() {
        // Arrange
        String providerUid = UUID.randomUUID().toString();

        BaseUserEntity baseUserEntity = buildAndSaveBaseUserEntity(providerUid);

        Attendance attendance = Attendance.builder()
                .eventId(UUID.randomUUID())
                .build();

        // Act & Assert
        OdeehNotFoundException exception = assertThrows(OdeehNotFoundException.class, () ->
                service.createAttendance(providerUid, attendance)
        );

        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    private BaseUserEntity buildAndSaveBaseUserEntity(String providerUid) {
        BaseUserEntity entity = BaseUserEntity.builder()
                .email("Email")
                .providerUid(providerUid)
                .build();

        return baseUserRepository.save(entity);
    }

    private ArtistEntity buildAndSaveArtistEntity() {
        ArtistEntity entity = ArtistEntity.builder()
                .name("Name")
                .type(ArtistType.GROUP)
                .country("BE")
                .build();

        return artistRepository.save(entity);
    }

    private VenueEntity buildAndSaveVenueEntity() {
        VenueEntity entity = VenueEntity.builder()
                .type(VenueType.FESTIVAL)
                .name("Name")
                .address("Address")
                .city("City")
                .country("Country")
                .build();

        return venueRepository.save(entity);
    }

    private EventEntity buildAndSaveEventEntity(UUID venueId, UUID artistId) {
        EventEntity entity = EventEntity.builder()
                .name("Name")
                .venueId(venueId)
                .artistId(artistId)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .build();

        return eventRepository.save(entity);
    }
}
