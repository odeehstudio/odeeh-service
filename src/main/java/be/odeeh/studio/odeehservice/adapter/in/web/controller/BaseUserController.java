package be.odeeh.studio.odeehservice.adapter.in.web.controller;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.BaseUserResponse;
import be.odeeh.studio.odeehservice.adapter.in.web.mapper.BaseUserMapper;
import be.odeeh.studio.odeehservice.application.port.BaseUserServicePort;
import be.odeeh.studio.odeehservice.config.security.FirebaseAuthentication;
import be.odeeh.studio.odeehservice.domain.entity.BaseUserEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/base-user")
@AllArgsConstructor
public class BaseUserController {

    private final BaseUserServicePort port;
    private final BaseUserMapper mapper;

    @PostMapping("/register")
    public ResponseEntity<BaseUserResponse> register(Authentication authentication) {
        FirebaseAuthentication auth = (FirebaseAuthentication) authentication;

         BaseUserEntity entity = port.createBaseUser(auth.getUid());

         return ResponseEntity.ok(mapper.map(entity));
    }
}
