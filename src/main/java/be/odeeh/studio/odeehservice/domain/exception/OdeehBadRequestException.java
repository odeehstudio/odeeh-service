package be.odeeh.studio.odeehservice.domain.exception;

import org.springframework.http.HttpStatus;

public class OdeehBadRequestException extends OdeehApplicationException {

    public OdeehBadRequestException() {
        super(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value()
        );
    }
}
