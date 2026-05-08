package be.odeeh.studio.odeehservice.adapter.in.web.controller;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.FriendshipRequest;
import be.odeeh.studio.odeehservice.adapter.in.web.dto.FriendshipResponse;
import be.odeeh.studio.odeehservice.adapter.in.web.mapper.FriendshipRequestMapper;
import be.odeeh.studio.odeehservice.adapter.in.web.mapper.FriendshipResponseMapper;
import be.odeeh.studio.odeehservice.application.port.FriendshipServicePort;
import be.odeeh.studio.odeehservice.config.security.FirebaseAuthentication;
import be.odeeh.studio.odeehservice.domain.model.FriendshipEntityQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/friendship")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipServicePort port;
    private final FriendshipRequestMapper requestMapper;
    private final FriendshipResponseMapper responseMapper;

    @PostMapping("/connect")
    public ResponseEntity<Void> connect(
            Authentication authentication,
            @RequestBody FriendshipRequest request
    ) {
        FirebaseAuthentication auth = (FirebaseAuthentication) authentication;

        port.connect(auth.getUid(), requestMapper.toDomain(request));

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<FriendshipResponse>> fetchFriendships(Authentication authentication) {
        FirebaseAuthentication auth = (FirebaseAuthentication) authentication;

        List<FriendshipEntityQuery> entities = port.fetchFriendships(auth.getUid());

        return ResponseEntity.ok(responseMapper.toResponse(entities));
    }
}
