package be.odeeh.studio.odeehservice.adapter.out.repository;

import be.odeeh.studio.odeehservice.domain.entity.EventEntity;
import be.odeeh.studio.odeehservice.domain.model.VenueEventEntityQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EventJpaRepository extends JpaRepository<EventEntity, UUID> {
    @Query("""
    SELECT new be.odeeh.studio.odeehservice.domain.model.VenueEventEntityQuery(
        e.id,
        a.name
    )
    FROM EventEntity e
    JOIN ArtistEntity a ON e.artistId = a.id
    WHERE e.venueId = :venueId
    AND LOWER(a.name) LIKE LOWER(CONCAT('%', :query, '%'))
    AND (YEAR(e.startTime) = YEAR(CURRENT_DATE) OR YEAR(e.endTime) = YEAR(CURRENT_DATE))
    """)
    Page<VenueEventEntityQuery> findByVenueIdAndArtistNameContainingIgnoreCase(
            @Param("venueId") UUID venueId,
            @Param("query") String query,
            Pageable pageable
    );
}
