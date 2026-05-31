package be.odeeh.studio.odeehservice.adapter.out.repository;

import be.odeeh.studio.odeehservice.domain.entity.AttendanceEntity;
import be.odeeh.studio.odeehservice.domain.model.AttendanceEntityQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AttendanceJpaRepository extends JpaRepository<AttendanceEntity, UUID> {
    Boolean existsByEventIdAndBaseUserId(UUID eventId, UUID baseUserId);

    @Query("""
    SELECT new be.odeeh.studio.odeehservice.domain.model.AttendanceEntityQuery(
        att.id,
        att.baseUserId,
        u.username,
        att.score,
        att.description,
        v.name,
        a.name
    )
    FROM AttendanceEntity att
    JOIN BaseUserEntity u ON att.baseUserId = u.id
    JOIN EventEntity e ON att.eventId = e.id
    JOIN VenueEntity v ON e.venueId = v.id
    JOIN ArtistEntity a ON e.artistId = a.id
    WHERE att.baseUserId = :userId
    ORDER BY att.createdAt DESC
    """)
    Page<AttendanceEntityQuery> findAttendancesByUserId(@Param("userId") UUID userId, Pageable pageable);
}
