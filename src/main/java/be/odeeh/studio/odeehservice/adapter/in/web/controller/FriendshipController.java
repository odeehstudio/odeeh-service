package be.odeeh.studio.odeehservice.adapter.in.web.controller;

import be.odeeh.studio.odeehservice.application.port.FriendshipServicePort;
import be.odeeh.studio.odeehservice.config.security.FirebaseAuthentication;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/friendship")
@AllArgsConstructor
public class FriendshipController {

    private final FriendshipServicePort port;

    @PostMapping("/request/{receiverId}")
    public ResponseEntity<Void> request(Authentication authentication, @PathVariable UUID receiverId) {
        FirebaseAuthentication auth = (FirebaseAuthentication) authentication;

        port.sendFriendshipRequest(
                auth.getUid(),
                receiverId
        );

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/dismiss/{requestId}")
    public ResponseEntity<Void> dismiss(Authentication authentication, @PathVariable UUID requestId) {
        FirebaseAuthentication auth = (FirebaseAuthentication) authentication;

        port.dismissFriendshipRequest(
                requestId,
                auth.getUid()
        );

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/accept/{requestId}")
    public ResponseEntity<Void> accept(Authentication authentication, @PathVariable UUID requestId) {
        FirebaseAuthentication auth = (FirebaseAuthentication) authentication;

        port.acceptFriendshipRequest(
                requestId,
                auth.getUid()
        );

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deny/{requestId}")
    public ResponseEntity<Void> deny(Authentication authentication, @PathVariable UUID requestId) {
        FirebaseAuthentication auth = (FirebaseAuthentication) authentication;

        port.denyFriendshipRequest(
                requestId,
                auth.getUid()
        );

        return ResponseEntity.ok().build();
    }
}
