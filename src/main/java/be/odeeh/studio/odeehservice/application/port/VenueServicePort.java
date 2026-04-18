package be.odeeh.studio.odeehservice.application.port;

import be.odeeh.studio.odeehservice.domain.entity.VenueEntity;
import org.springframework.data.domain.Page;

public interface VenueServicePort {
    Page<VenueEntity> search(String query, Integer page);
}
