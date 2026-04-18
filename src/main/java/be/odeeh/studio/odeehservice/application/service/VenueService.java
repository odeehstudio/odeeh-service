package be.odeeh.studio.odeehservice.application.service;

import be.odeeh.studio.odeehservice.adapter.out.repository.VenueJpaRepository;
import be.odeeh.studio.odeehservice.application.port.VenueServicePort;
import be.odeeh.studio.odeehservice.domain.entity.VenueEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class VenueService implements VenueServicePort {

    private final VenueJpaRepository repository;
    private final Integer PAGE_SIZE = 20;

    @Override
    public Page<VenueEntity> search(String query, Integer page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return repository.findByNameContainingIgnoreCase(query, pageable);
    }
}
