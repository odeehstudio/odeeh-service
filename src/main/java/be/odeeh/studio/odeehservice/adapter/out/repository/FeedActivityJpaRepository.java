package be.odeeh.studio.odeehservice.adapter.out.repository;

import be.odeeh.studio.odeehservice.domain.entity.FeedActivityEntity;
import be.odeeh.studio.odeehservice.domain.model.AttendanceEntityQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeedActivityJpaRepository extends JpaRepository<FeedActivityEntity, UUID> {

    void deleteAllByAttendanceId(UUID attendanceId);

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
    FROM FeedActivityEntity fa
    JOIN AttendanceEntity att ON fa.attendanceId = att.id
    JOIN BaseUserEntity u ON att.baseUserId = u.id
    JOIN EventEntity e ON att.eventId = e.id
    JOIN VenueEntity v ON e.venueId = v.id
    JOIN ArtistEntity a ON e.artistId = a.id
    WHERE fa.baseUserId = :userId
    ORDER BY fa.createdAt DESC
    """)
    Page<AttendanceEntityQuery> findFeedForUser(@Param("userId") UUID userId, Pageable pageable);
}
