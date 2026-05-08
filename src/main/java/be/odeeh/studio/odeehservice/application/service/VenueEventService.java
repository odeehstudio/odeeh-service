package be.odeeh.studio.odeehservice.application.service;


import be.odeeh.studio.odeehservice.adapter.out.repository.EventJpaRepository;
import be.odeeh.studio.odeehservice.application.port.VenueEventServicePort;
import be.odeeh.studio.odeehservice.application.port.VenueRepositoryPort;
import be.odeeh.studio.odeehservice.domain.entity.VenueEntity;
import be.odeeh.studio.odeehservice.domain.model.VenueEventEntityQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VenueEventService implements VenueEventServicePort {

    private final EventJpaRepository repository;
    private final VenueRepositoryPort venueRepository;
    private final Integer PAGE_SIZE = 20;

    @Override
    public Page<VenueEventEntityQuery> search(UUID venueId, String query, Integer page) {
        VenueEntity venueEntity = venueRepository.findById(venueId);

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return repository.findByVenueIdAndArtistNameContainingIgnoreCase(venueEntity.getId(), query, pageable);
    }
}
