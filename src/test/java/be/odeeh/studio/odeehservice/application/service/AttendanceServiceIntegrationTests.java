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
        var providerUid = UUID.randomUUID().toString();
        var friendProviderUid = UUID.randomUUID().toString();

        var baseUserEntity = buildAndSaveBaseUserEntity(providerUid);
        var friendBaseUserEntity = buildAndSaveBaseUserEntity(friendProviderUid);
        var venueEntity = buildAndSaveVenueEntity();
        var artistEntity = buildAndSaveArtistEntity();
        var eventEntity = buildAndSaveEventEntity(venueEntity.getId(), artistEntity.getId());

        var attendance = Attendance.builder()
                .score(BigDecimal.TEN)
                .eventId(eventEntity.getId())
                .description("Description")
                .friends(List.of(friendBaseUserEntity.getId()))
                .build();

        // Act
        var actual = service.createAttendance(providerUid, attendance);

        // Assert
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getBaseUserId()).isEqualTo(baseUserEntity.getId());
        assertThat(actual.getEventId()).isEqualTo(eventEntity.getId());
        assertThat(actual.getScore()).isEqualTo(attendance.score());
        assertThat(actual.getHasPictures()).isEqualTo(Boolean.FALSE);
        assertThat(actual.getDescription()).isEqualTo(attendance.description());
        assertThat(actual.getTaggedBaseUsers()).hasSize(1);
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
    }

    @Test
    void createAttendance_shouldSaveAndReturnNewAttendanceEntity_withoutOptionals() {
        // Arrange
        var providerUid = UUID.randomUUID().toString();

        var baseUserEntity = buildAndSaveBaseUserEntity(providerUid);
        var venueEntity = buildAndSaveVenueEntity();
        var artistEntity = buildAndSaveArtistEntity();
        var eventEntity = buildAndSaveEventEntity(venueEntity.getId(), artistEntity.getId());

        var attendance = Attendance.builder()
                .score(BigDecimal.TEN)
                .eventId(eventEntity.getId())
                .description(null)
                .friends(List.of())
                .build();

        // Act
        var actual = service.createAttendance(providerUid, attendance);

        // Assert
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getBaseUserId()).isEqualTo(baseUserEntity.getId());
        assertThat(actual.getEventId()).isEqualTo(eventEntity.getId());
        assertThat(actual.getScore()).isEqualTo(attendance.score());
        assertThat(actual.getHasPictures()).isEqualTo(Boolean.FALSE);
        assertThat(actual.getDescription()).isEqualTo(attendance.description());
        assertThat(actual.getTaggedBaseUsers()).isEmpty();
        assertThat(actual.getCreatedAt()).isNotNull();
        assertThat(actual.getUpdatedAt()).isNotNull();
    }

    @Test
    void createAttendance_shouldThrowOdeehDuplicateException_whenAttendanceAlreadyExists() {
        // Arrange
        var providerUid = UUID.randomUUID().toString();

        var baseUserEntity = buildAndSaveBaseUserEntity(providerUid);
        var venueEntity = buildAndSaveVenueEntity();
        var artistEntity = buildAndSaveArtistEntity();
        var eventEntity = buildAndSaveEventEntity(venueEntity.getId(), artistEntity.getId());

        var attendance = Attendance.builder()
                .score(BigDecimal.TEN)
                .eventId(eventEntity.getId())
                .description(null)
                .friends(List.of())
                .build();

        service.createAttendance(providerUid, attendance);

        // Act & Assert
        var exception = assertThrows(OdeehDuplicateException.class, () ->
                service.createAttendance(providerUid, attendance)
        );

        var expectedStatus = HttpStatus.CONFLICT;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void createAttendance_shouldThrowOdeehNotFoundException_whenNoBaseUserEntityFound() {
        // Arrange
        var providerUid = UUID.randomUUID().toString();

        var attendance = Attendance.builder().build();

        // Act & Assert
        var exception = assertThrows(OdeehNotFoundException.class, () ->
                service.createAttendance(providerUid, attendance)
        );

        var expectedStatus = HttpStatus.NOT_FOUND;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void createAttendance_shouldThrowOdeehNotFoundException_whenNoEventEntityFound() {
        // Arrange
        var providerUid = UUID.randomUUID().toString();

        var baseUserEntity = buildAndSaveBaseUserEntity(providerUid);

        var attendance = Attendance.builder()
                .eventId(UUID.randomUUID())
                .build();

        // Act & Assert
        var exception = assertThrows(OdeehNotFoundException.class, () ->
                service.createAttendance(providerUid, attendance)
        );

        var expectedStatus = HttpStatus.NOT_FOUND;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void deleteAttendance_shouldDeleteAttendanceEntity() {
        // Arrange
        var providerUid = UUID.randomUUID().toString();

        var baseUserEntity = buildAndSaveBaseUserEntity(providerUid);
        var venueEntity = buildAndSaveVenueEntity();
        var artistEntity = buildAndSaveArtistEntity();
        var eventEntity = buildAndSaveEventEntity(venueEntity.getId(), artistEntity.getId());

        var attendance = Attendance.builder()
                .score(BigDecimal.TEN)
                .eventId(eventEntity.getId())
                .description("Description")
                .friends(List.of())
                .build();

        var attendanceEntity = service.createAttendance(providerUid, attendance);

        // Act
        service.deleteAttendance(providerUid, attendanceEntity.getId());

        // Assert
        assertThat(repository.findById(attendanceEntity.getId())).isEmpty();
    }

    @Test
    void deleteAttendance_shouldThrowOdeehNotFoundException_whenNoAttendanceEntityFound() {
        // Arrange
        var providerUid = UUID.randomUUID().toString();

        var baseUserEntity = buildAndSaveBaseUserEntity(providerUid);
        var venueEntity = buildAndSaveVenueEntity();
        var artistEntity = buildAndSaveArtistEntity();
        var eventEntity = buildAndSaveEventEntity(venueEntity.getId(), artistEntity.getId());

        var attendance = Attendance.builder()
                .score(BigDecimal.TEN)
                .eventId(eventEntity.getId())
                .description("Description")
                .friends(List.of())
                .build();

        var attendanceEntity = service.createAttendance(providerUid, attendance);

        // Act & Assert
        var exception = assertThrows(OdeehNotFoundException.class, () ->
                service.deleteAttendance(providerUid, UUID.randomUUID())
        );

        var expectedStatus = HttpStatus.NOT_FOUND;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void deleteAttendance_shouldThrowOdeehBadRequestException_whenAttendanceNotByAuthenticatedUser() {
        // Arrange
        var providerUid = UUID.randomUUID().toString();
        var requesterUid = UUID.randomUUID().toString();

        var baseUserEntity = buildAndSaveBaseUserEntity(providerUid);
        var requesterBaseUserEntity = buildAndSaveBaseUserEntity(requesterUid);
        var venueEntity = buildAndSaveVenueEntity();
        var artistEntity = buildAndSaveArtistEntity();
        var eventEntity = buildAndSaveEventEntity(venueEntity.getId(), artistEntity.getId());

        var attendance = Attendance.builder()
                .score(BigDecimal.TEN)
                .eventId(eventEntity.getId())
                .description("Description")
                .friends(List.of())
                .build();

        var attendanceEntity = service.createAttendance(providerUid, attendance);

        // Act & Assert
        var exception = assertThrows(OdeehBadRequestException.class, () ->
                service.deleteAttendance(requesterUid, attendanceEntity.getId())
        );

        var expectedStatus = HttpStatus.BAD_REQUEST;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void updateAttendance_shouldUpdateAttendanceEntity() {
        // Arrange
        var providerUid = UUID.randomUUID().toString();
        var friendProviderUid = UUID.randomUUID().toString();

        var baseUserEntity = buildAndSaveBaseUserEntity(providerUid);
        var friendEntity = buildAndSaveBaseUserEntity(friendProviderUid);
        var venueEntity = buildAndSaveVenueEntity();
        var artistEntity = buildAndSaveArtistEntity();
        var eventEntity = buildAndSaveEventEntity(venueEntity.getId(), artistEntity.getId());

        var originalAttendance = Attendance.builder()
                .score(BigDecimal.ZERO)
                .eventId(eventEntity.getId())
                .description("Original")
                .friends(List.of())
                .build();

        var attendanceEntity = service.createAttendance(providerUid, originalAttendance);

        var updatedAttendance = Attendance.builder()
                .score(BigDecimal.TEN)
                .eventId(eventEntity.getId())
                .description("Updated with friends")
                .friends(List.of(friendEntity.getId()))
                .build();

        // Act
        var actual = service.updateAttendance(providerUid, attendanceEntity.getId(), updatedAttendance);

        // Assert
        assertThat(actual.getScore()).isEqualTo(updatedAttendance.score());
        assertThat(actual.getDescription()).isEqualTo(updatedAttendance.description());
        assertThat(actual.getTaggedBaseUsers()).hasSize(1);
    }

    @Test
    void updateAttendance_shouldThrowOdeehNotFoundException_whenAttendanceNotFound() {
        // Arrange
        var providerUid = UUID.randomUUID().toString();
        buildAndSaveBaseUserEntity(providerUid);

        var attendance = Attendance.builder().build();

        // Act & Assert
        var exception = assertThrows(OdeehNotFoundException.class, () ->
                service.updateAttendance(providerUid, UUID.randomUUID(), attendance)
        );

        var expectedStatus = HttpStatus.NOT_FOUND;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void updateAttendance_shouldThrowOdeehBadRequestException_whenAttendanceNotByAuthenticatedUser() {
        // Arrange
        var providerUid = UUID.randomUUID().toString();
        var otherProviderUid = UUID.randomUUID().toString();

        var baseUserEntity = buildAndSaveBaseUserEntity(providerUid);
        var otherUserEntity = buildAndSaveBaseUserEntity(otherProviderUid);
        var venueEntity = buildAndSaveVenueEntity();
        var artistEntity = buildAndSaveArtistEntity();
        var eventEntity = buildAndSaveEventEntity(venueEntity.getId(), artistEntity.getId());

        var attendance = Attendance.builder()
                .score(BigDecimal.TEN)
                .eventId(eventEntity.getId())
                .description("Description")
                .friends(List.of())
                .build();

        var attendanceEntity = service.createAttendance(providerUid, attendance);

        // Act & Assert
        var exception = assertThrows(OdeehBadRequestException.class, () ->
                service.updateAttendance(otherProviderUid, attendanceEntity.getId(), attendance)
        );

        var expectedStatus = HttpStatus.BAD_REQUEST;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    @Test
    void updateAttendance_shouldThrowOdeehNotFoundException_whenAuthenticatedUserNotFound() {
        // Arrange
        var nonExistentProviderUid = UUID.randomUUID().toString();

        var attendance = Attendance.builder().build();

        // Act & Assert
        var exception = assertThrows(OdeehNotFoundException.class, () ->
                service.updateAttendance(nonExistentProviderUid, UUID.randomUUID(), attendance)
        );

        var expectedStatus = HttpStatus.NOT_FOUND;
        assertThat(exception.getStatus()).isEqualTo(expectedStatus.value());
        assertThat(exception.getCode()).isEqualTo(expectedStatus.getReasonPhrase());
    }

    private BaseUserEntity buildAndSaveBaseUserEntity(String providerUid) {
        var entity = BaseUserEntity.builder()
                .username(UUID.randomUUID().toString())
                .providerUid(providerUid)
                .build();

        return baseUserRepository.save(entity);
    }

    private ArtistEntity buildAndSaveArtistEntity() {
        var entity = ArtistEntity.builder()
                .name("Name")
                .type(ArtistType.GROUP)
                .country("BE")
                .build();

        return artistRepository.save(entity);
    }

    private VenueEntity buildAndSaveVenueEntity() {
        var entity = VenueEntity.builder()
                .type(VenueType.FESTIVAL)
                .name("Name")
                .address("Address")
                .city("City")
                .country("Country")
                .build();

        return venueRepository.save(entity);
    }

    private EventEntity buildAndSaveEventEntity(UUID venueId, UUID artistId) {
        var entity = EventEntity.builder()
                .venueId(venueId)
                .artistId(artistId)
                .startTime(LocalDateTime.now())
                .endTime(LocalDateTime.now())
                .build();

        return eventRepository.save(entity);
    }
}
