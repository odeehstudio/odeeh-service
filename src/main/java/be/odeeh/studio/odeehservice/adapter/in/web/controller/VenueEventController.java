package be.odeeh.studio.odeehservice.adapter.in.web.controller;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.EventResponse;
import be.odeeh.studio.odeehservice.adapter.in.web.mapper.EventResponseMapper;
import be.odeeh.studio.odeehservice.application.port.VenueEventServicePort;
import be.odeeh.studio.odeehservice.domain.model.VenueEventEntityQuery;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/venue/{venueId}/event")
@AllArgsConstructor
public class VenueEventController {

    private final VenueEventServicePort port;
    private final EventResponseMapper responseMapper;

    @GetMapping
    public ResponseEntity<List<EventResponse>> search(
            @PathVariable("venueId") UUID venueId,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") Integer page
    ) {
        Page<VenueEventEntityQuery> entities = port.search(venueId, query, page);
        return ResponseEntity.ok(responseMapper.toResponse(entities));
    }
}
