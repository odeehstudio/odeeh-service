package be.odeeh.studio.odeehservice.adapter.out.repository;

import be.odeeh.studio.odeehservice.domain.entity.VenueEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VenueJpaRepository extends JpaRepository<VenueEntity, UUID> {
    Page<VenueEntity> findByNameContainingIgnoreCase(String query, Pageable pageable);
}
