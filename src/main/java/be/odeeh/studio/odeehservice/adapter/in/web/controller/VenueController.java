package be.odeeh.studio.odeehservice.adapter.in.web.controller;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.VenueResponse;
import be.odeeh.studio.odeehservice.adapter.in.web.mapper.VenueMapper;
import be.odeeh.studio.odeehservice.application.port.VenueServicePort;
import be.odeeh.studio.odeehservice.domain.entity.VenueEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/venue")
@AllArgsConstructor
public class VenueController {

    private final VenueServicePort port;
    private final VenueMapper mapper;

    @GetMapping
    public ResponseEntity<List<VenueResponse>> search(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") Integer page
    ) {
        Page<VenueEntity> entities = port.search(query, page);

        return ResponseEntity.ok(mapper.map(entities));
    }
}
