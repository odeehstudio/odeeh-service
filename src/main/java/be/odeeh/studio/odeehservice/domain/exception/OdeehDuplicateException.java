package be.odeeh.studio.odeehservice.domain.exception;

import org.springframework.http.HttpStatus;

public class OdeehDuplicateException extends OdeehApplicationException {

    public OdeehDuplicateException(String message) {
        super(
                message,
                "TEST",
                HttpStatus.BAD_REQUEST.value()
        );
    }
}
