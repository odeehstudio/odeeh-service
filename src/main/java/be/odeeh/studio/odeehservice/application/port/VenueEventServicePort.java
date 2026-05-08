package be.odeeh.studio.odeehservice.application.port;

import be.odeeh.studio.odeehservice.domain.model.VenueEventEntityQuery;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface VenueEventServicePort {
    Page<VenueEventEntityQuery> search(UUID venueId, String query, Integer page);
}
