package be.odeeh.studio.odeehservice.adapter.in.web.controller;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.BaseUserResponse;
import be.odeeh.studio.odeehservice.adapter.in.web.dto.UsernameRequest;
import be.odeeh.studio.odeehservice.adapter.in.web.mapper.BaseUserMapper;
import be.odeeh.studio.odeehservice.adapter.in.web.mapper.UsernameRequestMapper;
import be.odeeh.studio.odeehservice.application.port.BaseUserServicePort;
import be.odeeh.studio.odeehservice.config.security.FirebaseAuthentication;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/base-user")
@RequiredArgsConstructor
public class BaseUserController {

    private final BaseUserServicePort port;
    private final UsernameRequestMapper requestMapper;
    private final BaseUserMapper responseMapper;

    @PostMapping("/is-user-enrolled")
    public ResponseEntity<Boolean> isUserEnrolled(Authentication authentication) {
        FirebaseAuthentication auth = (FirebaseAuthentication) authentication;

        Boolean isUserEnrolled = port.isUserEnrolled(auth.getUid());

        return ResponseEntity.ok(isUserEnrolled);
    }

    @PostMapping("/is-username-available")
    public ResponseEntity<Boolean> isUsernameAvailable(@RequestBody UsernameRequest request) {
        Boolean isUsernameAvailable = port.isUsernameAvailable(
                requestMapper.toDomain(request)
        );

        return ResponseEntity.ok(isUsernameAvailable);
    }

    @PostMapping("/enroll")
    public ResponseEntity<BaseUserResponse> enroll(
            Authentication authentication,
            @RequestBody UsernameRequest request
    ) {
        FirebaseAuthentication auth = (FirebaseAuthentication) authentication;

        BaseUserEntity entity = port.enroll(
                auth.getUid(),
                requestMapper.toDomain(request)
        );

        return ResponseEntity.ok(responseMapper.map(entity));
    }
}
