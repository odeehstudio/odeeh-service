package be.odeeh.studio.odeehservice.adapter.in.web.controller;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.EventResponse;
import be.odeeh.studio.odeehservice.adapter.in.web.mapper.EventMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("venue/{venueId}/event")
@AllArgsConstructor
public class VenueEventController {

    private final EventMapper mapper;

    @GetMapping
    public ResponseEntity<List<EventResponse>> search(
            @PathVariable("venueId") UUID venueId,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") Integer page
    ) {
        return ResponseEntity.ok(List.of());
    }
}
