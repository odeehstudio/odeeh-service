package be.odeeh.studio.odeehservice.adapter.in.web.controller;

import be.odeeh.studio.odeehservice.adapter.in.web.dto.AttendanceRequest;
import be.odeeh.studio.odeehservice.adapter.in.web.dto.AttendanceResponse;
import be.odeeh.studio.odeehservice.adapter.in.web.mapper.AttendanceRequestMapper;
import be.odeeh.studio.odeehservice.adapter.in.web.mapper.AttendanceResponseMapper;
import be.odeeh.studio.odeehservice.application.port.AttendanceServicePort;
import be.odeeh.studio.odeehservice.config.security.FirebaseAuthentication;
import be.odeeh.studio.odeehservice.domain.model.AttendanceEntityQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceServicePort port;
    private final AttendanceRequestMapper requestMapper;
    private final AttendanceResponseMapper responseMapper;

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
    public ResponseEntity<Void> updateAttendance(
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

    @GetMapping
    public ResponseEntity<List<AttendanceResponse>> fetchAttendances(
            Authentication authentication,
            @RequestParam(defaultValue = "0") Integer page
    ) {
        FirebaseAuthentication auth = (FirebaseAuthentication) authentication;

        List<AttendanceEntityQuery> entities = port.fetchAttendances(auth.getUid(), page);

        return ResponseEntity.ok(responseMapper.toResponse(entities));
    }
}
