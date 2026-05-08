package be.odeeh.studio.odeehservice.adapter.in.web.controller;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.AttendanceRequest;
import be.odeeh.studio.odeehservice.adapter.in.web.mapper.AttendanceRequestMapper;
import be.odeeh.studio.odeehservice.application.port.AttendanceServicePort;
import be.odeeh.studio.odeehservice.config.security.FirebaseAuthentication;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceServicePort port;
    private final AttendanceRequestMapper requestMapper;

    @PostMapping
    public ResponseEntity<Void> createAttendance(
            Authentication authentication,
            @RequestBody AttendanceRequest request
    ) {
        FirebaseAuthentication auth = (FirebaseAuthentication) authentication;

        port.createAttendance(auth.getUid(), requestMapper.toDomain(request));

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> createAttendance(
            Authentication authentication,
            @PathVariable UUID id,
            @RequestBody AttendanceRequest request
    ) {
        FirebaseAuthentication auth = (FirebaseAuthentication) authentication;

        port.updateAttendance(auth.getUid(), id, requestMapper.toDomain(request));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> createAttendance(
            Authentication authentication,
            @PathVariable UUID id
    ) {
        FirebaseAuthentication auth = (FirebaseAuthentication) authentication;

        port.deleteAttendance(auth.getUid(), id);

        return ResponseEntity.ok().build();
    }
}
