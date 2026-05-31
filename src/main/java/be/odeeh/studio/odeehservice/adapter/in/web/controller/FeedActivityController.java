package be.odeeh.studio.odeehservice.adapter.in.web.controller;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.AttendanceResponse;
import be.odeeh.studio.odeehservice.adapter.in.web.mapper.AttendanceResponseMapper;
import be.odeeh.studio.odeehservice.application.port.FeedActivityServicePort;
import be.odeeh.studio.odeehservice.config.security.FirebaseAuthentication;
import be.odeeh.studio.odeehservice.domain.model.AttendanceEntityQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedActivityController {

    private final FeedActivityServicePort port;
    private final AttendanceResponseMapper responseMapper;

    @GetMapping
    public ResponseEntity<List<AttendanceResponse>> fetchFeed(
            Authentication authentication,
            @RequestParam(defaultValue = "0") Integer page
    ) {
        FirebaseAuthentication auth = (FirebaseAuthentication) authentication;

        List<AttendanceEntityQuery> entities = port.fetchFeed(auth.getUid(), page);

        return ResponseEntity.ok(responseMapper.toResponse(entities));
    }
}
