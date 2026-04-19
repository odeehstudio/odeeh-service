package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.*;
import be.odeeh.studio.odeehservice.application.model.Attendance;
import be.odeeh.studio.odeehservice.domain.entity.*;
import be.odeeh.studio.odeehservice.domain.exception.OdeehBadRequestException;
import be.odeeh.studio.odeehservice.domain.exception.OdeehDuplicateException;
import be.odeeh.studio.odeehservice.domain.exception.OdeehNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
        String friendProviderUid = UUID.randomUUID().toString();

        BaseUserEntity baseUserEntity = buildAndSaveBaseUserEntity(providerUid);
        BaseUserEntity friendBaseUserEntity = buildAndSaveBaseUserEntity(friendProviderUid);
        VenueEntity venueEntity = buildAndSaveVenueEntity();
        ArtistEntity artistEntity = buildAndSaveArtistEntity();
        EventEntity eventEntity = buildAndSaveEventEntity(venueEntity.getId(), artistEntity.getId());

        Attendance attendance = Attendance.builder()
                .score(BigDecimal.TEN)
                .eventId(eventEntity.getId())
                .description("Description")
                .friends(List.of(friendBaseUserEntity.getId()))
                .build();

        // Act
        AttendanceEntity actual = service.createAttendance(providerUid, attendance);

        // Assert
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getBaseUserId()).isEqualTo(baseUserEntity.getId());
        assertThat(actual.getEventId()).isEqualTo(eventEntity.getId());
        assertThat(actual.getScore()).isEqualTo(attendance.score());
        assertThat(actual.getHasPictures()).isEqualTo(Boolean.FALSE);
        assertThat(actual.getDescription()).isEqualTo(attendance.description());
        assertThat(actual.getFriends()).isEqualTo(friendBaseUserEntity.getId().toString());
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
    }

    @Test
    void createAttendance_shouldSaveAndReturnNewAttendanceEntity_withoutOptionals() {
        // Arrange
        String providerUid = UUID.randomUUID().toString();

        BaseUserEntity baseUserEntity = buildAndSaveBaseUserEntity(providerUid);
        VenueEntity venueEntity = buildAndSaveVenueEntity();
        ArtistEntity artistEntity = buildAndSaveArtistEntity();
        EventEntity eventEntity = buildAndSaveEventEntity(venueEntity.getId(), artistEntity.getId());

        Attendance attendance = Attendance.builder()
                .score(BigDecimal.TEN)
                .eventId(eventEntity.getId())
                .description(null)
                .friends(List.of())
                .build();

        // Act
        AttendanceEntity actual = service.createAttendance(providerUid, attendance);

        // Assert
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getBaseUserId()).isEqualTo(baseUserEntity.getId());
        assertThat(actual.getEventId()).isEqualTo(eventEntity.getId());
        assertThat(actual.getScore()).isEqualTo(attendance.score());
        assertThat(actual.getHasPictures()).isEqualTo(Boolean.FALSE);
        assertThat(actual.getDescription()).isEqualTo(attendance.description());
        assertThat(actual.getFriends()).isEqualTo("");
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
    }

    @Test
    void createAttendance_shouldThrowOdeehDuplicateException_whenAttendanceAlreadyExists() {
        // Arrange
        String providerUid = UUID.randomUUID().toString();

        BaseUserEntity baseUserEntity = buildAndSaveBaseUserEntity(providerUid);
        VenueEntity venueEntity = buildAndSaveVenueEntity();
        ArtistEntity artistEntity = buildAndSaveArtistEntity();
        EventEntity eventEntity = buildAndSaveEventEntity(venueEntity.getId(), artistEntity.getId());

        Attendance attendance = Attendance.builder()
                .score(BigDecimal.TEN)
                .eventId(eventEntity.getId())
                .description(null)
                .friends(List.of())
                .build();

        service.createAttendance(providerUid, attendance);

        // Act & Assert
        OdeehDuplicateException exception = assertThrows(OdeehDuplicateException.class, () ->
                service.createAttendance(providerUid, attendance)
        );

        HttpStatus expectedStatus = HttpStatus.CONFLICT;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
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

    @Test
    void deleteAttendance_shouldDeleteAttendanceEntity() {
        // Arrange
        String providerUid = UUID.randomUUID().toString();

        BaseUserEntity baseUserEntity = buildAndSaveBaseUserEntity(providerUid);
        VenueEntity venueEntity = buildAndSaveVenueEntity();
        ArtistEntity artistEntity = buildAndSaveArtistEntity();
        EventEntity eventEntity = buildAndSaveEventEntity(venueEntity.getId(), artistEntity.getId());

        Attendance attendance = Attendance.builder()
                .score(BigDecimal.TEN)
                .eventId(eventEntity.getId())
                .description("Description")
                .friends(List.of())
                .build();

        AttendanceEntity attendanceEntity = service.createAttendance(providerUid, attendance);

        // Act
        service.deleteAttendance(providerUid, attendanceEntity.getId());

        // Assert
        assertThat(repository.findById(attendanceEntity.getId())).isEmpty();
    }

    @Test
    void deleteAttendance_shouldThrowOdeehNotFoundException_whenNoAttendanceEntityFound() {
        // Arrange
        String providerUid = UUID.randomUUID().toString();

        BaseUserEntity baseUserEntity = buildAndSaveBaseUserEntity(providerUid);
        VenueEntity venueEntity = buildAndSaveVenueEntity();
        ArtistEntity artistEntity = buildAndSaveArtistEntity();
        EventEntity eventEntity = buildAndSaveEventEntity(venueEntity.getId(), artistEntity.getId());

        Attendance attendance = Attendance.builder()
                .score(BigDecimal.TEN)
                .eventId(eventEntity.getId())
                .description("Description")
                .friends(List.of())
                .build();

        AttendanceEntity attendanceEntity = service.createAttendance(providerUid, attendance);

        // Act & Assert
        OdeehNotFoundException exception = assertThrows(OdeehNotFoundException.class, () ->
                service.deleteAttendance(providerUid, UUID.randomUUID())
        );

        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void deleteAttendance_shouldThrowOdeehBadRequestException_whenAttendanceNotByAuthenticatedUser() {
        // Arrange
        String providerUid = UUID.randomUUID().toString();
        String requesterUid = UUID.randomUUID().toString();

        BaseUserEntity baseUserEntity = buildAndSaveBaseUserEntity(providerUid);
        BaseUserEntity requesterBaseUserEntity = buildAndSaveBaseUserEntity(requesterUid);
        VenueEntity venueEntity = buildAndSaveVenueEntity();
        ArtistEntity artistEntity = buildAndSaveArtistEntity();
        EventEntity eventEntity = buildAndSaveEventEntity(venueEntity.getId(), artistEntity.getId());

        Attendance attendance = Attendance.builder()
                .score(BigDecimal.TEN)
                .eventId(eventEntity.getId())
                .description("Description")
                .friends(List.of())
                .build();

        AttendanceEntity attendanceEntity = service.createAttendance(providerUid, attendance);

        // Act & Assert
        OdeehBadRequestException exception = assertThrows(OdeehBadRequestException.class, () ->
                service.deleteAttendance(requesterUid, attendanceEntity.getId())
        );

        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    private BaseUserEntity buildAndSaveBaseUserEntity(String providerUid) {
        BaseUserEntity entity = BaseUserEntity.builder()
                .email(UUID.randomUUID().toString())
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
