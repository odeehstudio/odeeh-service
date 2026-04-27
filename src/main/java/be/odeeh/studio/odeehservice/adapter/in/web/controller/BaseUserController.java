package be.odeeh.studio.odeehservice.adapter.in.web.controller;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.BaseUserRequest;
import be.odeeh.studio.odeehservice.adapter.in.web.dto.BaseUserResponse;
import be.odeeh.studio.odeehservice.adapter.in.web.mapper.BaseUserMapper;
import be.odeeh.studio.odeehservice.application.port.BaseUserServicePort;
import be.odeeh.studio.odeehservice.config.security.FirebaseAuthentication;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEnrollmentStatus;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/base-user")
@AllArgsConstructor
public class BaseUserController {

    private final BaseUserServicePort port;
    private final BaseUserMapper mapper;

    @PostMapping("/validate-enrollment")
    public ResponseEntity<BaseUserEnrollmentStatus> validateEnrollment(Authentication authentication) {
        FirebaseAuthentication auth = (FirebaseAuthentication) authentication;

        BaseUserEnrollmentStatus status = port.validateEnrollment(auth.getUid());

        return ResponseEntity.ok(status);
    }

    @PostMapping("/register")
    public ResponseEntity<BaseUserResponse> register(
            Authentication authentication,
            @RequestBody BaseUserRequest request
    ) {
        FirebaseAuthentication auth = (FirebaseAuthentication) authentication;

        BaseUserEntity entity = port.createBaseUser(
                mapper.mapRequestToDomain(request),
                auth.getUid()
        );

        return ResponseEntity.ok(mapper.map(entity));
    }
}
