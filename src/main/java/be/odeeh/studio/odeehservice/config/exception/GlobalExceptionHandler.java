package be.odeeh.studio.odeehservice.config.exception;

import be.odeeh.studio.odeehservice.domain.exception.OdeehApplicationException;
import be.odeeh.studio.odeehservice.domain.exception.OdeehDuplicateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OdeehApplicationException.class)
    public ResponseEntity<ErrorResponse> handleOdeehApplicationException(
            OdeehDuplicateException ex,
            WebRequest request
    ) {
        log.error("Application Exception [{}]: {}", ex.getCode(), ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(ex.getStatus())
                .build();

        return ResponseEntity
                .status(ex.getStatus())
                .body(errorResponse);
    }
}
